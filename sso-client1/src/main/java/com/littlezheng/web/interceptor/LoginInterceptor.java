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
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(true);
        System.out.println("[第1步] 用户访问子系统，判断是否已登录");
        //未登录
        if (session.getAttribute("username") == null) {
            boolean success = SSOUtils.login(request, response);
            if(success){
            }
            String token = request.getParameter("token");
            String username = SSOUtils.verifyToken(token);
            if(username != null){
                System.out.println("[第5步] 携带token并且token验证成功，创建局部会话");
                
                return true;
            }
            System.out.println("[第3步] 没有携带token或token验证失败，重定向到sso认证中心");
           
            return false;
        }
        System.out.println("[第6步] 用户访问子系统，已经登录，无需验证token");
        return true;
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
