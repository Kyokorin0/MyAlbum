package com.kyoko.myalbum.auth;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @author young
 * @create 2023/5/8 20:10
 * @Description
 */
@Service
public class EmailValidator implements Predicate<String> {
    //验证email格式的正则表达式
    @Override
    public boolean test(String s) {
        if ((s != null) && (!s.isEmpty())) {
            return Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", s);
        }
        return false;
    }
}
