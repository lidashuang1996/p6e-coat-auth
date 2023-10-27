export default {
  sign: {
    in: {
      title: 'P6e Oauth2',
      apTitle: 'Account password login',
      acTitle: 'Verification code login',
      qcTitle1: 'Account login',
      qcTitle2: 'Scan code login',
      nav: {
        register: {
          title: 'Register',
          text: 'No account?',
          url: 'https://baidu.com'
        },
        forgetPassword: {
          title: 'Forgot password?',
          url: 'https://baidu.com'
        }
      },
      other: {
        title: 'Other login',
        list: [
          {
            icon: 'QqOutlined',
            iconStyle: {
              color: 'rgb(71, 184, 243)'
            },
            name: 'QQ',
            url: 'https://baidu.com'
          },
          {
            icon: 'WechatOutlined',
            iconStyle: {
              color: 'rgb(78, 178, 101)'
            },
            name: 'WeChat',
            url: 'https://baidu.com'
          }
        ]
      },
      ap: {
        account: 'Email/Mobile',
        password: 'Password',
        button: 'Login'
      },
      ac: {
        button: 'Login',
        account: 'Email/Mobile',
        code: {
          text: 'Code',
          button: 'Get code'
        }
      },
      qc: {
        button: 'Click get new'
      }
    }
  }
};
