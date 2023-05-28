package club.p6e.coat.gateway.auth.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public final class TemplateParser {

    /**
     * 标记开始的符号
     */
    private static final String START_CHAR = "@";

    /**
     * 内容结束的符号
     */
    private static final String CONTENT_END_CHAR = "}";

    /**
     * 内容开始的符号
     */
    private static final String CONTENT_START_CHAR = "{";

    /**
     * 内容默认值的符号
     */
    private static final String CONTENT_DEFAULT_VALUE_CHAR = ":";

    /**
     * 执行解析模板的内容
     *
     * @param content 模板的内容
     * @param data    模板的数据 KEY/VALUE
     * @return 模板解析后的内容
     */
    public static String execute(String content, Map<String, String> data) {
        return execute(content, data, false);
    }

    public static String execute(String content, String... data) {
        System.out.println(Arrays.toString(data));
        if (data.length % 2 != 0) {
            throw new RuntimeException();
        }
        final Map<String, String> map = new HashMap<>();
        for (int i = 0; i < data.length; i++) {
            map.put(data[i], data[++i]);
        }
        return execute(content, map, false);
    }

    /**
     * 执行解析模板的内容
     *
     * @param content                      模板的内容
     * @param data                         模板的数据 KEY/VALUE
     * @param isVariablesReplaceEmptyValue 是否用变量名称替换空的数据内容
     * @return 模板解析后的内容
     */
    public static String execute(String content, Map<String, String> data, boolean isVariablesReplaceEmptyValue) {
        if (content == null || content.isEmpty()) {
            return "";
        } else {
            StringBuilder key = null;
            final int length = content.length();
            final StringBuilder result = new StringBuilder();
            for (int i = 0; i < length; i++) {
                final String c = String.valueOf(content.charAt(i));
                if (i + 1 < length && START_CHAR.equals(c)
                        && CONTENT_START_CHAR.equals(String.valueOf(content.charAt(i + 1)))) {
                    if (key != null) {
                        result.append(START_CHAR).append(CONTENT_START_CHAR).append(key);
                    }
                    i++;
                    key = new StringBuilder();
                } else if (key != null) {
                    if (CONTENT_END_CHAR.equals(c)) {
                        final String keySource = key.toString();
                        key = null;
                        if (data != null) {
                            final String value = data.get(getKey(keySource));
                            if (value != null) {
                                result.append(value);
                                continue;
                            }
                        }
                        final String defaultValue = getDefaultValue(keySource);
                        if (defaultValue != null) {
                            result.append(defaultValue);
                            continue;
                        }
                        if (isVariablesReplaceEmptyValue) {
                            result.append(keySource);
                        } else {
                            result.append(START_CHAR)
                                    .append(CONTENT_START_CHAR)
                                    .append(keySource).append(CONTENT_END_CHAR);
                        }
                    } else {
                        key.append(c);
                    }
                } else {
                    result.append(c);
                }
            }
            if (key != null) {
                result.append(START_CHAR).append(CONTENT_START_CHAR).append(key);
            }
            return result.toString();
        }
    }

    /**
     * 获取内容里面的 KEY 的内容
     *
     * @param content 内容
     * @return KEY
     */
    private static String getKey(String content) {
        if (content != null && !content.isEmpty()) {
            final StringBuilder result = new StringBuilder();
            for (int i = 0; i < content.length(); i++) {
                final String ch = String.valueOf(content.charAt(i));
                if (CONTENT_DEFAULT_VALUE_CHAR.equals(ch)) {
                    break;
                } else {
                    result.append(content.charAt(i));
                }
            }
            return result.toString();
        }
        return "";
    }

    /**
     * 获取内容里面的默认值的内容
     *
     * @param content 内容
     * @return 默认值数据
     */
    private static String getDefaultValue(String content) {
        if (content != null && !content.isEmpty()) {
            final StringBuilder result = new StringBuilder();
            for (int i = content.length() - 1; i >= 0; i--) {
                final String ch = String.valueOf(content.charAt(i));
                if (CONTENT_DEFAULT_VALUE_CHAR.equals(ch)) {
                    return result.length() > 0 ? result.toString() : null;
                } else {
                    result.append(content.charAt(i));
                }
            }
        }
        return null;
    }
}
