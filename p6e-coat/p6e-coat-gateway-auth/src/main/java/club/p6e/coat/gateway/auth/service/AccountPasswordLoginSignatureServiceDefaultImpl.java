package club.p6e.coat.gateway.auth.service;

import club.p6e.cloud.auth.Properties;
import club.p6e.cloud.auth.context.LoginContext;
import club.p6e.cloud.auth.codec.AccountPasswordLoginTransmissionCodec;
import club.p6e.cloud.auth.error.GlobalExceptionContext;
import club.p6e.cloud.auth.voucher.VoucherConversation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

/**
 * 账号密码登录的密码签名服务的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = AccountPasswordLoginSignatureService.class,
        ignored = AccountPasswordLoginSignatureServiceDefaultImpl.class
)
@ConditionalOnExpression(AccountPasswordLoginSignatureService.CONDITIONAL_EXPRESSION)
public class AccountPasswordLoginSignatureServiceDefaultImpl implements AccountPasswordLoginSignatureService {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 凭证会话对象
     */
    private final VoucherConversation conversation;

    /**
     * 传输编码解码器
     */
    private final AccountPasswordLoginTransmissionCodec codec;

    /**
     * 构造方法初始化
     *
     * @param codec        传输编码解码器
     * @param properties   配置文件对象
     * @param conversation 凭证会话对象
     */
    public AccountPasswordLoginSignatureServiceDefaultImpl(
            Properties properties,
            VoucherConversation conversation,
            AccountPasswordLoginTransmissionCodec codec) {
        this.codec = codec;
        this.properties = properties;
        this.conversation = conversation;
    }

    @Override
    public LoginContext.AccountPasswordSignature.Dto execute(LoginContext.AccountPasswordSignature.Request param) {
        // 读取配置文件判断服务是否启动
        // 读取配置文件判断传输编码解码是否启动
        if (!properties.getLogin().isEnable()
                || !properties.getLogin().getAccountPassword().isEnable()
                || !properties.getLogin().getAccountPassword().isEnableTransmissionEncryption()) {
            throw GlobalExceptionContext.executeServiceNotEnableException(
                    this.getClass(), "fun execute(LoginContext.AccountPasswordSignature.Request param).");
        }
        final String voucher = param.getVoucher();
        final AccountPasswordLoginTransmissionCodec.Model model = codec.generate();
        Map<String, String> map = new HashMap<>(2);
        map.put(VoucherConversation.ACCOUNT_PASSWORD_LOGIN, String.valueOf(System.currentTimeMillis()));
        map.put(VoucherConversation.ACCOUNT_PASSWORD_CODEC_MARK, model.getMark());
        conversation.bind(voucher, map);
        return new LoginContext.AccountPasswordSignature.Dto().setContent(model.getPublicKey());
    }

}
