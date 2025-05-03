package com.perfexiolegal.billingsystem.Model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {
    private String message;
    private boolean success;
    private Object data;
} 