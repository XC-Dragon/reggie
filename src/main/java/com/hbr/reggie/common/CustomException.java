package com.hbr.reggie.common;

/**
 * @ClassName CustomException
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/9 21:17
 * @Version 1.0
 */
public class CustomException extends RuntimeException{
    public CustomException(String msg) {
        super(msg);
    }
}
