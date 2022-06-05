package com.zkz.quicklyspringbootstarter.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.gson.Gson;
import com.zkz.quicklyspringbootstarter.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtUtils {
    // jwt字符中存放自定义数据的key
    private static final String JWT_CLAIM_DATE_KEY = "data";

    private static JwtSetting jwtSetting;

    @Autowired
    public void setJwtProperties(JwtSetting jwtSetting) {
        JwtUtils.jwtSetting = jwtSetting;
    }

    /**
     * 生成jwt
     *
     * @param token 自定义的数据
     * @return jwt字符串.参数token为null或发生异常时返回null.
     */
    public static String create(Token token) {
        if (token == null) {
            return null;
        }

        //签发时间
        Date nowTime = new Date();

        //header
        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        //创建builder
        JWTCreator.Builder builder = JWT.create()
                .withHeader(header)
                .withClaim(JWT_CLAIM_DATE_KEY, new Gson().toJson(token))
                .withIssuedAt(nowTime);

        //过期时间
        if (token.getExpiredTime() != null) {
            builder.withExpiresAt(DateUtils.toDate(token.getExpiredTime()));
        } else {
            if (jwtSetting.getSurvivalMinutes() > 0) {
                Date expiresTime = DateUtils.addMinutes(nowTime, jwtSetting.getSurvivalMinutes());
                builder.withExpiresAt(expiresTime);
            }
        }
        return builder.sign(getAlgorithm());
    }

    /**
     * 解析/验证jwt
     *
     * @param jwtStr jwt字符串
     * @return {@link Token}. 如果字符串为null或空,或者jwt中携带信息的字段没有值,则返回null
     */
    static Optional<Token> verify(String jwtStr) throws TokenException {
        if (StringUtils.isBlank(jwtStr) || StringUtils.equalsIgnoreCase(jwtStr, "null")) {
            return Optional.empty();
        }

        try {
            String jsonToken = JWT.require(getAlgorithm()).build()
                    .verify(jwtStr)
                    .getClaim(JWT_CLAIM_DATE_KEY)
                    .as(String.class);
            Token token = new Gson().fromJson(jsonToken, Token.class);
            return Optional.of(token);
        } catch (TokenExpiredException e) {//token过期
            throw new TokenException("0301");
        } catch (JWTVerificationException e) {//其他token验证异常
            throw new TokenException("0302");
        } catch (Exception e) {
            throw new TokenException("0303");
        }
    }

    /**
     * 获取算法
     */
    private static Algorithm getAlgorithm() throws TokenException {
        String secretKey = jwtSetting.getSecretKey();
        if (StringUtils.isBlank(secretKey)) {
            throw new TokenException("0304");
        }
        return Algorithm.HMAC256(secretKey);
    }
}
