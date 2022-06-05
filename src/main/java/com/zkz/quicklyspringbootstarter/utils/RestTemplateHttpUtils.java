package com.zkz.quicklyspringbootstarter.utils;

import com.google.gson.JsonObject;
import com.zkz.quicklyspringbootstarter.exception.BaseException;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class RestTemplateHttpUtils {

    /**
     * 网络请求工具
     *
     * @param <T>            请求参数类型
     * @param url            接口url
     * @param httpMethod     请求方式
     * @param requestBody    请求参数
     * @param requestHeaders 请求头
     * @return JSON类型返回
     */

    public static <T> JsonObject HttpRequest(String url, HttpMethod httpMethod, Map<String, T> requestBody, Map<String, String> requestHeaders) {
        if (StringUtils.isBlank(url)) {
            throw new BaseException("9003");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        JsonObject body = new JsonObject();
        for (Map.Entry<String, T> entry : requestBody.entrySet()) {
            body.addProperty(entry.getKey(), entry.getValue().toString());
        }
        for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
            httpHeaders.set(entry.getKey(), entry.getValue());
        }

        HttpEntity entity = new HttpEntity<>(body, httpHeaders);
        return HttpRequest(url, httpMethod, entity, JsonObject.class);
    }


    /**
     * 网络请求工具
     *
     * @param url           接口url
     * @param httpMethod    请求方式
     * @param requestEntity 请求设置
     * @param responseType  返回类型
     * @param <T>           返回类型
     * @return 返回网络请求接口数据
     */
    public static <T> T HttpRequest(String url, HttpMethod httpMethod, HttpEntity requestEntity, Class<T> responseType) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> response = restTemplate.exchange(url, httpMethod, requestEntity, responseType);
        if (200 != response.getStatusCodeValue() || null == response.getBody()) {
            throw new BaseException("0406");
        }
        return response.getBody();
    }
}
