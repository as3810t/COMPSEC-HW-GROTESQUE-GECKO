package hu.grotesque_gecko.caffstore.utils;

public abstract class BusinessException extends RuntimeException {
    private final String errorCode;

    protected BusinessException(final String errorCode, final String reason) {
        super(reason);
        this.errorCode = errorCode;
    }

    protected BusinessException(final String errorCode, final String reason, final Throwable throwable) {
        super(reason, throwable);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
