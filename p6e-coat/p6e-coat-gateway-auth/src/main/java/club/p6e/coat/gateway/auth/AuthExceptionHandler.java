package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.error.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ControllerAdvice(basePackages = "club.p6e.coat.gateway.auth")
public class AuthExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        if (e instanceof final CustomException ce) {
            return ResponseEntity.status(ce.getCode()).body(ce.getContent());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
