package club.p6e.cloud.auth;

import club.p6e.cloud.auth.service.IndexService;
import club.p6e.cloud.auth.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lidashuang
 * @version 1.0.0
 */
//@EnableP6eGatewayAuth
@SpringBootApplication
public class P6eGatewayAuthApplication {

    public static void main(String[] args) {
        SpringUtil.init(
                SpringApplication.run(P6eGatewayAuthApplication.class, args)
        );
        SpringUtil.getBean(IndexService.class).setTemplateContent(
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "  <meta charset='UTF-8' />\n" +
                        "  <link rel='icon' type='image/svg+xml' href='/vite.svg' />\n" +
                        "  <meta name='viewport' content='width=device-width, initial-scale=1.0' />\n" +
                        "  <title>P6e | Auth</title>\n" +
                        "  <style>\n" +
                        "    body {\n" +
                        "      margin: 0;\n" +
                        "      padding: 0;\n" +
                        "      min-width: 100%;\n" +
                        "      min-height: 100vh;\n" +
                        "      background-color: #242e48;\n" +
                        "    }\n" +
                        "\n" +
                        "    .p-loader {\n" +
                        "      position: fixed;\n" +
                        "      top: 50%;\n" +
                        "      left: 50%;\n" +
                        "      transform: translate(-50%, -50%);\n" +
                        "    }\n" +
                        "\n" +
                        "    .p-loader-container {\n" +
                        "      padding: 32px;\n" +
                        "    }\n" +
                        "\n" +
                        "    .p-loader-content {\n" +
                        "      position: relative;\n" +
                        "      border: solid #41bce1 2px;\n" +
                        "      width: 180px;\n" +
                        "      height: 20px;\n" +
                        "      border-radius: 7px;\n" +
                        "    }\n" +
                        "\n" +
                        "    .p-loader-content:before {\n" +
                        "      content: \"\";\n" +
                        "      position: absolute;\n" +
                        "      width: 176px;\n" +
                        "      height: 16px;\n" +
                        "      top: 2px;\n" +
                        "      left: 2px;\n" +
                        "      background: linear-gradient(to top left, #4169E1, #1DA57A);\n" +
                        "      border-radius: 4px;\n" +
                        "      animation: loader7AnimationBefore 4s linear infinite\n" +
                        "    }\n" +
                        "\n" +
                        "    @keyframes loader7AnimationBefore {\n" +
                        "      0% {\n" +
                        "        width: 0\n" +
                        "      }\n" +
                        "      10% {\n" +
                        "        width: 0\n" +
                        "      }\n" +
                        "      90% {\n" +
                        "        width: 176px\n" +
                        "      }\n" +
                        "      100% {\n" +
                        "        width: 176px\n" +
                        "      }\n" +
                        "    }\n" +
                        "  </style>\n" +
                        "  <script src='/js/init.js' type='text/javascript'></script>\n" +
                        "  <script type='text/javascript'>\n" +
                        "    window.p6e.voucher = '@{voucher}';\n" +
                        "  </script>\n" +
                        "  <script type=\"module\" crossorigin src=\"/js/index-3a90435b.js\"></script>\n" +
                        "  <link rel=\"stylesheet\" href=\"/css/index-98e85107.css\">\n" +
                        "</head>\n" +
                        "<body class='def-theme'>\n" +
                        "<div id='app'>\n" +
                        "  <div class='p-loader'>\n" +
                        "    <div class='p-loader-container'>\n" +
                        "      <div class='p-loader-content'></div>\n" +
                        "    </div>\n" +
                        "  </div>\n" +
                        "</div>\n" +
                        "</body>\n" +
                        "</html>\n"
        );
    }

}
