package club.p6e.coat.file.manage.domain.entity;

import club.p6e.coat.file.manage.infrastructure.error.GlobalExceptionContext;
import club.p6e.coat.file.manage.infrastructure.es.EsRepository;
import club.p6e.coat.file.manage.infrastructure.model.FolderModel;
import club.p6e.coat.file.manage.infrastructure.repository.FolderRepository;
import com.darvi.hksi.badminton.lib.AuthCore;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author lidashuang
 * @version 1.0
 */
public class FolderEntity {

    private final FolderModel model;

    /**
     * 创建数据
     *
     * @param model 模型对象
     * @return FolderEntity 对象
     */
    public static FolderEntity create(Integer folderId, FolderModel model) {
        // 如果创建的文件不是根部目录
        // 那么就要的对父目录进行验证
        if (folderId != 0) {
            final FolderRepository folderRepository = SpringUtil.getBean(FolderRepository.class);
            final Optional<FolderModel> folderModelOptional = folderRepository.findById(folderId);
            if (folderModelOptional.isEmpty()) {
                throw GlobalExceptionContext.executeUserAccountExistException(FolderEntity.class, "");
            }
        }
        // 创建文件夹
        // 获取事务对象
        final AuthCore auth = AuthCore.getThreadInstance();
        final FolderRepository FolderRepository = SpringUtil.getBean(FolderRepository.class);
        final EsRepository esFolderRepository = SpringUtil.getBean(EsRepository.class);
        final TransactionDefinition transactionDefinition = SpringUtil.getBean(TransactionDefinition.class);
        final JpaTransactionManager jpaTransactionManager = SpringUtil.getBean(JpaTransactionManager.class);
        final TransactionStatus transactionStatus = jpaTransactionManager.getTransaction(transactionDefinition);
        try {
            model.setId(null);
            model.setParent(folderId);
            model.setSize(0L);
            model.setIcon(null);
            model.setVersion(0);
            model.setIsDelete(0);
            model.setOwner(String.valueOf(auth.getId()));
            model.setOperator(String.valueOf(auth.getId()));
            model.setCreateDate(LocalDateTime.now());
            model.setUpdateDate(LocalDateTime.now());
            // ES
            esFolderRepository.create();
            // DB
            final FolderEntity entity = new FolderEntity(FolderRepository.saveAndFlush(model));
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
    private FolderEntity(FolderModel model) {
        this.model = model;
    }

    public FolderModel getModel() {
        return model;
    }

}
