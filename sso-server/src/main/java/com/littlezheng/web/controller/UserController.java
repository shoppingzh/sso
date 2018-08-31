package com.littlezheng.web.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.littlezheng.util.AjaxResult;
import com.littlezheng.web.support.BasicSSOSession;
import com.littlezheng.web.support.SSOSession;
import com.littlezheng.web.support.SSOSessionContext;

@Controller
@RequestMapping(path="/user")
public class UserController {
    
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWROD = "123";

    @GetMapping(path="/login")
    public ModelAndView loginPage(@RequestParam(name="returnUrl") String returnUrl, 
    		HttpSession session,
        Model model) throws IOException{
    	if(session.getAttribute("username") != null){
    		return back(returnUrl, (String) session.getAttribute("token"));
    	}
        model.addAttribute("returnUrl", returnUrl);
        return new ModelAndView("user/login");
    }

	@PostMapping(path="/login")
    public ModelAndView login(@RequestParam(name="username") String username,
        @RequestParam(name="password") String password,
        @RequestParam(name="returnUrl") String returnUrl,
        Model model, HttpSession session) throws IOException{
        if(DEFAULT_USERNAME.equalsIgnoreCase(username) && DEFAULT_PASSWROD.equalsIgnoreCase(password)){
            String token = SSOSessionContext.add(new BasicSSOSession(session, username));
            return back(returnUrl, token);
        }
        
        model.addAttribute("msg", "用户名或密码错误！");
        return loginPage(returnUrl, session, model);
    }
    
	//注册子系统
    private void registerSubSystem(String token, String returnUrl) {
        String subSystem = getSubSystem(returnUrl);
        SSOSession ssoSession = SSOSessionContext.get(token);
        if(ssoSession != null){
            ssoSession.bindSubSystem(subSystem);
        }
    }

    private ModelAndView back(String returnUrl, String token) throws IOException{
        registerSubSystem(token, returnUrl);
        
        String sign = "&";
        if(returnUrl.indexOf('?') == -1){
            sign = "?";
        }
        String queryString = "";
        if(token != null){
            queryString = sign + "token=" + URLEncoder.encode(token, "UTF-8");
        }
        String url = returnUrl + queryString;
        return new ModelAndView("redirect:" + url);
    }
    
    @RequestMapping(path="/logout")
    public @ResponseBody AjaxResult logout(@RequestParam(name="token") String token,
        HttpSession session) throws IOException{
        if(verify(token)){
            SSOSessionContext.get(token).invalidate();
        }
        return new AjaxResult("201", "注销失败！");
    }
    
    private String getSubSystem(String url){
        Matcher m = Pattern.compile("http://localhost/[^/]*").matcher(url);
        if(m.find()){
            return m.group();
        }
        return null;
    }
    
    @RequestMapping(path="/verify")
    public @ResponseBody AjaxResult verifyToken(@RequestParam(name="token") String token){
        if(verify(token)){
            return new AjaxResult("100", "校验成功！", SSOSessionContext.get(token).getUsername());
        }else{
            return new AjaxResult("101", "校验失败！");
        }
    }
    
    private boolean verify(String token){
        return SSOSessionContext.has(token);
    }
    
}
