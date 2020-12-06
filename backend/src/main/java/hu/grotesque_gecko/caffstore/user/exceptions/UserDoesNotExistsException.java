package hu.grotesque_gecko.caffstore.user.exceptions;

import hu.grotesque_gecko.caffstore.utils.BusinessException;

public class UserDoesNotExistsException extends BusinessException {
    public UserDoesNotExistsException() {
        super("USER_DOES_NOT_EXISTS", "User does not exists");
    }
}
