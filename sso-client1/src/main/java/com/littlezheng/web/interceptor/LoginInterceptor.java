package com.littlezheng.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.littlezheng.util.sso.LoginCallback;
import com.littlezheng.util.sso.SSOUtils;

public class LoginInterceptor implements HandlerInterceptor{
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	HttpSession session = request.getSession();
    	if(session.getAttribute("username") == null){
    		SSOUtils.login(request, response, new LoginCallback() {
				@Override
				public void onSuccess(HttpServletRequest req, HttpServletResponse resp,
						String username, String token) {
					req.getSession().setAttribute("username", username);
					req.getSession().setAttribute("token", token);
				}
				
			});
    	}
    	
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
