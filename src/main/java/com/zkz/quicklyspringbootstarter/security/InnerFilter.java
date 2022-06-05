package com.zkz.quicklyspringbootstarter.security;

import com.zkz.quicklyspringbootstarter.log.LogUtils;
import com.zkz.quicklyspringbootstarter.utils.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
@Order(99)
public class InnerFilter extends GenericFilterBean {
    public InnerFilter() {
        LogUtils.info("启用接口安全验证");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获取所有AbstractTokenAuthFilter的实例
        Map<String, AbstractTokenAuthFilter> filterMap = SpringContextUtils.getBeansOfType(AbstractTokenAuthFilter.class);
        //找到实现了AbstractTokenAuthFilter的第一个bean.如果找不到,则使用默认的filter
        AbstractTokenAuthFilter filter = filterMap.values().stream()
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(new GeneralTokenAuthFilter());
        //执行方法
        filter.doFilter(request, response, chain);
    }
}
