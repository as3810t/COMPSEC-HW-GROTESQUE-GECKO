package hu.grotesque_gecko.caffstore.caff.exceptions;

import hu.grotesque_gecko.caffstore.utils.BusinessException;

public class CAFFTitleCannotBeEmptyException extends BusinessException {
    public CAFFTitleCannotBeEmptyException() {
        super("CAFF_TITLE_CANNOT_BE_EMPTY", "The title of the CAFF cannot be empty");
    }
}
