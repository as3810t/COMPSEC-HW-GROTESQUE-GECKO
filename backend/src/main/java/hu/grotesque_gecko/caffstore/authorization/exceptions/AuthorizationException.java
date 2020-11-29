package hu.grotesque_gecko.caffstore.authorization.exceptions;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(final String reason) {
        super(reason);
    }

    public AuthorizationException(final String reason, final Throwable throwable) {
        super(reason, throwable);
    }
}
