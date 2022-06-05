package com.zkz.quicklyspringbootstarter.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 抽象类
 * 实现此抽象类的过滤器会被当做处理token的过滤处理器
 * 如果没有,则会默认使用{@link GeneralTokenAuthFilter}
 */
        public abstract class AbstractTokenAuthFilter {

    public abstract void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;
}
