package hu.grotesque_gecko.caffstore.caff.exceptions;

import hu.grotesque_gecko.caffstore.utils.BusinessException;

public class CAFFDoesNotExistsException extends BusinessException {
    public CAFFDoesNotExistsException() {
        super("CAFF_DOES_NOT_EXISTS", "CAFF does not exists");
    }
}
