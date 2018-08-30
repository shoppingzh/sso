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
        System.out.println("[��1��] �û�������ϵͳ���ж��Ƿ��ѵ�¼");
        //δ��¼
        if (session.getAttribute("username") == null) {
            boolean success = SSOUtils.login(request, response);
            if(success){
            }
            String token = request.getParameter("token");
            String username = SSOUtils.verifyToken(token);
            if(username != null){
                System.out.println("[��5��] Я��token����token��֤�ɹ��������ֲ��Ự");
                
                return true;
            }
            System.out.println("[��3��] û��Я��token��token��֤ʧ�ܣ��ض���sso��֤����");
           
            return false;
        }
        System.out.println("[��6��] �û�������ϵͳ���Ѿ���¼��������֤token");
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
