package com.zkz.quicklyspringbootstarter.jwt;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class TokenService {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private JwtSetting jwtSetting;
    public  boolean verifyJwtToken(String token, HandlerMethod handlerMethod) {
        Optional<Token> verify = jwtUtils.verify(token);
        return false;
    }
}
