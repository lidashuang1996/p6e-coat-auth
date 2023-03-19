package club.p6e.coat.file.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class JsonUtil {

    /**
     * OBJECT_MAPPER 对象
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 序列化对象
     *
     * @param o 对象
     * @return 序列化内容
     */
    public static String toJson(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 反序列化 JSON 到对象
     *
     * @param json   json 内容
     * @param tClass 对象类型
     * @param <T>    类型
     * @return 对象
     */
    public static <T> T fromJson(String json, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(json, tClass);
        } catch (Exception e) {
            return null;
        }
    }

    public static <K, V> Map<K, V> fromJsonToMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructParametricType(Map.class, kClass, vClass));
        } catch (Exception e) {
            return null;
        }
    }

}
