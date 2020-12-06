package hu.grotesque_gecko.caffstore.caff.exceptions;

import hu.grotesque_gecko.caffstore.utils.BusinessException;

public class CommentDoesNotExistException extends BusinessException {
    public CommentDoesNotExistException() {
        super("COMMENT_DOES_NOT_EXIST", "Comment does not exist");
    }
}
