package club.p6e.coat.gateway.auth;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface AuthSignatureCodec {

    public String decoder(CharSequence rawPassword);

    public String encode(Map<String, String> rawPassword);

}
