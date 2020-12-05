package com.darian.darianlucenefile.repository;

import com.darian.darianlucenefile.config.DarianGitRepConfig;
import com.darian.darianlucenefile.config.DarianLuceneConfig;
import com.darian.darianlucenefile.domain.ConvertUtils;
import com.darian.darianlucenefile.domain.CustomerFile;
import com.darian.darianlucenefile.exception.CustomerRuntimeException;
import com.darian.darianlucenefile.utils.AssertUtils;
import com.darian.darianlucenefile.utils.CustomerNumberUtils;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.wltea.analyzer.lucene.IKAnalyzer;


import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.darian.darianlucenefile.config.DocumentContants.*;


/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/13  22:31
 */
@Slf4j
@Repository
public class LuceneRepository {

    @Resource
    private DarianLuceneConfig darianLuceneConfig;

    @Resource
    private CustomerFileRepository customerFileRepository;

    private boolean INIT_FINISHED = false;

    private static RAMDirectory ramDirectory;


    /**
     * 创建索引
     */
    public void createIndex() {
        String directoryPath = darianLuceneConfig.getDirectoryPath();
        String docsFilePath = darianLuceneConfig.getDocsFilePath();
        Pattern pattern = darianLuceneConfig.getPattern();
        AssertUtils.assertTrue(StringUtils.hasText(directoryPath),
                "darian.lucene.config.directoryPath must be not blank , 索引文件不能为空");
        AssertUtils.assertTrue(StringUtils.hasText(docsFilePath),
                "darian.lucene.config.docsFilePath must not blank");

        try {
            //原始文档的路径
            List<File> fileList = new ArrayList<>();
            getAllItemFileList(docsFilePath, fileList, pattern);

            // 初始化索引库
            Directory directory = initDirectory();
            // 初始化分词器
            Analyzer analyzer = initAnalyzer();
            // 创建一个indexwriter对象，并删除所有的索引
            IndexWriter indexWriter = initIndexWriter(directory, new IndexWriterConfig(analyzer));

            List<Document> documentList = new ArrayList<>();
            for (File itemFile : fileList) {
                CustomerFile customerFile = this.insertIntoCustomerFileRepository(itemFile);
                Document document = this.fileInsertRepositoryAndToDocument(itemFile, customerFile);
                documentList.add(document);
            }
            indexWriter.addDocuments(documentList);
            indexWriter.commit();
            //关闭IndexWriter对象。
            indexWriter.close();
        } catch (IOException e) {
            log.error("构建索引库发生错误！\n", e);
            throw new CustomerRuntimeException("构建索引库发生错误！\n", e);
        } finally {
            customerFileRepository.setInitFinshFinshed();
            INIT_FINISHED = true;
        }
        log.info("索引库加载完成！");
    }

    /**
     * 并返回需要的 Document 对象
     *
     * @param itemFile
     * @return
     */
    private Document fileInsertRepositoryAndToDocument(File itemFile, CustomerFile customerFile) {
        //创建document对象
        Document document = new Document();
        //创建 文件名 域 第一个参数：域的名称 第二个参数：域的内容 第三个参数：是否存储
        Field fileSimpleNameField = new TextField(FILE_SIMPLE_NAME_INDEX_NAME, customerFile.getFileSimpleName(), Field.Store.YES);
        // 文件内容的处理
        String fileContent = ConvertUtils.doInsertContextIndexPre(itemFile);
        // 文件内容域
        Field fileContentDetailField = new TextField(FILE_CONTENT_DETAIL_INDEX_NAME, fileContent, Field.Store.YES);

        // 文件路径 -  文件路径域（不分析、不索引、只存储）
        Field filePathSubDocsFilePathField = new StoredField(FILE_PATH_SUB_DOCS_FILE_PATH_INDEX_NAME,
                customerFile.getFilePathSubDocsFilePath());
        //创建field对象，将field添加到document对象中
        document.add(fileSimpleNameField);
        document.add(fileContentDetailField);
        document.add(filePathSubDocsFilePathField);
        return document;
    }

    private CustomerFile insertIntoCustomerFileRepository(File itemFile) {
        CustomerFile customerFile = new CustomerFile();
        customerFile.setFileSimpleName(itemFile.getName());
        customerFile.setFileFullName(itemFile.toString());
        customerFile.setGiteeUrl(DarianGitRepConfig.getGiteeUrlFromFullName(itemFile.toString()));
        customerFile.setGiteeRawUrl(customerFile.getGiteeUrl().replace("/tree/", "/raw/"));
        customerFile.setGiteeEditUrl(customerFile.getGiteeUrl().replace("/tree/", "/edit/"));
        customerFile.setGitHubUrl(DarianGitRepConfig.getGitHubUrlFromFullName(itemFile.toString()));
        customerFile.setGiteeWebIdeEditUrl(DarianGitRepConfig.getGiteeWebIdeEditUrlFromFullName(itemFile.toString()));
        customerFile.setFilePathSubDocsFilePath(DarianGitRepConfig.getFilePathSubDocsPath(itemFile.toString()));
        customerFileRepository.insertCustomerFile(customerFile.getFilePathSubDocsFilePath(), customerFile);
        return customerFile;
    }

    public List<CustomerFile> multiFiledQueryParser(String searchParamterString) {

        AssertUtils.assertTrue(StringUtils.hasText(darianLuceneConfig.getDirectoryPath()), "索引文件路径不能为空");
        AssertUtils.assertTrue(StringUtils.hasText(searchParamterString), "searchParamterString must be not blank");
        AssertUtils.assertTrue(INIT_FINISHED, "索引库-正在构建...");

        List<CustomerFile> customerFileList = new ArrayList<>();

        try {
            Directory directory = getDirectory();

            //创建IndexReader对象，需要指定Directory对象
            IndexReader indexReader = DirectoryReader.open(directory);
            //创建Indexsearcher对象，需要指定IndexReader对象
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            // 可以指定默认搜索的域是多个
            String[] fields = {FILE_SIMPLE_NAME_INDEX_NAME, FILE_CONTENT_DETAIL_INDEX_NAME};
            //创建一个MulitFiledQueryParser对象
            Analyzer analyzer = new IKAnalyzer();
            MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
            Query query = queryParser.parse(searchParamterString);
            //执行查询

            //第一个参数是查询对象，第二个参数是查询结果返回的最大值
            TopDocs topDocs = indexSearcher.search(query, darianLuceneConfig.getResutMaxSize());

            //查询结果的总条数
            log.debug("查询的条件是[" + searchParamterString + "] 查询结果的总条数：[" + topDocs.totalHits + "]");
            //遍历查询结果
            //topDocs.scoreDocs存储了document对象的id

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                // scoreDoc.doc属性就是document对象的id
                // int doc = scoreDoc.doc;
                // 根据document的id找到document对象
                Document document = indexSearcher.doc(scoreDoc.doc);
                // 转化的时候，做处理
                CustomerFile customerFile = documentConvertToCustomerFile(query, analyzer, document);
                customerFileList.add(customerFile);
            }
            //关闭indexreader对象
            indexReader.close();
        } catch (Exception e) {
            log.error("查询出现问题：\n", e);
            throw new CustomerRuntimeException("查询出现问题：\n", e);
        }
        return customerFileList;
    }

    /**
     * 高亮显示的处理
     * red 分词器的
     *
     * @param query
     * @param analyzer
     * @param document
     * @throws Exception
     */
    private CustomerFile documentConvertToCustomerFile(Query query, Analyzer analyzer,
                                                       Document document) {
        CustomerFile customerFile = new CustomerFile();

        QueryScorer queryScorer = new QueryScorer(query);
//        List<String> searchParamStringList = initSearchParamStringList(analyzer, searchParamString);

        //设置高亮标签,可以自定义
        Formatter formatter = new SimpleHTMLFormatter(darianLuceneConfig.getRedHighLightBegin(),
                darianLuceneConfig.getRedHighLightEnd());
        /**创建Fragmenter*/
        Fragmenter fragmenter = new SimpleFragmenter(darianLuceneConfig.getContentMaxSize());
        //高亮分析器
        Highlighter highlight = new Highlighter(formatter, queryScorer);
        highlight.setEncoder(new SimpleHTMLEncoder());
        highlight.setTextFragmenter(fragmenter);
        // fieldname是域名，如"title",fieldContent是d.get("title");

        try {
            /**处理文件名*/
            String fileSimpleNameValue = doRedHighLight(highlight, analyzer, document.get(FILE_SIMPLE_NAME_INDEX_NAME));
//            fileNameValue = doCustomerHighLight(fileNameValue, searchParamStringList, searchParamString);
//             fileNameValue = subEndIncompleteLabels(fileNameValue);


            /**处理文件内容*/
            String fileContentDetailValue = doRedHighLight(highlight, analyzer, document.get(FILE_CONTENT_DETAIL_INDEX_NAME));

            customerFile.setFileSimpleName(fileSimpleNameValue);
            customerFile.setContentDetail(fileContentDetailValue);
            customerFile.setFilePathSubDocsFilePath(document.get(FILE_PATH_SUB_DOCS_FILE_PATH_INDEX_NAME));

        } catch (Exception e) {
            log.error("高亮处理错误！！！", e);
            throw new CustomerRuntimeException("高亮处理错误！！！", e);
        }

        return customerFile;
    }

    private String doRedHighLight(Highlighter highlight, Analyzer analyzer, String index_value_String) {
        String documentFileNameValue = index_value_String;
        String fileNameValue = null;
        try {
            fileNameValue = highlight.getBestFragment(analyzer, FILE_SIMPLE_NAME_INDEX_NAME, documentFileNameValue);
        } catch (Exception e) {
            log.error("高亮处理错误！", e);
            throw new CustomerRuntimeException("高亮处理错误！", e);
        }
        // 高亮处理如果返回空，那么把原来的值返回去。 发生错误，会抛出异常的。
        if (fileNameValue == null) {
            if (documentFileNameValue.length() > darianLuceneConfig.getContentMaxSize()) {
                int indexBegin = 0;
                int indexEnd = CustomerNumberUtils.min(indexBegin + darianLuceneConfig.getContentMaxSize(),
                        documentFileNameValue.length());
                if (indexEnd < (indexBegin + darianLuceneConfig.getContentMaxSize())) {
                    indexBegin = indexEnd - darianLuceneConfig.getContentMaxSize();
                    indexBegin = CustomerNumberUtils.max(indexBegin, 0);
                }
                documentFileNameValue = documentFileNameValue.substring(indexBegin, indexEnd);
            }
            fileNameValue = documentFileNameValue;
        }
        fileNameValue = fileNameValue != null ? fileNameValue : documentFileNameValue;
        return fileNameValue;
    }


    /**
     * 1. 先去活得查询缓存索引库
     * 2. 查询时索引库
     * 3. 放入缓存
     *
     * @return
     */
    private Directory getDirectory() {
        if (ramDirectory != null) {
            return ramDirectory;
        }
        Directory directory = null;
        if (darianLuceneConfig.getRamOpen()) {
            if (ramDirectory == null) {
                createIndex();
            }
            directory = ramDirectory;
        } else {
            //创建一个Directory对象，指定索引库存放的路径
            File directoryFile = new File(darianLuceneConfig.getDirectoryPath());
            AssertUtils.assertTrue(directoryFile.exists(),
                    "directory[" + darianLuceneConfig.getDirectoryPath() + "]does not exist , 索引文件路径不存在！");
            try {
                directory = FSDirectory.open(Paths.get(darianLuceneConfig.getDirectoryPath()));
            } catch (IOException e) {
                log.error(" 构建 Directory 出现异常 ", e);
                throw new CustomerRuntimeException(" 构建 Directory 出现异常 ", e);
            }
        }
        AssertUtils.assertTrue(directory != null, "directory is null \n 索引库等于空！");
        return directory;
    }

    private static IndexWriter initIndexWriter(Directory directory, IndexWriterConfig indexWriterConfig) {
        IndexWriter indexWriter = null;
        try {
            // 调用时 根据 分词器创建 IndexWriterConfig 对象，
            //创建一个indexwriter对象
            indexWriter = new IndexWriter(directory, indexWriterConfig);
            // 删除所有的索引
            indexWriter.deleteAll();
        } catch (IOException e) {
            log.error("LuceneRepository#initIndexWriter\n 构建 indexWriter 异常，或者 删除所有索引异常!\n", e);
            throw new CustomerRuntimeException("LuceneRepository#initIndexWriter\n 构建 indexWriter 异常，或者 删除所有索引异常!\n", e);
        }
        return indexWriter;
    }

    /**
     * 初始化索引库
     *
     * @return
     */
    private Directory initDirectory() {

        Directory directory = null;
        if (darianLuceneConfig.getRamOpen()) {
            //索引库还可以存放到内存中
            ramDirectory = new RAMDirectory();
            directory = ramDirectory;
        } else {
            //指定索引库的存放位置Directory对象
            try {
                directory = FSDirectory.open(Paths.get(darianLuceneConfig.getDirectoryPath()));
            } catch (IOException e) {
                log.error(" 构建 Directory 出现异常 ", e);
                throw new CustomerRuntimeException(" 构建 Directory 出现异常 ", e);
            }
        }
        return directory;
    }

    /**
     * 初始化分词器
     *
     * @return
     */
    private Analyzer initAnalyzer() {
        Analyzer analyzer = null;
        if (darianLuceneConfig.getUseIK()) {
            // 使用 IK 分词器进行解析
            analyzer = new IKAnalyzer();
        } else {
            //指定一个标准分析器，对文档内容进行分析
            analyzer = new StandardAnalyzer();
        }
        return analyzer;
    }


    public static void getAllItemFileList(String filePath, List<File> fileList, Pattern pattern) {
        doGetAllItemFileList(filePath, fileList, pattern);
        log.info("一共找到所有的需要索引到文件总个数：[" + fileList.size() + "]");
        AssertUtils.assertFalse(fileList.isEmpty(), "FileList not exit  找不到要索引的文件");
    }

    private static void doGetAllItemFileList(String filePath, List<File> fileList, Pattern pattern) {

        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        String fileName = file.getName();
        log.debug("文件名：[" + fileName + "]");
        if (file.isFile()) {
            if (pattern.matcher(fileName).find()) {
                fileList.add(file);
            }
        } else {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                log.debug(fileName + " Directory is empty");
                return;
            }
            for (File itemFile : files) {
                if (!itemFile.exists()) {
                    log.debug(itemFile + " File is not exists");
                    return;
                }
                doGetAllItemFileList(itemFile.toString(), fileList, pattern);
            }
        }
    }


}
