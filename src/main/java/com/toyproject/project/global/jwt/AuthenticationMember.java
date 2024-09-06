package com.toyproject.project.global.jwt;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : member")
public @interface AuthenticationMember {
}
