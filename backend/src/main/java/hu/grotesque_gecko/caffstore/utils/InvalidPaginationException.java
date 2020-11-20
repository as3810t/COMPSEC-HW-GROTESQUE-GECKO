package hu.grotesque_gecko.caffstore.utils;

public class InvalidPaginationException extends BusinessException {
    public InvalidPaginationException() {
        super("INVALID_PAGINATION", "Invalid pagination request");
    }
}
