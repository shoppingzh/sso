package com.littlezheng.web.support;

import java.util.HashMap;
import java.util.Map;

public class SSOSessionContext {

    private static final Map<String, SSOSession> SSO_SESSION_MAP = new HashMap<String, SSOSession>();
    
    public static String add(SSOSession ssoSession){
        if(ssoSession == null){
            throw new IllegalArgumentException("ssoSession is null!");
        }
        String token = ssoSession.getToken();
        SSO_SESSION_MAP.put(token, ssoSession);
        return token;
    }
    
    public static String remove(SSOSession ssoSession){
        if(ssoSession == null){
            throw new IllegalArgumentException("ssoSession is null!");
        }
        String token = ssoSession.getToken();
        SSO_SESSION_MAP.remove(token);
        return token;
    }
    
    public static SSOSession get(String token){
        return SSO_SESSION_MAP.get(token);
    }
    
    public static boolean has(String token){
        return SSO_SESSION_MAP.get(token) != null;
    }
    
}
