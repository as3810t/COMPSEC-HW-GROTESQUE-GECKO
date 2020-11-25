package hu.grotesque_gecko.caffstore.utils;

import hu.grotesque_gecko.caffstore.authorization.exceptions.AuthorizationException;
import lombok.SneakyThrows;

public class Preconditions {
    private Preconditions() {}

    public static void checkPermission(boolean ...authorized) {
        for(boolean a : authorized) {
            if(a) {
                return;
            }
        }

        throw new AuthorizationException("unauthorized");
    }

    @SneakyThrows
    public static void checkParameter(boolean valid, Class<? extends BusinessException> error) {
        if(!valid) {
            throw error.newInstance();
        }
    }

    @SneakyThrows
    public static void checkParameter(boolean valid1, boolean valid2, Class<? extends BusinessException> error) {
        if(!valid1 || !valid2) {
            throw error.newInstance();
        }
    }

    public static void checkPagination(int offset, int pageSize) {
        if(offset < 0 || pageSize < 1) {
            throw new InvalidPaginationException();
        }
    }
}
