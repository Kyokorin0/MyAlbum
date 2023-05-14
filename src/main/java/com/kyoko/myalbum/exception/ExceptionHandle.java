package com.kyoko.myalbum.exception;

import com.kyoko.myalbum.enumCode.EnumCode;
import com.kyoko.myalbum.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author young
 * @create 2023/3/23 16:52
 * @Description 接受并处理controller层的异常，service层的异常需要抛出到controller层
 */
@RestControllerAdvice
public class ExceptionHandle {


    private final static Logger log = LoggerFactory.getLogger(ExceptionHandle.class);


    @ExceptionHandler(value = {Exception.class})
    public Object handle(Exception e) {


        if (e instanceof MyException myException) {
            return myException.getResult();
        }
        //处理接口参数缺失异常
        else if (e instanceof MissingServletRequestParameterException) {
            return Result.builder()
                    .code(EnumCode.BAD_REQUEST.getValue())
                    .msg("请求参数错误！")
                    .data(e.getMessage())
                    .build()
                    .toJson();
        }
        //数据冲突异常
        else if(e instanceof DataIntegrityViolationException){
            return Result.builder()
                    .code(EnumCode.INTERNAL_SERVER_ERROR.getValue())
                    .msg("数据读写冲突！")
                    .data(e.getMessage())
                    .build()
                    .toJson();
        }
        //错误的请求方式
        else if(e instanceof HttpRequestMethodNotSupportedException){
            return Result.builder()
                    .code(EnumCode.BAD_REQUEST.getValue())
                    .msg("错误的请求方式！")
                    .data(e.getMessage())
                    .build()
                    .toJson();
        }
        //其他异常
        else {
            //不配置jwt签名密钥报过这个
            log.info("系统异常！", e);
            return Result.builder()
                    .code(EnumCode.EXCEPTION_ERROR.getValue())
                    .msg("未知错误")
                    .data(e.getMessage())
                    .build().toJson();
            //ResultUtil.result(-1, "未知错误");
        }
    }
}
