export default {
  sign: {
    in: {
      apTitle: '密码登录',
      acTitle: '验证码登录',
      nav: {
        register: {
          text: '没有账号?',
          link: {
            text: '注册',
            url: '/register'
          }
        },
        forgetPassword: {
          link: {
            text: '忘记密码',
            url: '/forget_password'
          }
        }
      },
      agreement: {
        text: '登录即代表同意',
        userLink: {
          text: '《用户协议》',
          url: 'javascript:void(0);'
        },
        privacyLink: {
          text: '《用户隐私保护指引》',
          url: 'javascript:void(0);'
        }
      },
      other: {
        title: '其它第三方登录',
        list: [
          {
            icon: 'QqOutlined',
            iconStyle: {
              color: 'rgb(71, 184, 243)'
            },
            name: 'QQ',
            url: '/sign/other/qq'
          },
          {
            icon: 'WechatOutlined',
            iconStyle: {
              color: 'rgb(78, 178, 101)'
            },
            name: '微信',
            url: '/sign/other/wechat'
          }
        ]
      },
      confirm: {
        text: '您正在授权：',
        button: {
          text: '确认'
        }
      },
      ap: {
        account: {
          placeholder: '邮箱/手机号'
        },
        password: {
          placeholder: '密码'
        },
        button: {
          text: '登录'
        }
      },
      ac: {
        button: {
          text: '登录'
        },
        account: {
          placeholder: '邮箱/手机号'
        },
        code: {
          placeholder: '验证码',
          button: '获取验证码',
          resetButton: '重新获取'
        }
      },
      qc: {
        title: '打开「P6E」App',
        subtitle: '在「首页」左上角打开扫一扫',
        button: {
          title: '二维码已经过期，请刷新重试',
          text: '点击重新获取'
        }
      }
    },
    up: {
      title: '注册',
      code: {
        placeholder: '验证码',
        button: '获取验证码',
        resetButton: '重新获取'
      },
      account: {
        placeholder: '邮箱/手机号'
      },
      password: {
        placeholder: '密码'
      },
      confirmPassword: {
        placeholder: '确认密码'
      },
      button: {
        text: '注册'
      },
      result: {
        button: {
          text: '前往登录'
        },
        success: {
          text: '账号注册成功'
        }
      }
    }
  },
  me: {
    title: '基本信息',
    info: {
      id: 'ID',
      status: '状态',
      name: '名称',
      nickname: '别名',
      account: '账号',
      describe: '描述'
    },
    button: {
      text: '退出登录',
      modal: {
        title: '提示',
        content: '您确定退出登录吗？',
        button: {
          ok: {
            text: '确定'
          },
          cancel: {
            text: '返回'
          }
        }
      }
    }
  },
  forgetPassword: {
    title: '忘记密码',
    code: {
      text: '验证码',
      button: '获取验证码',
      resetButton: '重新获取'
    },
    account: '账号',
    password: '密码',
    confirmPassword: '确认密码',
    button: '确定',
    buttonStep: '下一步'
  },
  validator: {
    code: {
      error1: '验证码不能为空',
      error2: '验证码格式不正确'
    },
    account: {
      error1: '账号不能为空',
      error2: '账号格式不正确'
    },
    password: {
      error1: '密码不能为空',
      error2: '密码格式不正确'
    },
    contrast: {
      error1: '没有获取验证码',
      error2: '密码不能为空',
      error3: '输入的账号不一致',
      error4: '输入的密码不一致'
    }
  }
};
