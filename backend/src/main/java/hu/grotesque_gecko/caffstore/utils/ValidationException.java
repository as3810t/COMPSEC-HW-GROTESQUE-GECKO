package hu.grotesque_gecko.caffstore.utils;

public class ValidationException extends BusinessException {
    public ValidationException() {
        super("VALIDATION_ERROR", "Validation error");
    }
}
