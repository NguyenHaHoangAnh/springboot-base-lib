package com.example.lib.auth.util;

import com.example.lib.auth.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {
    public static UserDto getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getPrincipal() instanceof UserDto ? (UserDto)authentication.getPrincipal() : new UserDto();
        } else {
            return null;
        }
    }
}
