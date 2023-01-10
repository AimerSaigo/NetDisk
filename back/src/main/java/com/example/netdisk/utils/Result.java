package com.example.netdisk.utils;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

//统一返回结果的类
@Getter
@Setter
public class Result {
    private Integer code;
    private String token;
}
