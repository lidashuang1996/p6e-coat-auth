package club.p6e.coat.file.file;

/**
 * 上传文件清除策略
 *
 * @author lidashuang
 * @version 1.0
 */
public interface UploadFileCleanStrategy {

    /**
     * 执行文件清除策略
     */
    public void execute();

    /**
     * 执行文件时间策略
     */
    public boolean time();

}
