package club.p6e.coat.gateway.auth;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface AuthOauth2Client<M extends AuthOauth2Client.Model> {

    public interface Model extends Serializable {
        public String id();
        public String password();
        public String serialize();
    }


    public M create(String content);

}
