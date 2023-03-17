package club.p6e.coat.file.folder;

/**
 * 上传文件的本地存储路径服务
 *
 * @author lidashuang
 * @version 1.0
 */
public interface UploadFolderStorageLocationPathService {

    /**
     * 获取文件保存的路径
     *
     * @return 文件保存的路径
     */
    public String path();

}
