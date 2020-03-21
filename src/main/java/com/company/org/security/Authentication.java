package com.company.org.security;

import com.company.org.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class Authentication {

    // This could obviously be more elaborate, but made it simple for simple sake
    public void authenticate(String token) {

        if (StringUtils.isEmpty(token)) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "required token header missing");
        }
        else if (!"112233".equals(token)) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "access is not authorized for token: " + token);
        }
    }
}
