package com.zkz.quicklyspringbootstarter.security;

import com.zkz.quicklyspringbootstarter.apiResponse.ApiResponse;
import com.zkz.quicklyspringbootstarter.apiResponse.ApiResponseUtils;
import com.zkz.quicklyspringbootstarter.exception.BaseException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ApiResponse error = ApiResponseUtils.error(new BaseException(authException.getMessage()));
        response.getWriter().write(error.toString());
    }
}
