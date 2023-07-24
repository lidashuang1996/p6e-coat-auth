if (!window.g) {
  window.g = {};
}
if (!window.p6e) {
  window.p6e = {};
}

// 全局的配置
window.g.config = {
  // 网络请求的基础路径
  // url: 'http://auth.p6e.club',
  // 项目当前是否为 debug 状态
  debug: false,
  // 项目当前的环境
  environment: 'pro'
};

// p6e 模式
// PHONE 电话模式
// MAILBOX 邮箱模式
// ACCOUNT 账号模式
// PHONE_OR_MAILBOX 电话邮箱模式
window.p6e.mode = 'PHONE_OR_MAILBOX';
// p6e 凭证
window.p6e.voucher = '9cf19d44b3034356bed9104d0f09e440';
// p6e 主页
window.p6e.home = {
  page: 'http://auth.p6e.club/me'
};
// p6e 登录页
window.p6e.login = {
  page: 'http://auth.p6e.club/login',
  ap: {
    rsa: false
  },
  ac: {
    // 倒计时时长
    countDownTimeout: 60
  },
  qc: {
    // 最大刷新次数
    maxRefreshCount: 3,
    // 过期时间
    expirationTime: 180000,
    // 解析间隔时间
    parsingIntervalTime: 2000,
    // 内容前缀
    contentPrefix: 'http://auth.p6e.club?qr_code='
  }
};
// p6e 验证器
window.p6e.validator = {
  code: (data) => {
    if (!data) {
      return '$t("sign.validator.code.error1")';
    }
    if (data === '') {
      return '$t("sign.validator.code.error1")';
    }
    if (!/^\d{6}$/.test(data)) {
      return '$t("sign.validator.code.error2")';
    }
    return true;
  },
  account: (data) => {
    if (!data) {
      return '$t("sign.validator.account.error1")';
    }
    if (data === '') {
      return '$t("sign.validator.account.error1")';
    }
    if (!/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/.test(data) && !/^1[3-9]\d{9}$/.test(data)) {
      return '$t("sign.validator.account.error2")';
    }
    return true;
  },
  password: (data) => {
    if (!data) {
      return '$t("sign.validator.password.error1")';
    }
    if (data === '') {
      return '$t("sign.validator.password.error1")';
    }
    if (!/^[*]{6,24}$/.test(data)) {
      return '$t("sign.validator.password.error2")';
    }
    return true;
  },
  contrast: (data1, data2) => {
    if (!data1 || !data2) {
      return '$t("sign.validator.contrast.error1")';
    }
    if (data1 !== data2) {
      return '$t("sign.validator.contrast.error2")';
    }
    return true;
  }
};

