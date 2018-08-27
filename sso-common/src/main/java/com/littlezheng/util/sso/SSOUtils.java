package com.littlezheng.util.sso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

public class SSOUtils {
    
    private static final String SSO_VERIFY_URL = "http://localhost/sso-server/user/verify";
    private static final String SSO_LOGOUT_URL = "http://localhost/sso-server/user/logout";

    public static boolean verifyToken(HttpServletRequest request, HttpServletResponse response){
        String token = request.getParameter("token");
        if(StringUtils.isBlank(token)){
            return false;
        }
        try {
            return doVerify(token, request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean doVerify(String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        String json = HttpUtils.post(SSO_VERIFY_URL, params);
        return "100".equals(new Gson().fromJson(json, AjaxResult.class).getCode());
    }

    public static boolean logout(String token) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        String json = HttpUtils.post(SSO_LOGOUT_URL, params);
        return "100".equals(new Gson().fromJson(json, AjaxResult.class).getCode());
    }
    
}
