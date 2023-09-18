package club.p6e.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AutoConfiguration implements Configuration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoConfiguration.class);

    @Override
    public void execute(Properties properties) {
        LOGGER.info("Auto Auth >>> " + properties);
    }

}
