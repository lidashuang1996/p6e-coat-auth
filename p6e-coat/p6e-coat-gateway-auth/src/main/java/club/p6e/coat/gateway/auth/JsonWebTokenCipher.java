package club.p6e.coat.gateway.auth;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface JsonWebTokenCipher {

    /**
     * 获取 ACCESS TOKEN 密钥
     *
     * @return ACCESS TOKEN 密钥
     */
    public String getAccessTokenSecret();

    /**
     * 获取 REFRESH TOKEN 密钥
     *
     * @return REFRESH TOKEN 密钥
     */
    public String getRefreshTokenSecret();

}
