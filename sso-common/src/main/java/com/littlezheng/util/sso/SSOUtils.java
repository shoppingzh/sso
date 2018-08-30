package com.littlezheng.util.sso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.littlezheng.util.AjaxResult;
import com.littlezheng.util.HttpUtils;

public class SSOUtils {
    
    private static final String SSO_SERVER_LOGIN_URL = "http://localhost/sso-server/user/login";
    private static final String SSO_VERIFY_URL = "http://localhost/sso-server/user/verify";
    private static final String SSO_LOGOUT_URL = "http://localhost/sso-server/user/logout";
    
    public static boolean login(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String token = req.getParameter("token");
        if(StringUtils.isNotBlank(token)){
            String username = verifyToken(token);
            if(StringUtils.isNotBlank(username)){
                req.getSession(true).setAttribute("zxpsso_username", username);
                return true;
            }
        }
        String redirectUrl = SSO_SERVER_LOGIN_URL + "?returnUrl=" + getReturnUrl(req);
        resp.sendRedirect(redirectUrl);
        
        return false;
    }
    
    //»ñÈ¡·µ»ØURL
    private static String getReturnUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if(StringUtils.isNotBlank(queryString)){
            url += "?" + queryString;
        }
        try {
            url = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String verifyToken(String token){
        if(StringUtils.isNotBlank(token)){
            try {
                return doVerify(token);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String doVerify(String token) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        String json = HttpUtils.post(SSO_VERIFY_URL, params);
        AjaxResult result = new Gson().fromJson(json, AjaxResult.class);
        if("100".equals(result.getCode())){
            return (String) result.getData();
        }
        return null;
    }

    public static boolean logout(String token) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        String json = HttpUtils.post(SSO_LOGOUT_URL, params);
        return "100".equals(new Gson().fromJson(json, AjaxResult.class).getCode());
    }
    
}
