package club.p6e.cloud.auth.generator;

import club.p6e.cloud.auth.utils.GeneratorUtil;
import org.springframework.util.DigestUtils;

/**
 * 实现认证令牌
 *
 * @author lidashuang
 * @version 1.0
 */
public class AuthTokenGeneratorImpl implements
        AuthAccessTokenGenerator,
        AuthRefreshTokenGenerator,
        Oauth2TokenClientAuthAccessTokenGenerator,
        Oauth2TokenClientAuthRefreshTokenGenerator,
        Oauth2TokenUserAuthAccessTokenGenerator,
        Oauth2TokenUserAuthRefreshTokenGenerator {

    @Override
    public String execute() {
        return GeneratorUtil.uuid() + DigestUtils.md5DigestAsHex(
                (GeneratorUtil.uuid() + GeneratorUtil.random(6, true, false)).getBytes()
        );
    }

}
