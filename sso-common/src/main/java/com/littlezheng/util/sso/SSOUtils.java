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
    
    /**
     * sso登录
     * @param req
     * @param resp
     * @param callback
     * @throws IOException
     */
    public static void login(HttpServletRequest req, HttpServletResponse resp, LoginCallback callback) throws IOException{
    	String token = req.getParameter("token");
        if(StringUtils.isNotBlank(token)){
            String username = verifyToken(token);
            if(StringUtils.isNotBlank(username)){
            	callback.onSuccess(req, resp, username, token);
            }
        }
        String redirectUrl = SSO_SERVER_LOGIN_URL + "?returnUrl=" + getReturnUrl(req);
        resp.sendRedirect(redirectUrl);
    }
    
    //获取返回URL
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

    /**
     * 校验令牌，校验成功返回登录的用户名
     * @param token
     * @return
     */
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

    /**
     * sso登出
     * @param token
     * @return
     * @throws IOException
     */
    public static boolean logout(String token) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        String json = HttpUtils.post(SSO_LOGOUT_URL, params);
        return "100".equals(new Gson().fromJson(json, AjaxResult.class).getCode());
    }
    
}
