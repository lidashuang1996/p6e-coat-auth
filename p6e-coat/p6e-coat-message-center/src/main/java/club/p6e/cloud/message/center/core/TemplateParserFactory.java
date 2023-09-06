package club.p6e.cloud.message.center.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lidashuang
 * @version 1.0
 */
public class TemplateParserFactory implements TemplateParser {

    private final Map<String, TemplateParser> cache = new ConcurrentHashMap<>();

    public void register(String name, TemplateParser parser) {
        cache.put(name, parser);
    }

    public void unregister(String name) {
        cache.remove(name);
    }

    @Override
    public String execute(Template template, Map<String, String> params) {
        final TemplateParser templateParser = cache.get(template.name());
        if (templateParser == null) {
            return null;
        } else {
            return templateParser.execute(template, params);
        }
    }
}
