package hu.grotesque_gecko.caffstore.user.exceptions;

import hu.grotesque_gecko.caffstore.utils.BusinessException;

public class UserAlreadyExistsException extends BusinessException {
    public UserAlreadyExistsException() {
        super("USER_ALREADY_EXISTS", "User already exists");
    }
}
