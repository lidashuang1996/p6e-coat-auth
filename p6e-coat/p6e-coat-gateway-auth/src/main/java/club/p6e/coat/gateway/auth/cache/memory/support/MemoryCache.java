package club.p6e.coat.gateway.auth.cache.memory.support;

import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.support.ICache;
import club.p6e.coat.gateway.auth.utils.TemplateParser;

/**
 * @author lidashuang
 * @version 1.0
 */
public class MemoryCache implements ICache {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION =
            TemplateParser.execute(
                    "#{"
                            + " ${@{AUTH_PROPERTIES_PREFIX}.login.enable:false} "
                            + " && ${@{AUTH_PROPERTIES_PREFIX}.login.account-password.enable:false} "
                            + " && ${@{AUTH_PROPERTIES_PREFIX}.login.account-password.enable-transmission-encryption:false} "
                            + "}",
                    "AUTH_PROPERTIES_PREFIX", Properties.AUTH_PROPERTIES_PREFIX
            );

    @Override
    public String toType() {
        return "MEMORY_TYPE";
    }

}
