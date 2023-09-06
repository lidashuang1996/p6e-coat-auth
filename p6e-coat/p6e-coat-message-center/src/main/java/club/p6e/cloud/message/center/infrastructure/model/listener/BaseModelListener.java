package club.p6e.cloud.message.center.infrastructure.model.listener;

import club.p6e.cloud.console.infrastructure.model.*;
import club.p6e.cloud.message.center.infrastructure.model.ConfigModel;
import club.p6e.cloud.message.center.infrastructure.model.LauncherModel;
import com.darvi.hksi.badminton.lib.AuthCore;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class BaseModelListener {
    private static final String ID = "id";
    private static final String IS_DELETE = "isDelete";
    private static final String VERSION = "version";
    private static final String OPERATOR = "operator";
    private static final String CREATE_DATE = "createDate";
    private static final String UPDATE_DATE = "updateDate";

    /**
     * 注册使用改功能的模型
     */
    private static final Class<?>[] MS = new Class<?>[]{
            ConfigModel.class,
            LauncherModel.class,
            UserModel.class,
            UserAuthModel.class,
            PermissionUrlModel.class,
            PermissionUrlGroupModel.class,
            PermissionUrlGroupRelationUrlModel.class,
            Oauth2ClientModel.class
    };

    /**
     * 判断是否属于注册的模型
     *
     * @param target 模型对象
     * @return 判断是否属于注册的模型结果
     */
    private static boolean isModel(Object target) {
        if (target != null) {
            for (final Class<?> m : MS) {
                if (target.getClass() == m) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 新增操作之前
     *
     * @param target 源对象
     */
    @PrePersist
    public void prePersist(Object target) {
        if (isModel(target)) {
            final Field[] fields = target.getClass().getDeclaredFields();
            for (final Field field : fields) {
                try {
                    final String name = field.getName();
                    switch (name) {
                        case ID -> {
                            field.setAccessible(true);
                            field.set(target, null);
                        }
                        case CREATE_DATE, UPDATE_DATE -> {
                            field.setAccessible(true);
                            field.set(target, LocalDateTime.now());
                        }
                        case OPERATOR -> {
                            field.setAccessible(true);
                            final AuthCore auth = AuthCore.getThreadInstance();
                            field.set(target, String.valueOf(auth.getId()));
                        }
                        case VERSION, IS_DELETE -> {
                            field.setAccessible(true);
                            field.set(target, 0);
                        }
                        default -> {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 新增操作之后
     *
     * @param target 源对象
     */
    @PostPersist
    public void postPersist(Object target) {
    }

    /**
     * 更新操作之前
     *
     * @param target 源对象
     */
    @PreUpdate
    public void preUpdate(Object target) {
        if (isModel(target)) {
            final Field[] fields = target.getClass().getDeclaredFields();
            for (final Field field : fields) {
                try {
                    final String name = field.getName();
                    switch (name) {
                        case UPDATE_DATE -> {
                            field.setAccessible(true);
                            field.set(target, LocalDateTime.now());
                        }
                        case OPERATOR -> {
                            field.setAccessible(true);
                            final AuthCore auth = AuthCore.getThreadInstance();
                            field.set(target, String.valueOf(auth.getId()));
                        }
                        default -> {
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 更新操作之后
     *
     * @param target 源对象
     */
    @PostUpdate
    public void postUpdate(Object target) {
    }

    /**
     * 删除操作之前
     *
     * @param target 源对象
     */
    @PreRemove
    public void preRemove(Object target) {
    }

    /**
     * 删除操作之后
     *
     * @param target 源对象
     */
    @PostRemove
    public void postRemove(Object target) {
    }

}
