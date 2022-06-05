package com.zkz.quicklyspringbootstarter.utils.httprequest;

import com.google.gson.Gson;
import com.zkz.quicklyspringbootstarter.apiResponse.ApiResponse;
import com.zkz.quicklyspringbootstarter.exception.MiddlewareException;
import com.zkz.quicklyspringbootstarter.log.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class RequestUtils {
    private static HttpServletRequest httpServletRequest;

    @Autowired
    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        RequestUtils.httpServletRequest = httpServletRequest;
    }

    /**
     * 获取当前请求
     */
    public static HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    /**
     * 获取当前响应
     */
    public static HttpServletResponse getHttpServletResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new MiddlewareException("0105");
        }
        return attributes.getResponse();
    }

    /**
     * 获取完整的url
     */
    public static String getRequestUrl() {
        HttpServletRequest request = getHttpServletRequest();
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (StringUtils.isBlank(queryString)) {
            return requestURL;
        } else {
            return String.format("%s?%s", requestURL, queryString);
        }
    }

    /**
     * 获取请求中携带参数
     *
     * @param key 参数名
     * @return 请求中携带的参数, 找不到返回null
     */
    public static String getParam(String key) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        return getParam(httpServletRequest, key);
    }

    /**
     * 获取请求中携带的参数
     *
     * @param httpServletRequest 请求
     * @param key                参数名
     * @return 请求中携带的参数, 找不到返回null
     */
    public static String getParam(HttpServletRequest httpServletRequest, String key) {
        String value = httpServletRequest.getHeader(key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }

        // 请求头中找不到时,从cookie中获取
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies != null) {
            value = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(key))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        if (StringUtils.isNotBlank(value)) {
            return value;
        }

        // 请求头中找不到时,从请求参数中获取
        value = httpServletRequest.getParameter(key);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }

        //无论如何,返回
        return null;
    }

    /**
     * 获取客户端ip
     */
    public static String getIp() {
        HttpServletRequest request = getHttpServletRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 响应请求
     *
     * @param response    ServletResponse
     * @param apiResponse 响应数据实体
     */
    public static void doResponse(ServletResponse response, ApiResponse apiResponse) {
        response.setContentType("application/json;charset=utf-8");
        try {
            response.getWriter().write(new Gson().toJson(apiResponse));
        } catch (IOException e) {
            LogUtils.error(e.getLocalizedMessage());
        }
    }
}
