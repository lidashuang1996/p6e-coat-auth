package club.p6e.coat.gateway.auth;

import club.p6e.cloud.auth.cache.AccountPasswordLoginSignatureCache;
import club.p6e.cloud.auth.error.GlobalExceptionContext;
import club.p6e.cloud.auth.utils.GeneratorUtil;
import club.p6e.cloud.auth.utils.JsonUtil;
import club.p6e.cloud.auth.utils.RsaUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 账号密码登录传输数据编码解码器的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = AuthAccountPasswordLoginTransmissionCodec.class,
        ignored = AuthAccountPasswordLoginTransmissionCodecDefaultImpl.class
)
@ConditionalOnExpression(AuthAccountPasswordLoginTransmissionCodec.CONDITIONAL_EXPRESSION)
public class AuthAccountPasswordLoginTransmissionCodecDefaultImpl implements AuthAccountPasswordLoginTransmissionCodec {

    /**
     * 账号密码登录的缓存对象
     */
    private final AccountPasswordLoginSignatureCache cache;

    /**
     * 构造方法初始化
     *
     * @param cache 账号密码登录的缓存对象
     */
    public AuthAccountPasswordLoginTransmissionCodecDefaultImpl(AccountPasswordLoginSignatureCache cache) {
        this.cache = cache;
    }

    /**
     * 通过 MARK 来初始化模型
     *
     * @param mark MARK
     * @return 模型对象
     */
    private Model initModel(String mark) {
        final Optional<String> optional = cache.get(mark);
        if (optional.isEmpty()) {
            throw GlobalExceptionContext.executeAccountPasswordLoginCodecException(
                    this.getClass(), "fun initModel(String mark).");
        } else {
            return JsonUtil.fromJson(optional.get(), Model.class);
        }
    }

    @Override
    public Model generate() {
        try {
            final RsaUtil.KeyModel keyModel = RsaUtil.generateKeyPair();
            final String mark = GeneratorUtil.uuid() + GeneratorUtil.random();
            final Model model = new Model()
                    .setMark(mark)
                    .setPublicKey(keyModel.getPublicKey())
                    .setPrivateKey(keyModel.getPrivateKey());
            cache.set(mark, JsonUtil.toJson(model));
            return model;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String encrypt(Model model, String content) {
        String mark = null;
        try {
            if (model == null) {
                throw GlobalExceptionContext.executeAccountPasswordLoginCodecException(
                        this.getClass(), "fun encrypt(Model model, String content).");
            } else {
                if (model.getMark() != null) {
                    final Model nm = initModel(model.getMark());
                    model.setPublicKey(nm.getPublicKey()).setPrivateKey(nm.getPrivateKey());
                }
                if (model.getPublicKey() != null) {
                    mark = model.getMark();
                    return RsaUtil.encryptByPublicKey(model.getPublicKey(), content);
                } else if (model.getPrivateKey() != null) {
                    mark = model.getMark();
                    return RsaUtil.encryptByPrivateKey(model.getPrivateKey(), content);
                } else {
                    throw GlobalExceptionContext.executeAccountPasswordLoginCodecException(
                            this.getClass(), "fun encrypt(Model model, String content).");
                }
            }
        } catch (Exception e) {
            throw GlobalExceptionContext.executeAccountPasswordLoginCodecException(
                    this.getClass(), "fun encrypt(Model model, String content).");
        } finally {
            if (mark != null) {
                cache.del(mark);
            }
        }
    }

    @Override
    public String decrypt(Model model, String content) {
        String mark = null;
        try {
            if (model == null) {
                throw GlobalExceptionContext.executeAccountPasswordLoginCodecException(
                        this.getClass(), "fun encrypt(Model model, String content).");
            } else {
                if (model.getMark() != null) {
                    final Model nm = initModel(model.getMark());
                    model.setPublicKey(nm.getPublicKey()).setPrivateKey(nm.getPrivateKey());
                }
                if (model.getPrivateKey() != null) {
                    mark = model.getMark();
                    return RsaUtil.decryptByPrivateKey(model.getPrivateKey(), content);
                } else {
                    throw GlobalExceptionContext.executeAccountPasswordLoginCodecException(
                            this.getClass(), "fun encrypt(Model model, String content).");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw GlobalExceptionContext.executeAccountPasswordLoginCodecException(
                    this.getClass(), "fun decrypt(Model model, String content).");
        } finally {
            if (mark != null) {
                cache.del(mark);
            }
        }
    }
}
