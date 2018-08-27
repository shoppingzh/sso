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
        System.out.println("[��1��] �û�������ϵͳ���ж��Ƿ��ѵ�¼");
        if (isLogin == null || !Boolean.valueOf(isLogin)) {
            System.out.println("[��2��] �û�δ��¼���ж��Ƿ�Я��token");
            if(SSOUtils.verifyToken(request, response)){
                System.out.println("[��5��] Я��token����token��֤�ɹ��������ֲ��Ự");
                session.setAttribute("isLogin", true);
                session.setAttribute("token", request.getParameter("token"));
                return true;
            }
            System.out.println("[��3��] û��Я��token��token��֤ʧ�ܣ��ض���sso��֤����");
            String redirectUrl = SSO_SERVER_LOGIN_URL + "?returnUrl=" + getReturnUrl(request);
            response.sendRedirect(redirectUrl);
            return false;
        }
        System.out.println("[��6��] �û�������ϵͳ���Ѿ���¼��������֤token");
        
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
