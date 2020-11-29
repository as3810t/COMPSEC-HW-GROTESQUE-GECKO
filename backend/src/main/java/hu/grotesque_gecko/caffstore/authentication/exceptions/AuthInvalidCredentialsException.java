package hu.grotesque_gecko.caffstore.authentication.exceptions;

import hu.grotesque_gecko.caffstore.utils.BusinessException;

public class AuthInvalidCredentialsException extends BusinessException {
    public AuthInvalidCredentialsException() {
        super("AUTH_INVALID_CREDENTIALS", "Invalid credentials");
    }
}
