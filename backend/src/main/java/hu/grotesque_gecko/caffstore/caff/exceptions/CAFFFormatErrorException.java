package hu.grotesque_gecko.caffstore.caff.exceptions;

import hu.grotesque_gecko.caffstore.utils.BusinessException;

public class CAFFFormatErrorException extends BusinessException {
    public CAFFFormatErrorException(String error) {
        super("CAFF_FORMAT_ERROR", error);
    }
}
