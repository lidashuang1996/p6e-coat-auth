package club.p6e.cloud.test.domain.aggregate;

import club.p6e.cloud.test.Properties;
import club.p6e.cloud.test.domain.ConfigurationDomain;
import club.p6e.cloud.test.domain.keyvalue.FileUploadLogKeyValue;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author lidashuang
 * @version 1.0
 */
public class FileUploadLogAggregate extends ConfigurationDomain {

    private static LocalDateTime LOCAL_DATE_TIME = null;
    private static FileUploadLogKeyValue.Storage STORAGE = null;

    private static final long CACHE_MAX_INTERVAL = 3600;

    public static FileUploadLogKeyValue.Storage storage() {
        if (LOCAL_DATE_TIME == null || STORAGE == null) {
            init();
        } else {
            final LocalDateTime now = LocalDateTime.now();
            final Duration duration = Duration.between(now, LOCAL_DATE_TIME);
            if (duration.toSeconds() > CACHE_MAX_INTERVAL) {
                init();
            }
        }
        return STORAGE;
    }

    private static synchronized void init() {
        if (LOCAL_DATE_TIME == null || STORAGE == null) {
            load();
        } else {
            final LocalDateTime now = LocalDateTime.now();
            final Duration duration = Duration.between(now, LOCAL_DATE_TIME);
            if (duration.toSeconds() > CACHE_MAX_INTERVAL) {
                load();
            }
        }
    }

    private static void load() {
        final Properties properties = SpringUtil.getBean(Properties.class);
        final String path = properties.getFileUpload().getPath();
        final Model rootModel = new Model().setPath(path);
        compute(rootModel);
        LOCAL_DATE_TIME = LocalDateTime.now();
        STORAGE = new FileUploadLogKeyValue.Storage()
                .setFiles(rootModel.getFile())
                .setMemory(rootModel.getLength())
                .setFolders(rootModel.getFolder());
    }

    private static void compute(Model model) {
        final File f = new File(model.getPath());
        if (f.exists()) {
            final File[] fs = f.listFiles();
            if (fs != null && fs.length > 0) {
                for (final File item : fs) {
                    if (item.isFile()) {
                        model.setFile(model.getFile() + 1);
                        model.setLength(item.length());
                    } else if (item.isDirectory()) {
                        final Model dirModel = new Model().setPath(item.getAbsolutePath());
                        compute(dirModel);
                        model.setFile(model.getFile() + dirModel.getFile());
                        model.setLength(model.getLength() + dirModel.getLength());
                        model.setFolder(model.getFolder() + dirModel.getFolder() + 1);
                    }
                }
            } else {
                model.setLength(0L);
            }
        } else {
            model.setLength(-1L);
        }
    }

    @Data
    @Accessors(chain = true)
    private static class Model implements Serializable {
        private String path;
        private int file;
        private int folder;
        private long length;
    }

}
