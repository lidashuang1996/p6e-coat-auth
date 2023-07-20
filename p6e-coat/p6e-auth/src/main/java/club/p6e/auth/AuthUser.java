package club.p6e.auth;

import club.p6e.auth.model.UserAuthModel;
import club.p6e.auth.model.UserModel;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface AuthUser<M extends AuthUser.Model> {

    public interface Model extends Serializable {
        public String id();
        public String password();
        public String serialize();
    }

    public M create(UserModel um, UserAuthModel uam);

    public M create(String content);

}
