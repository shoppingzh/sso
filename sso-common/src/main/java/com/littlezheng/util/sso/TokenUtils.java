package com.littlezheng.util.sso;

import java.util.UUID;

public class TokenUtils {

    /**
     * 生成一个简单token
     * @return
     */
    public static String genToken(){
        return UUID.randomUUID().toString();
    }
    
}
