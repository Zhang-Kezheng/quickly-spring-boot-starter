package com.zkz.quicklyspringbootstarter.jwt;

import com.zkz.quicklyspringbootstarter.security.Role;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Token implements Serializable {
    private static long VISITOR_TOKEN_USER_ID = 0;
    private static String VISITOR_TOKEN_USER_NAME = "visitor";


    private long userId;
    private String userName;
    private String roleCode;
    private String client;
    private LocalDateTime createTime;
    private LocalDateTime expiredTime;


    /**
     * 生成游客token
     */
    static Token getVisitorToken() {
        Token token = new Token();
        token.setUserId(VISITOR_TOKEN_USER_ID);
        token.setUserName(VISITOR_TOKEN_USER_NAME);
        token.setRoleCode(Role.VISITOR);
        token.setClient(null); // todo
        token.setCreateTime(LocalDateTime.now());
        token.setExpiredTime(null);
        return token;
    }

    public boolean isVisitorToken() {
        return this.userId == VISITOR_TOKEN_USER_ID;
    }
}
