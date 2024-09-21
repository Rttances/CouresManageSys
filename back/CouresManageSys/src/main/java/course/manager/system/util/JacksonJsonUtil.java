package course.manager.system.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * JacksonJSON序列化反序列化工具
 */
@Slf4j
@NoArgsConstructor
public class JacksonJsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final ObjectMapper OBJECT_MAPPER_SNAKE_CASE = new ObjectMapper();
    // 日期格式化
    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        //对象的所有字段全部列入
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //忽略空Bean转json的错误
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    static {
        //对象的所有字段全部列入
        OBJECT_MAPPER_SNAKE_CASE.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        OBJECT_MAPPER_SNAKE_CASE.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //忽略空Bean转json的错误
        OBJECT_MAPPER_SNAKE_CASE.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        OBJECT_MAPPER_SNAKE_CASE.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        OBJECT_MAPPER_SNAKE_CASE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //转换为下划线
        OBJECT_MAPPER_SNAKE_CASE.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }


    /**
     * 对象转Json格式字符串
     *
     * @param obj 对象
     * @return Json格式字符串
     */
    public static <T> String toJSONString(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Parse Object to String error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 对象转file
     *
     * @param fileName
     * @param obj
     */
    public static void toJSONFile(String fileName, Object obj) {
        if (obj == null) {
            return;
        }
        try {
            OBJECT_MAPPER.writeValue(new File(fileName), obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对象转Json格式字符串; 属性名从驼峰改为下划线形式
     *
     * @param obj 对象
     * @return Json格式字符串
     */
    public static <T> String toJSONStringFieldSnakeCase(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            ObjectMapper objectMapper = OBJECT_MAPPER_SNAKE_CASE;
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Parse Object to String error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 字符串转换为自定义对象； 属性名从下划线形式改为驼峰
     *
     * @param str   要转换的字符串
     * @param clazz 自定义对象的class对象
     * @return 自定义对象
     */
    public static <T> T parseObjectFieldSnakeCase(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            ObjectMapper objectMapper = OBJECT_MAPPER_SNAKE_CASE;
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 字符串转换为自定义对象(List)； 属性名从下划线形式改为驼峰
     *
     * @param str           要转换的字符串
     * @param typeReference 自定义对象的typeReference List 对象
     * @return 自定义对象
     */
    public static <T> List<T> parseListFieldSnakeCase(String str, TypeReference<List<T>> typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            ObjectMapper objectMapper = OBJECT_MAPPER_SNAKE_CASE;
            return objectMapper.readValue(str, typeReference);
        } catch (Exception e) {
            log.warn("Parse String to Object error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 对象转Json格式字符串(格式化的Json字符串)
     *
     * @param obj 对象
     * @return 美化的Json格式字符串
     */
    public static <T> String toJSONStringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Parse Object to String error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 字符串转换为自定义对象
     *
     * @param str   要转换的字符串
     * @param clazz 自定义对象的class对象
     * @return 自定义对象
     */
    public static <T> T parseObject(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : OBJECT_MAPPER.readValue(str, clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 字符串转换为自定义字段转为list
     *
     * @param str
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T parseList(String str, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : OBJECT_MAPPER.readValue(str, typeReference));
        } catch (IOException e) {
            log.warn("Parse String to Object error", e);
            return null;
        }
    }

    public static <T> T toJSONString(String str, Class<?> collectionClazz, Class<?>... elementClazz) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClazz, elementClazz);
        try {
            return OBJECT_MAPPER.readValue(str, javaType);
        } catch (IOException e) {
            log.warn("Parse String to Object error : {}" + e.getMessage());
            return null;
        }
    }
}

