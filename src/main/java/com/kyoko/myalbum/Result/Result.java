package com.kyoko.myalbum.Result;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * @author young
 * @create 2023/3/23 16:33
 * @Description 统一封装的返回对象
 */
//封装返回结果
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Result {
    private Integer code;
    private String msg;
    private Object data;
    private Integer total;

    public String toJson() {
        String json = new Gson().toJson(
                Result.builder()
                        .code(code)
                        .msg(msg)
                        .data(data)
                        .total(total)
                        .build()
        );
        return json;
    }
}
