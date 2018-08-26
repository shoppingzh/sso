package com.littlezheng.web.interceptor;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.littlezheng.web.support.AjaxResult;

public class LoginInterceptor implements HandlerInterceptor{
    
    private static final String SSO_SERVER_LOGIN_URL = "http://localhost/sso-server/user/login?";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(true);
        Boolean isLogin = (Boolean) session.getAttribute("isLogin");
        if(isLogin == null || !Boolean.valueOf(isLogin)){
        	String token = request.getParameter("token");
            if(token != null && !"".equals(token.trim())){
                System.out.println("子系统2获取token成功，开始去sso认证中心校验token[" + token + "]");
                if(ssoVerify(token)){
                    System.out.println("子系统2token校验成功，直接登录！");
                    session.setAttribute("isLogin", true);
                    return true;
                }
                System.out.println("子系统2token校验失败！");
            }
            System.out.println("用户未登录，重定向到sso认证中心登录页面！");
            String queryString = "returnUrl=" +  URLEncoder.encode(request.getRequestURL().toString(), "UTF-8");
            response.sendRedirect(SSO_SERVER_LOGIN_URL + queryString);
            return false;
        }
        return true;
    }
    
    /**
     * 校验token
     * @param token
     * @return
     * @throws Exception
     */
    private static boolean ssoVerify(String token) throws Exception{
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost/sso-server/user/verify");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        post.setEntity(new UrlEncodedFormEntity(params));
        HttpResponse resp = client.execute(post);
        String json = EntityUtils.toString(resp.getEntity());
        System.out.println("sso认证中心返回校验信息：" + json);
        return "100".equals(new Gson().fromJson(json, AjaxResult.class).getCode());
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
        throws Exception {
        
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
    }

}
