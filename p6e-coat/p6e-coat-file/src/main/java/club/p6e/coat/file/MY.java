package club.p6e.coat.file;

import club.p6e.coat.file.file.UploadFileCleanStrategyService;
import club.p6e.coat.file.file.UploadFileCleanTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@Configuration
public class MY {

    @Bean
    public UploadFileCleanTask task(UploadFileCleanStrategyService service) {
        return new UploadFileCleanTask(service);
    }

}
