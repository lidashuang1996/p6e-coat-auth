package club.p6e.coat.file.manage.domain.entity;

import club.p6e.coat.file.manage.Properties;
import club.p6e.coat.file.manage.infrastructure.error.GlobalExceptionContext;
import club.p6e.coat.file.manage.infrastructure.model.FileModel;
import club.p6e.coat.file.manage.infrastructure.model.FolderModel;
import club.p6e.coat.file.manage.infrastructure.repository.FileRepository;
import club.p6e.coat.file.manage.infrastructure.repository.FolderRepository;
import com.darvi.hksi.badminton.lib.AuthCore;
import com.darvi.hksi.badminton.lib.utils.FileUtil;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author lidashuang
 * @version 1.0
 */
public class FileEntity {

    private final FileModel model;

    /**
     * 创建数据
     *
     * @param model 模型对象
     * @return FolderEntity 对象
     */
    public static FileEntity create(Integer folderId, FileModel model) {
        // 如果创建的文件不是根部目录
        // 那么就要的对父目录进行验证
        if (folderId != 0) {
            final FolderRepository folderRepository = SpringUtil.getBean(FolderRepository.class);
            final Optional<FolderModel> folderModelOptional = folderRepository.findById(folderId);
            if (folderModelOptional.isEmpty()) {
                throw GlobalExceptionContext.executeUserAccountExistException(FileEntity.class, "");
            }
        }
        // 创建文件夹
        // 获取事务对象
        final AuthCore auth = AuthCore.getThreadInstance();
        final Properties properties = SpringUtil.getBean(Properties.class);
        final FileRepository fileRepository = SpringUtil.getBean(FileRepository.class);
        final EsFileRepository esFileRepository = SpringUtil.getBean(EsFileRepository.class);
        final File file = new File(FileUtil.composePath(properties.getUploadBasePath(), model.getSource()));
        final TransactionDefinition transactionDefinition = SpringUtil.getBean(TransactionDefinition.class);
        final JpaTransactionManager jpaTransactionManager = SpringUtil.getBean(JpaTransactionManager.class);
        final TransactionStatus transactionStatus = jpaTransactionManager.getTransaction(transactionDefinition);
        try {
            model.setId(null);
            model.setLock(0);
            model.setParent(folderId);
            model.setEdition(0);
            System.out.println(file);
            System.out.println(file.isFile());
            System.out.println(file.length());
            model.setMediaSize(FileUtil.obtainFileMediaSize(file));
            model.setMediaType(FileUtil.obtainFileMediaType(file));
            model.setPreview(null);
            model.setThumbnail(null);
            model.setVersion(0);
            model.setIsDelete(0);
            model.setAttachment(0);
            model.setOwner(String.valueOf(auth.getId()));
            model.setOperator(String.valueOf(auth.getId()));
            model.setCreateDate(LocalDateTime.now());
            model.setUpdateDate(LocalDateTime.now());
            // ES
            esFileRepository.create();
            // DB
            final FileEntity entity = new FileEntity(fileRepository.saveAndFlush(model));
            // 事务提交
            jpaTransactionManager.commit(transactionStatus);
            return entity;
        } catch (Exception e) {
            // 事务回滚
            jpaTransactionManager.rollback(transactionStatus);
            throw new RuntimeException(e);
        }
    }

    /**
     * 构造方法
     *
     * @param model 模型对象
     */
    private FileEntity(FileModel model) {
        this.model = model;
    }

    public FileModel getModel() {
        return model;
    }

}
