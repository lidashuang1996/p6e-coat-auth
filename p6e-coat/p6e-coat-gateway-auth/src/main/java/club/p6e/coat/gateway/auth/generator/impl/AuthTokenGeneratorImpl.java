package club.p6e.coat.gateway.auth.generator.impl;

import club.p6e.coat.gateway.auth.generator.*;
import club.p6e.coat.gateway.auth.utils.GeneratorUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * 实现认证令牌
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = {
                AuthAccessTokenGenerator.class,
                AuthRefreshTokenGenerator.class
        },
        ignored = AuthTokenGeneratorImpl.class
)
public class AuthTokenGeneratorImpl implements AuthAccessTokenGenerator, AuthRefreshTokenGenerator,
        Oauth2TokenClientAuthAccessTokenGenerator, Oauth2TokenClientAuthRefreshTokenGenerator,
        Oauth2TokenUserAuthAccessTokenGenerator, Oauth2TokenUserAuthRefreshTokenGenerator {

    @Override
    public String execute() {
        return GeneratorUtil.uuid() + DigestUtils.md5DigestAsHex(
                (GeneratorUtil.uuid() + GeneratorUtil.random(6, true, false)).getBytes()
        );
    }

}
