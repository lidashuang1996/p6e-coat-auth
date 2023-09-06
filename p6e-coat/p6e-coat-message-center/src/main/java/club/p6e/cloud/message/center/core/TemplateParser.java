package club.p6e.cloud.message.center.core;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface TemplateParser {

    public String execute(Template template, Map<String, String> params);

}
