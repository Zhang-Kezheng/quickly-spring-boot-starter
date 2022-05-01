package com.zkz.quicklyspringbootstarter.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.gson.Gson;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.zkz.quicklyspringbootstarter.exception.TokenException;
import com.zkz.quicklyspringbootstarter.redis.RedisService;
import com.zkz.quicklyspringbootstarter.utils.DateUtils;
import com.zkz.quicklyspringbootstarter.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class JwtUtils  {
    private static final String JWT_CLAIM_DATE_KEY = "data";
    private static JwtSetting jwtSetting;
    @Resource
    private RedisService redisService;
    @Autowired
    public void setJwtProperties(JwtSetting jwtSetting) {
        JwtUtils.jwtSetting = jwtSetting;
    }

    public void create(Token token) {
        if (token != null) {
            Date nowTime = new Date();
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");
            JWTCreator.Builder builder = JWT.create().withHeader(header).withClaim(JWT_CLAIM_DATE_KEY, (new Gson()).toJson(token)).withIssuedAt(nowTime);
            if (token.getExpiredTime() != null) {
                builder.withExpiresAt(DateUtils.toDate(token.getExpiredTime()));
            } else if (jwtSetting.getSurvivalMinutes() > 0) {
                Date expiresTime = com.zkz.quicklyspringbootstarter.utils.DateUtils.addMinutes(nowTime, jwtSetting.getSurvivalMinutes());
                builder.withExpiresAt(expiresTime);
            }

            String sign = builder.sign(getAlgorithm());
            redisService.set(token.getId().toString(), sign, jwtSetting.getSurvivalMinutes());
        }
    }

    public  Optional<Token> verify(String jwtStr) throws TokenException {
        if (!StringUtils.isEmpty(jwtStr) && !StringUtils.equalsIgnoreCase(jwtStr, "null")) {
            try {
                String jsonToken = JWT.require(getAlgorithm()).build().verify(jwtStr).getClaim(JWT_CLAIM_DATE_KEY).as(String.class);
                Token token = (Token) new Gson().fromJson(jsonToken,Class.forName(jwtSetting.getTokenClassName()));
                return Optional.of(token);
            } catch (TokenExpiredException var3) {
                throw new TokenException("0301");
            } catch (JWTVerificationException var4) {
                throw new TokenException("0302");
            } catch (Exception var5) {
                throw new TokenException("0303");
            }
        } else {
            return Optional.empty();
        }
    }

    private static Algorithm getAlgorithm() throws TokenException {
        String secretKey = jwtSetting.getSecretKey();
        if (StringUtils.isEmpty(secretKey)) {
            throw new TokenException("0304");
        } else {
            return Algorithm.HMAC256(secretKey);
        }
    }
}
