package club.p6e.cloud.test.domain;

/**
 * @author lidashuang
 * @version 1.0
 */
public class ConfigurationDomain {

    protected static final String DEFAULT_LANGUAGE = "zh-Hans-HK";

    protected static int initPage(Integer page) {
        return page == null ? 1 : page < 0 ? 1 : page;
    }

    protected static int initSize(Integer size) {
        return size == null ? 16 : size < 0 ? 16 : size > 100 ? 100 : size;
    }

    protected static String initLanguage(String language) {
        return "";
    }

}
