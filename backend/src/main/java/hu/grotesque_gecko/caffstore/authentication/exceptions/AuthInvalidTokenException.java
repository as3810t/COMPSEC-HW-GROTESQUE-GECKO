package hu.grotesque_gecko.caffstore.authentication.exceptions;

import hu.grotesque_gecko.caffstore.utils.BusinessException;

public class AuthInvalidTokenException extends BusinessException {
    public AuthInvalidTokenException() {
        super("AUTH_INVALID_TOKEN", "Invalid token");
    }
}
