package club.p6e.cloud.message.center.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lidashuang
 * @version 1.0
 */
public class ConfigParserFactory implements ConfigParser {

    private final Map<String, ConfigParser> cache = new ConcurrentHashMap<>();

    public void register(String name, ConfigParser parser) {
        cache.put(name, parser);
    }

    public void unregister(String name) {
        cache.remove(name);
    }

    @Override
    public ConfigSource execute(Config config) {
        final ConfigParser configParser = cache.get(config.name());
        if (configParser == null) {
            return null;
        } else {
            return configParser.execute(config);
        }
    }

}
