package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String msg;
    private Integer code;
    private Object data;

    public static Result success(Object data) {
        return new Result("操作成功", 20000, data);
    }
}
