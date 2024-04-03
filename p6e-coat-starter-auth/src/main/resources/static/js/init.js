if (!window.g) {
  window.g = {};
}
if (!window.p6e) {
  window.p6e = {};
}
// window.g
window.g.config = {};
window.g.config.debug = false;
window.g.config.environment = 'pro';
window.g.config.url = 'http://auth.p6e.club';
window.g.config.url = '';
// window.p6e
// window.p6e.mode
// PHONE 电话模式
// MAILBOX 邮箱模式
// ACCOUNT 账号模式
// PHONE_OR_MAILBOX 电话邮箱模式
window.p6e.mode = 'PHONE_OR_MAILBOX';
window.p6e.home = {};
window.p6e.home.page = '/me';
window.p6e.login = {};
window.p6e.login.ap = {};
window.p6e.login.ac = {};
window.p6e.login.qc = {};
window.p6e.login.reconfirm = {};
window.p6e.login.page = '/';
window.p6e.login.ap.rsa = false;
window.p6e.login.ac.countDownTime = 60;
window.p6e.login.qc.expireTime = 50000;
window.p6e.login.qc.maxRefreshCount = 5;
window.p6e.login.qc.parserIntervalTime = 3000;
window.p6e.login.qc.noticeMode = 'WEB_SOCKET';
window.p6e.login.qc.websocketUrl = 'ws://qc.p6e.club/ws';
window.p6e.login.qc.contentPrefix = 'http://qc.p6e.club/';
window.p6e.login.reconfirm.page = '/oauth2/reconfirm';
window.p6e.register = {};
window.p6e.register.countDownTime = 60;
window.p6e.forgot = {};
window.p6e.forgot.password = {};
window.p6e.forgot.password.countDownTime = 60;
window.p6e.validator = {
  code: (data) => {
    if (!data) {
      return '$t("validator.code.error1")';
    }
    if (data === '') {
      return '$t("validator.code.error1")';
    }
    if (!/^\d{6}$/.test(data)) {
      return '$t("validator.code.error2")';
    }
    return true;
  },
  account: (data) => {
    if (!data) {
      return '$t("validator.account.error1")';
    }
    if (data === '') {
      return '$t("validator.account.error1")';
    }
    if (!/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/.test(data) && !/^1[3-9]\d{9}$/.test(data)) {
      return '$t("validator.account.error2")';
    }
    return true;
  },
  password: (data) => {
    if (!data) {
      return '$t("validator.password.error1")';
    }
    if (data === '') {
      return '$t("validator.password.error1")';
    }
    return true;
  },
  passwordContrast: (data1, data2) => {
    if (!data1 || !data2) {
      return '$t("validator.contrast.error2")';
    }
    if (data1 !== data2) {
      return '$t("validator.contrast.error4")';
    }
    return true;
  },
  accountContrast: (data1, data2) => {
    if (!data1 || !data2) {
      return '$t("validator.contrast.error1")';
    }
    if (data1 !== data2) {
      return '$t("validator.contrast.error3")';
    }
    return true;
  }
};
