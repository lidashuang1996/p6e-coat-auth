package club.p6e.auth.generator;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Oauth2UserOpenIdGeneratorDefaultImpl implements Oauth2UserOpenIdGenerator {

    private static final String SEASONING = "C6xfd#lKCF!6$IQB1NQZo&%m1jenVc3p";

    @Override
    public String execute(String clientId, String userId) {
        final String c = DigestUtils.md5DigestAsHex(clientId.getBytes(StandardCharsets.UTF_8));
        final int i = c.charAt(0) % 8;
        return DigestUtils.md5DigestAsHex(
                (c.substring(i)
                        + userId
                        + SEASONING
                        + DigestUtils.md5DigestAsHex(
                        c.substring(0, i).getBytes(StandardCharsets.UTF_8))
                ).getBytes(StandardCharsets.UTF_8)
        );
    }

}
