package com.kyoko.myalbum.Exception;

import com.kyoko.myalbum.Enum.EnumCode;
import com.kyoko.myalbum.Result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
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


        if (e instanceof MyException) {
            MyException myException = (MyException) e;
            return myException.getResult();
        } else if (e instanceof AuthenticationException) {
            AuthenticationException authenticationException = (AuthenticationException) e;
            return Result.builder()
                    .code(EnumCode.LOGIN_FAIL.getValue())
                    .msg("用户名不存在！")
                    .data(authenticationException.getMessage())
                    .build()
                    .toJson();
                    //ResultUtil.result(EnumCode.LOGIN_FAIL.getValue(), authenticationException.getMessage());
        } else {
            //不配置jwt签名密钥报过这个
            log.info("系统异常！", e);
            return Result.builder()
                    .code(EnumCode.EXCPTION_ERROR.getValue())
                    .msg("未知错误")
                    .data(e)
                    .build().toJson();
                    //ResultUtil.result(-1, "未知错误");
        }
    }
}
