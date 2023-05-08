package com.kyoko.myalbum.exception;

/**
 * @author young
 * @create 2023/3/23 16:51
 * @Description
 */
public class MyException extends RuntimeException{

    /**
     * 返回结果
     */
    private Object result;

    public MyException(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
