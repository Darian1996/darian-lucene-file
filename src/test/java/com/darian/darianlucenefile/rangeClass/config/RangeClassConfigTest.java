package com.darian.darianlucenefile.rangeClass.config;

import com.darian.darianlucenefile.rangeClass.domain.CourseDO;
import com.darian.darianlucenefile.rangeClass.domain.CourseRuleEnums;
import com.darian.darianlucenefile.rangeClass.domain.GradeDO;
import com.darian.darianlucenefile.rangeClass.domain.SchoolDO;
import com.darian.darianlucenefile.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.darian.darianlucenefile.utils.JSONUtils.beanToJsonPrinter;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class RangeClassConfigTest {

    @Resource
    private RangeClassConfig rangeClassConfig;

    @Test
    public void testRangeClassService() throws Exception {
        SchoolDO schoolDO = rangeClassConfig.getSchoolDO();
        //log.info(beanToJsonPrinter(schoolDO));
        log.info("学校的配置：一周上几天课");
        log.info(beanToJsonPrinter(schoolDO.getBaseDays()));
        log.info("学校的配置：一天上几节课");
        log.info(beanToJsonPrinter(schoolDO.getOneDaySourceCount()));
        log.info("学校的配置：不用上的课程的集合, 是否实例化完成");
        log.info(beanToJsonPrinter(!schoolDO.getMustNotMakeClassXYSet().isEmpty()));
        log.info("学校的配置：一周一共多少课时");
        log.info(beanToJsonPrinter(schoolDO.getWeekSourceCount()));

        Map<String, CourseDO> courseDOMap = rangeClassConfig.getCourseDOMap();

        //log.info(beanToJsonPrinter(courseDOMap));


        log.info("课程的配置：课程的数目");
        log.info(beanToJsonPrinter(courseDOMap.entrySet().size()));
        log.info("课程的配置：课程的列表");
        log.info(beanToJsonPrinter(courseDOMap.keySet()));


        Map<Integer, GradeDO> gradeDOMap = rangeClassConfig.getGradeDOMap();
        for (Entry<Integer, GradeDO> integerGradeDOEntry : gradeDOMap.entrySet()) {
            Integer gradeNum = integerGradeDOEntry.getKey();
            GradeDO gradeDO = integerGradeDOEntry.getValue();
            Integer gradeDOCount = gradeDO.getCourseCountMap().values()
                    .stream()
                    .mapToInt(Integer::valueOf)
                    .reduce(Integer::sum).orElse(0);
            log.info("[{}]年级一共[{}]课时：", gradeDO.getGradeNum(), gradeDOCount);
//            log.info("{}", beanToJsonPrinter(gradeDO));

        }

        // 语文配置
        log.info("语文配置");
        log.info("" + beanToJsonPrinter(courseDOMap.get("CHINESE")));
        CourseRuleEnums chineseCourseRuleEnums = courseDOMap.get("CHINESE").getCourseRuleDOList().get(0)
                .getCourseRuleEnums();
        log.info(beanToJsonPrinter(chineseCourseRuleEnums));

        log.info("班级配置：");
        log.info("班级个数：" + rangeClassConfig.getClassDOMap().entrySet().size());

//        log.info(JSONUtils.beanToJsonPrinter(rangeClassConfig.getClassDOMap()));

        log.info("老师的配置：");
        log.info("老师个数：" + rangeClassConfig.getTeacherDOMap().entrySet().size());
//        log.info(JSONUtils.beanToJsonPrinter(rangeClassConfig.getTeacherDOMap()));


        //log.info(JSONUtils.beanToJsonPrinter(rangeClassConfig.getClassDOMap()));
    }

    private static void printFormatJson(Object object) {
        System.out.println(beanToJsonPrinter(object));
        //        System.out.println(JSON.toJSONString(object,
        //                SerializerFeature.PrettyFormat,
        //                SerializerFeature.WriteMapNullValue,
        //                SerializerFeature.WriteDateUseDateFormat));
    }
}