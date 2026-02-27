package com.example.lib.auth;

import com.example.lib.auth.config.AuthConfiguration;
import com.example.lib.auth.config.SecurityConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({AuthConfiguration.class, SecurityConfiguration.class})
public @interface EnableAuth {
}
