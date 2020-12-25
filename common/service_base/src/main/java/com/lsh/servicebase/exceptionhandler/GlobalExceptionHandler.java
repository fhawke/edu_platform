package com.lsh.servicebase.exceptionhandler;

import com.lsh.commonutils.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        e.printStackTrace();
        return R.error();
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().message("执行了自定义异常");
    }

    @ExceptionHandler(EduException.class)
    @ResponseBody
    public R error(EduException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().message(e.getMsg()).code(e.getCode());
    }
}
