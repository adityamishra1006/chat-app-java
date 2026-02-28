package com.example.auth.dto;

import com.example.auth.common.ErrorCode;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private ErrorCode errorCode;
    private LocalDateTime timeStamp;
}
