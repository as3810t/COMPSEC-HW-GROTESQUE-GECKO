package hu.grotesque_gecko.caffstore.api;

import hu.grotesque_gecko.caffstore.authorization.exceptions.AuthorizationException;
import hu.grotesque_gecko.caffstore.utils.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Object> authorizationException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<Object, Object>> businessException(BusinessException e) {
        final Map<Object, Object> response = new HashMap<>();
        response.put("errorCode", e.getErrorCode());
        response.put("errorMessage", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
