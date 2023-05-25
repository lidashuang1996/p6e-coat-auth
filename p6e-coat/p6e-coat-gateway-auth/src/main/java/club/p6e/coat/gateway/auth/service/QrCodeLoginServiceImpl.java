//package club.p6e.coat.gateway.auth.service;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
///**
// * 二维码登录服务的默认实现
// *
// * @author lidashuang
// * @version 1.0
// */
//@Component
//@ConditionalOnMissingBean(
//        value = QrCodeLoginService.class,
//        ignored = QrCodeLoginServiceImpl.class
//)
//@ConditionalOnExpression(QrCodeLoginService.CONDITIONAL_EXPRESSION)
//public class QrCodeLoginServiceImpl implements QrCodeLoginService {
//
//    /**
//     * 配置文件对象
//     */
//    private final Properties properties;
//
//    /**
//     * 二维码缓存对象
//     */
//    private final QrCodeLoginCache cache;
//
//    /**
//     * 用户存储库
//     */
//    private final UserRepository repository;
//
//    /**
//     * 构造方法初始化
//     *
//     * @param cache      二维码缓存对象
//     * @param properties 配置文件对象
//     * @param repository 用户存储库
//     */
//    public QrCodeLoginServiceImpl(
//            Properties properties,
//            QrCodeLoginCache cache,
//            UserRepository repository) {
//        this.cache = cache;
//        this.properties = properties;
//        this.repository = repository;
//    }
//
//    @Override
//    public LoginContext.QrCode.Dto execute(LoginContext.QrCode.Request param) {
//        // 读取配置文件判断服务是否启动
//        if (!properties.getLogin().isEnable()
//                || !properties.getLogin().getQrCode().isEnable()) {
//            throw GlobalExceptionContext.executeServiceNotEnableException(
//                    this.getClass(), "fun execute(LoginContext.QRCode.Request param).");
//        }
//        final String qrCode = param.getVoucher() == null ?
//                null : param.getVoucherMap().get(VoucherConversation.QR_CODE_VALUE);
//        if (qrCode == null) {
//            throw GlobalExceptionContext.executeVoucherException(
//                    this.getClass(), "fun execute(LoginContext.QRCode.Request param).");
//        } else {
//            final Optional<String> cacheOptional = cache.get(qrCode);
//            if (cacheOptional.isEmpty()) {
//                throw GlobalExceptionContext.executeQrCodeLoginException(
//                        this.getClass(), "fun execute(LoginContext.QRCode.Request param).");
//            } else {
//                final String content = cacheOptional.get();
//                if (QrCodeLoginCache.isEmpty(content)) {
//                    return null;
//                } else {
//                    try {
//                        final Optional<UserModel> userOptional = repository.findById(Integer.valueOf(content));
//                        if (userOptional.isEmpty()) {
//                            throw GlobalExceptionContext.executeUserNotExistException(
//                                    this.getClass(), "fun execute(LoginContext.QRCode.Request param).");
//                        } else {
//                            return CopyUtil.run(userOptional.get(), LoginContext.QrCode.Dto.class);
//                        }
//                    } finally {
//                        cache.del(qrCode);
//                    }
//                }
//            }
//        }
//    }
//}
