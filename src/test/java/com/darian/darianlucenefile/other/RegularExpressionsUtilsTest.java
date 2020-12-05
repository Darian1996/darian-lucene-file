//package com.darian.darianlucenefile.other;
//
//import org.junit.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.File;
//
///***
// *
// *
// * @author <a href="mailto:1934849492@qq.com">Darian</a>
// * @date 2020/4/13  2:22
// */
//@SpringBootTest
//public class RegularExpressionsUtilsTest {
//
//    @Test
//    public static void test() {
//        String string =
//                "## 对比表格\n"
//                        + "\n"
//                        + "| \\            | Thymeleaf              | Velocity               | JSP                              |\n"
//                        + "| ------------ | ---------------------- | ---------------------- | -------------------------------- |\n"
//                        + "| **学习曲线** | 最简单                 | 中等                   | 中等偏上                         |\n"
//                        + "| **友好性**   | HTML 、XML 友好        | HTML 不太友好          | HTML（多种格式约束）、XML 友好   |\n"
//                        + "| **扩展性**   | 提供标签和属性方式扩展 | 非标签或属性方式扩展   | 扩展能力最强，自定义标签等       |\n"
//                        + "| **移植性**   | 移植性强，Spring 生态  | 移植性强，缺少周边生态 | 必须是 Servlet  或  JSP 容器     |\n"
//                        + "| **性能**     | 解释执行，性能较差     | 解释执行，性能良好     | 翻译成源码，再编译执行，性能优秀 |\n"
//                        + "\n"
//                        + "## 使用场景\n"
//                        + "\n"
//                        + "| \\          | JSP                          | Velocity                       | Thymeleaf             |\n"
//                        + "| ---------- | ---------------------------- | ------------------------------ | --------------------- |\n"
//                        + "| **适合**   | 高性能、Servlet / JSP 天然性 | 性能良好、移植性强、大多数场景 | 后台系统、HTML 5 应用 |\n"
//                        + "| **不适合** | 非 Servlet/JSP 环境          | HTML 5 应用                    | 访问量大，性能敏感    |";
//        //System.out.println(string);
//        string = string.replaceAll("\\| -+", "");
//        string = string.replaceAll("\\| ", "");
//        string = string.replaceAll(" \\|", "");
//
//        System.out.println();
//        //System.out.println(string);
//
//        //System.exit(1);
//        //System.out.println(replaceWhiteSpace("sdfas dafdfa     ajgoiaofaj   oasgjaosf "));
//
//        //String str = doInsertContextIndexPre();
//
//        //System.out.println(str);
//        //
//        //String filePath = "C:\\Users\\wb-zrj546555\\Desktop\\Darian1996-docs-master\\docs\\mercyblitz_md\\GP-public\\深入java世界系列\\2018-12"
//        //+ "-27-深入 JAVA 系列之 NIO2 文件事件通知.md";
//        String filePath = "C:\\Users\\wb-zrj546555\\Desktop\\Darian1996-docs-master\\docs\\mercyblitz_md\\GP-public\\Spring Boot "
//                + "系列\\2018-12-14-Spring Boot 系列之渲染引擎选型.md";
//        String contentString = RegularExpressionsUtils.doInsertContextIndexPre(new File(filePath), "utf-8");
//
//        System.out.println(contentString);
//    }
//}
