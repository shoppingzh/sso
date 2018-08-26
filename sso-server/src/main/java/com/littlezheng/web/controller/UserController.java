package com.littlezheng.web.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.littlezheng.web.support.AjaxResult;

@Controller
@RequestMapping(path="/user")
public class UserController {
    
    private static final Log LOG = LogFactory.getLog(UserController.class);
    
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWROD = "123";
    private static final Map<String, String> TOKEN_MAP = new HashMap<String, String>();

    @GetMapping(path="/login")
    public ModelAndView loginPage(@RequestParam(name="returnUrl") String returnUrl, 
    		HttpSession session,
        Model model) throws IOException{
    	if(isLogin(session)){
    		LOG.info("当前用户已登录，直接返回地址：" + returnUrl);
    		return back(returnUrl, TOKEN_MAP.get(session.getId()));
    	}
        LOG.info("SSO认证中心定位到登录界面，登录成功后将返回网址：" + returnUrl);
        model.addAttribute("returnUrl", returnUrl);
        return new ModelAndView("user/login");
    }
    

	private boolean isLogin(HttpSession session) {
		Boolean isLogin = (Boolean) session.getAttribute("isLogin");
		if(isLogin == null){
			return false;
		}
		return Boolean.valueOf(isLogin);
	}

	@PostMapping(path="/login")
    public ModelAndView login(@RequestParam(name="username") String username,
        @RequestParam(name="password") String password,
        @RequestParam(name="returnUrl") String returnUrl,
        Model model, HttpSession session) throws IOException{
        if(DEFAULT_USERNAME.equalsIgnoreCase(username) && DEFAULT_PASSWROD.equalsIgnoreCase(password)){
        	session.setAttribute("isLogin", true);
            //生成令牌
            String token = UUID.randomUUID().toString();
            LOG.info("SSO生成token：" + token);
            TOKEN_MAP.put(session.getId(), token);
            return back(returnUrl, token);
        }
        
        model.addAttribute("msg", "用户名或密码错误！");
        return loginPage(returnUrl, session, model);
    }
    
    private ModelAndView back(String returnUrl, String token) throws IOException{
        String sign = "?";
        if(returnUrl.indexOf("&") != -1){
            sign = "&";
        }
        String queryString = sign + "token=" + URLEncoder.encode(token, "UTF-8");
        String url = returnUrl + queryString;
        LOG.info("SSO认证中心登录成功，返回网址：" + url);
        
        return new ModelAndView("redirect:" + url);
    }
    
    @RequestMapping(path="/verify")
    public @ResponseBody AjaxResult verifyToken(@RequestParam(name="token") String token){
        LOG.info("SSO认证中心校验token：" + token);
        if(TOKEN_MAP.containsValue(token)){
            return new AjaxResult("100", "校验成功！");
        }else{
            return new AjaxResult("400", "校验失败！");
        }
    }
    
}
