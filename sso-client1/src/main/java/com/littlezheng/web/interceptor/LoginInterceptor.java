package com.littlezheng.web.interceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.littlezheng.util.sso.SSOUtils;

public class LoginInterceptor implements HandlerInterceptor{
    
    private static final String SSO_SERVER_LOGIN_URL = "http://localhost/sso-server/user/login";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(true);
        Boolean isLogin = (Boolean) session.getAttribute("isLogin");
        System.out.println("[第1步] 用户访问子系统，判断是否已登录");
        if (isLogin == null || !Boolean.valueOf(isLogin)) {
            System.out.println("[第2步] 用户未登录，判断是否携带token");
            if(SSOUtils.verifyToken(request, response)){
                System.out.println("[第5步] 携带token并且token验证成功，创建局部会话");
                session.setAttribute("isLogin", true);
                session.setAttribute("token", request.getParameter("token"));
                return true;
            }
            System.out.println("[第3步] 没有携带token或token验证失败，重定向到sso认证中心");
            String redirectUrl = SSO_SERVER_LOGIN_URL + "?returnUrl=" + getReturnUrl(request);
            response.sendRedirect(redirectUrl);
            return false;
        }
        System.out.println("[第6步] 用户访问子系统，已经登录，无需验证token");
        
        return true;
    }
    
    private String getReturnUrl(HttpServletRequest request) {
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

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
        throws Exception {
        
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
    }

}
