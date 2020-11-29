package hu.grotesque_gecko.caffstore.caff.exceptions;

import hu.grotesque_gecko.caffstore.utils.BusinessException;

public class CAFFTemporalErrorException extends BusinessException {
    public CAFFTemporalErrorException(Throwable t) {
        super("CAFF_TEMPORAL_ERROR", "Error parsing the caff. Try again later!", t);
    }
}
