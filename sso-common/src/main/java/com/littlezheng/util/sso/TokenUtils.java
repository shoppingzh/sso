package com.littlezheng.util.sso;

import java.util.UUID;

public class TokenUtils {

    /**
     * ����һ����token
     * @return
     */
    public static String genToken(){
        return UUID.randomUUID().toString();
    }
    
}
