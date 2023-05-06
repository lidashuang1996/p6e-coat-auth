package club.p6e.coat.gateway.auth.generator.impl;

import club.p6e.coat.gateway.auth.generator.AuthAccessTokenGenerator;
import club.p6e.coat.gateway.auth.generator.AuthRefreshTokenGenerator;
import club.p6e.coat.gateway.auth.utils.GeneratorUtil;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthTokenGeneratorImpl implements AuthAccessTokenGenerator, AuthRefreshTokenGenerator {

    @Override
    public String execute() {
        return GeneratorUtil.uuid() + GeneratorUtil.random(6, true, false);
    }

}
