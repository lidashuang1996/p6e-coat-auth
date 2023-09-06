package club.p6e.cloud.message.center.core;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Launcher {

    private ConfigParserFactory configParserFactory;
    private TemplateParserFactory templateParserFactory;
    private LauncherPatternParser launcherPatternParserFactory;

    public Launcher(ConfigParserFactory configParserFactory, TemplateParserFactory templateParserFactory, LauncherPatternParser launcherPatternParserFactory) {
        this.configParserFactory = configParserFactory;
        this.templateParserFactory = templateParserFactory;
        this.launcherPatternParserFactory = launcherPatternParserFactory;
    }

    public void execute(String id, Map<String, String> data) {

        la


        configParserFactory.execute();
        templateParserFactory.execute();
        launcherPatternParserFactory.execute();
    }

}
