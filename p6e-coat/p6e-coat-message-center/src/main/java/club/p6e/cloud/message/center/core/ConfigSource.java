package club.p6e.cloud.message.center.core;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface ConfigSource {

    public void execute(Launcher launcher, Map<String, String> content);

}
