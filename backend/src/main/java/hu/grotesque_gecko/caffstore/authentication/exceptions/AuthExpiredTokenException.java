package hu.grotesque_gecko.caffstore.authentication.exceptions;

import hu.grotesque_gecko.caffstore.utils.BusinessException;

public class AuthExpiredTokenException extends BusinessException {
    public AuthExpiredTokenException() {
        super("AUTH_EXPIRED_TOKEN", "Expired token");
    }
}
