package com.darian.darianlucenefile.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/6/7  1:00
 */
public class JSONUtils {

    private static final Logger log = LoggerFactory.getLogger(JSONUtils.class);

    private static final ObjectMapper mObjectMapper = new ObjectMapper();
    private static final ObjectMapper mObjectMapperLowerCaseWithUnderScores = new ObjectMapper();

    //在这里进行配置全局
    static {
        //有时JSON字符串中含有我们并不需要的字段，那么当对应的实体类中不含有该字段时，会抛出一个异常，告诉你有些字段（java 原始类型）没有在实体类中找到
        //设置为false即不抛出异常，并设置默认值 int->0 double->0.0
        mObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mObjectMapperLowerCaseWithUnderScores.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //序列化时添加下划线
        mObjectMapperLowerCaseWithUnderScores.setPropertyNamingStrategy(new PropertyNamingStrategy.SnakeCaseStrategy());
    }

    public static String beanToJson(Object object) {
        if (object != null) {
            try {
                return mObjectMapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                log.error("bean to json exception", e);
            }
        }
        return "";
    }

    public static String beanToJsonPrinter(Object object) {
        if (object != null) {
            try {
                return mObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            } catch (JsonProcessingException e) {
                log.error("bean to json exception", e);
            }
        }
        return "";
    }

    public static <T> T jsonToBean(String json, Class<T> classType) {
        if (isNotBlank(json) && classType != null) {
            try {
                return mObjectMapper.readValue(json, classType);
            } catch (IOException e) {
                log.error("json to bean exception", e);
            }
        }
        return null;
    }

    public static <T> List<T> jsonToList(String json, Class<T> classType) {
        if (isNotBlank(json) && classType != null) {
            try {
                return mObjectMapper.readValue(json, mObjectMapper.getTypeFactory().constructCollectionType(List.class, classType));
            } catch (IOException e) {
                log.error("json to list exception", e);
            }
        }
        return null;
    }

    public static <k, v> Map<k, v> jsonToMap(String json, Class<k> kType, Class<v> vType) {
        if (isNotBlank(json)) {
            try {
                return mObjectMapper.readValue(json, mObjectMapper.getTypeFactory().constructMapType(Map.class, kType, vType));
            } catch (IOException e) {
                log.error("json to Map exception", e);
            }
        }
        return null;
    }

    public static String beanToJsonByLowerCase(Object object) {
        if (object != null) {
            try {
                return mObjectMapperLowerCaseWithUnderScores.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                log.error("bean to json with lowerCase exception", e);
            }
        }
        return "";
    }

    public static <T> T jsonToBeanByLowerCase(String json, Class<T> classType) {
        if (isNotBlank(json)) {
            try {
                return mObjectMapperLowerCaseWithUnderScores.readValue(json, classType);
            } catch (IOException e) {
                log.error("json to Bean with lowerCase exception", e);
            }
        }
        return null;
    }

    private static boolean isNotBlank(String string) {
        return string != null && !"".equals(string);
    }
}