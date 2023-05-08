package com.kyoko.myalbum.Outdated;

import org.json.JSONObject;

/**
 * @author young
 * @create 2023/3/23 16:33
 * @Description 最开始使用的统一封装对象，适配gson后由com.kyoko.myalbum.Result.Result.class代替
 */
//封装返回结果
public class ResultUtil {
    public static String result(final Integer status,final String msg,final Object data,final Integer total) {
        JSONObject jsonObject = new JSONObject() {
            {
                put("status", status);
                put("msg", msg);
                put("data", data);
                put("total", total);
            }
        };
        return jsonObject.toString();
    }

    public static String result(final Integer status,final String msg,final Object data) {
        JSONObject jsonObject = new JSONObject() {
            {
                put("status", status);
                put("msg", msg);
                put("data", data);
            }
        };
        return jsonObject.toString();
    }

    public static String result(final Integer status,final String msg) {
        JSONObject jsonObject = new JSONObject() {
            {
                put("status", status);
                put("msg", msg);
            }
        };
        return jsonObject.toString();
    }
}
