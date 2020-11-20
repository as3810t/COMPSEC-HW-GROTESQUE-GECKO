package hu.grotesque_gecko.caffstore.utils;

import org.springframework.security.access.AuthorizationServiceException;

public class Preconditions {
    public static void checkPermission(boolean ...authorized) {
        for(boolean a : authorized) {
            if(a) {
                return;
            }
        }

        throw new AuthorizationServiceException("unauthorized");
    }
}
