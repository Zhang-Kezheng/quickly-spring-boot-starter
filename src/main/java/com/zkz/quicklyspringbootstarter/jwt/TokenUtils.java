package com.zkz.quicklyspringbootstarter.jwt;

import com.zkz.quicklyspringbootstarter.apiResponse.ApiResponse;
import com.zkz.quicklyspringbootstarter.apiResponse.ApiResponseUtils;
import com.zkz.quicklyspringbootstarter.log.LogUtils;
import com.zkz.quicklyspringbootstarter.utils.httprequest.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TokenUtils extends OncePerRequestFilter {
    // 用于存放token缓存
    private static final InheritableThreadLocal<Token> tokenCache = new InheritableThreadLocal<>();
    // 相关配置
    private static TokenSetting tokenSetting;

    @Autowired
    public TokenUtils(TokenSetting tokenSetting) {
        TokenUtils.tokenSetting = tokenSetting;
    }

    /**
     * 获取当前请求解密后的token.
     */
    public static Token getToken() {
        return tokenCache.get();
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestKey = tokenSetting.getRequestKey();
        String encodeToken = RequestUtils.getParam(request, requestKey);

        try {
            // 如果不能获取到有效token,则以游客token代替
            Token token = JwtUtils.verify(encodeToken).orElse(Token.getVisitorToken());
            // 放入缓存
            tokenCache.set(token);
            // 放行
            filterChain.doFilter(request, response);
            // 清除缓存,防止OOM
            tokenCache.remove();
        } catch (TokenException ex) {
            ApiResponse error = ApiResponseUtils.error(ex.getCode(), ex.getMessage());
            LogUtils.error(error.toString());
            RequestUtils.doResponse(response, error);
        }
    }
}
