package com.zkz.quicklyspringbootstarter.security;

import com.zkz.quicklyspringbootstarter.jwt.Token;
import com.zkz.quicklyspringbootstarter.jwt.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认的的token过滤器
 */
public class GeneralTokenAuthFilter extends AbstractTokenAuthFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取token,生成角色信息
        Token token = TokenUtils.getToken();
        List<GrantedAuthority> authorities;
        if (token == null || token.isVisitorToken()) {
            authorities = AuthorityUtils.NO_AUTHORITIES;
        } else {
            List<String> authorityList = getAuthorityList(token.getRoleCode());
            authorities = AuthorityUtils.createAuthorityList(authorityList.toArray(new String[0]));
        }

        //生成令牌
        Authentication authentication = new UsernamePasswordAuthenticationToken(null, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    /**
     * 根据用户角色生成对应的权限集合
     */
    private List<String> getAuthorityList(String roleCode) {
        List<String> authorityList = new ArrayList<>();

        // 如果没有任何一位包含1,则直接返回空数组
        int firstIndex = roleCode.indexOf('1');
        if (firstIndex < 0) {
            return authorityList;
        }

        // 准备一个全是0的初始角色编码
        String rawRoleString = StringUtils.repeat('0', roleCode.length());
        for (int i = firstIndex; i < roleCode.length(); i++) {
            StringBuilder stringBuilder = new StringBuilder(rawRoleString);
            stringBuilder.replace(i, i + 1, "1");
            authorityList.add(stringBuilder.toString());
        }
        return authorityList;
    }
}
