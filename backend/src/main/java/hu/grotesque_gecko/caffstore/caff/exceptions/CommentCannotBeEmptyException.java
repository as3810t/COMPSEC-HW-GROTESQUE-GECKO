package hu.grotesque_gecko.caffstore.caff.exceptions;

import hu.grotesque_gecko.caffstore.utils.BusinessException;

public class CommentCannotBeEmptyException extends BusinessException {
    public CommentCannotBeEmptyException() {
        super("COMMENT_CANNOT_BE_EMPTY", "Comment cannot be empty");
    }
}
