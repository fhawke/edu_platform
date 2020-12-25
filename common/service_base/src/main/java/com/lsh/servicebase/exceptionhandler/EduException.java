package com.lsh.servicebase.exceptionhandler;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EduException extends RuntimeException{
    @ApiModelProperty(value = "状态码")
    private Integer code;

    private String msg;
}
