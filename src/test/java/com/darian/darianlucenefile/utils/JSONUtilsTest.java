package com.darian.darianlucenefile.utils;


import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


/***
 * Test for {@link JSONUtils}
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/8/1  3:03
 */
public class JSONUtilsTest {

    /**
     * Test for {@link JSONUtils#beanToJson(java.lang.Object)}
     */
    @Test
    public void beanToJson() {
        StudentModule studentModule = new StudentModule();
        studentModule.setId(1L);
        studentModule.setStuName("stuName");
        studentModule.setStuAge(1);
        studentModule.setStu_Addr("stu_Addr");

        String jsonString = "{\"id\":1,\"stuName\":\"stuName\",\"stuAge\":1,\"stu_Addr\":\"stu_Addr\"}";
        Assert.assertEquals(jsonString, JSONUtils.beanToJson(studentModule));
    }

    /**
     * {@link JSONUtils#beanToJsonPrinter(java.lang.Object)}
     */
    @Test
    public void beanToJsonPrinter() {
        StudentModule studentModule = new StudentModule();
        studentModule.setId(1L);
        studentModule.setStuName("stuName");
        studentModule.setStuAge(1);
        studentModule.setStu_Addr("stu_Addr");

        System.out.println(JSONUtils.beanToJsonPrinter(studentModule));
        String jsonString = JSONUtils.beanToJsonPrinter(studentModule);
        Assert.assertEquals(jsonString, JSONUtils.beanToJsonPrinter(studentModule));
    }

    /**
     * {@link JSONUtils#jsonToBean(java.lang.String, java.lang.Class)}
     */
    @Test
    public void jsonToBean() {

        String jsonString = "{\"id\":1,\"stuName\":\"stuName\",\"stuAge\":1,\"stu_Addr\":\"stu_Addr\"}";

        StudentModule studentModule = new StudentModule();
        studentModule.setId(1L);
        studentModule.setStuName("stuName");
        studentModule.setStuAge(1);
        studentModule.setStu_Addr("stu_Addr");

        Assert.assertEquals(studentModule, JSONUtils.jsonToBean(jsonString, StudentModule.class));

    }

    /**
     * {@link JSONUtils#jsonToList(java.lang.String, java.lang.Class)}
     */
    @Test
    public void jsonToList() {
        StudentModule studentModule = new StudentModule();
        studentModule.setId(1L);
        studentModule.setStuName("stuName");
        studentModule.setStuAge(1);
        studentModule.setStu_Addr("stu_Addr");

        String jsonListString = "[{\"id\":1,\"stuName\":\"stuName\",\"stuAge\":1,\"stu_Addr\":\"stu_Addr\"}]";
        Assert.assertEquals(ListUtils.of(studentModule), JSONUtils.jsonToList(jsonListString, StudentModule.class));
    }

    /**
     * {@link JSONUtils#jsonToMap(java.lang.String, java.lang.Class, java.lang.Class)}
     */
    @Test
    public void jsonToMap() {
        Map<String, String> map = new HashMap<>();
        map.put("aaa", "aaa");

        System.out.println(JSONUtils.beanToJson(map));
        String jsonMapString = "{\"aaa\":\"aaa\"}";

        Assert.assertEquals(map, JSONUtils.jsonToMap(jsonMapString, String.class, String.class));
    }

    /**
     * {@link JSONUtils#beanToJsonByLowerCase(java.lang.Object)}
     */
    @Test
    public void beanToJsonByLowerCase() {

        StudentModule studentModule = new StudentModule();
        studentModule.setId(1L);
        studentModule.setStuName("stuName");
        studentModule.setStuAge(1);
        studentModule.setStu_Addr("stu_Addr");

        String jsonString = "{\"id\":1,\"stu_name\":\"stuName\",\"stu_age\":1,\"stu_addr\":\"stu_Addr\"}";
        Assert.assertEquals(jsonString, JSONUtils.beanToJsonByLowerCase(studentModule));
    }

    /**
     * {@link JSONUtils#jsonToBeanByLowerCase(java.lang.String, java.lang.Class)}
     */
    @Test
    public void jsonToBeanByLowerCase() {

        String jsonString = "{\"id\":1,\"stu_name\":\"stuName\",\"stu_age\":1,\"stu_addr\":\"stu_Addr\"}";

        StudentModule studentModule = new StudentModule();
        studentModule.setId(1L);
        studentModule.setStuName("stuName");
        studentModule.setStuAge(1);
        studentModule.setStu_Addr("stu_Addr");

        Assert.assertEquals(studentModule, JSONUtils.jsonToBeanByLowerCase(jsonString, StudentModule.class));
    }

    @Data
    public static class StudentModule {

        private long id;

        private String stuName;

        private int stuAge;

        private String stu_Addr;

    }
}