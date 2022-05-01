package com.zkz.quicklyspringbootstarter.jwt;

import com.zkz.quicklyspringbootstarter.annotation.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
@Component
public class JwtInterceptor  implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        Class<?> clazz = handlerMethod.getBeanType();
        Authorization annotation = clazz.getAnnotation(Authorization.class);
        String token = request.getHeader("authorization");
        if(annotation == null) {
            Authorization authorization = method.getAnnotation(Authorization.class);
            if(authorization == null) {
                return true;
            }else {
                return tokenService.verifyJwtToken(token,handlerMethod);
            }
        }else {
            return tokenService.verifyJwtToken(token,handlerMethod);
        }

    }
}
