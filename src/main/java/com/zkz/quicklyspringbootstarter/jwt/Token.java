package com.zkz.quicklyspringbootstarter.jwt;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Token {
    private UUID id;
    private LocalDateTime expiredTime;
}
