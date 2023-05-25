package club.p6e.coat.gateway.auth.generator;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = Oauth2UserOpenIdGenerator.class,
//        ignored = Oauth2UserOpenIdGeneratorDefaultImpl.class
//)
//@ConditionalOnExpression(Oauth2UserOpenIdGenerator.CONDITIONAL_EXPRESSION)
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
