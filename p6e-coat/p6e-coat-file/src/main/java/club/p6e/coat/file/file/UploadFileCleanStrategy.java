package club.p6e.coat.file.file;

import club.p6e.coat.file.Properties;

/**
 * 上传文件清除策略
 *
 * @author lidashuang
 * @version 1.0
 */
public interface UploadFileCleanStrategy {

    /**
     * 执行文件清除策略
     * 每间隔一个小时执行一次
     */
    public void execute();

}
