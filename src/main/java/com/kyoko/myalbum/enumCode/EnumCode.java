package com.kyoko.myalbum.enumCode;

/**
 * @author young
 * @create 2023/3/23 16:25
 * @Description
 */
public enum EnumCode {
    /**
     * 200请求成功
     */
    OK(200, "请求成功"),
    /**
     * 303登录失败
     */
    LOGIN_FAIL(303, "登录失败"),
    /**
     * 400请求参数出错
     */
    BAD_REQUEST(400, "请求参数出错"),
    /**
     * 401没有登录
     */
    UNAUTHORIZED(401, "登录失效"),
    /**
     * 403没有权限
     */
    FORBIDDEN(403, "没有权限"),
    /**
     * 410已被删除
     */
    GONE(410, "已被删除"),
    /**
     * 423已被锁定
     */
    LOCKED(423, "已被锁定"),
    /**
     * 500服务器出错
     */
    INTERNAL_SERVER_ERROR(500, "服务器出错"),
    /**
     * 600x 文件
     */
    FILE_IO_ERROR(6001, "文件读写出错"),
    FILE_NO_IMAGE(6002, "不支持的文件类型"),
    FILE_BAD_NAME(6003, "文件名包含非法字符"),
    FILE_FAIL_EXTRACT(6004, "提取图片元数据失败"),

    /**
     * 异常
     */
    EXCEPTION_ERROR(4001, "未知异常");

    private final Integer value;
    private final String text;

    EnumCode(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 获取value
     */
    public Integer getValue() {
        return this.value;
    }

    /**
     * 获取Text
     */
    public String getText() {
        return this.text;
    }
}
