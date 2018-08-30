package com.littlezheng.web.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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

@Controller
@RequestMapping(path="/user")
public class UserController {
    
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWROD = "123";
    private static final Set<String> TOKENS = new HashSet<String>();
    private static final Map<String, String> TOKEN_MAP = new HashMap<String, String>();

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
            String token = UUID.randomUUID().toString();
            
            doLogin(session, username, token);
            registerSubSystem(token, returnUrl);
            
            System.out.println("[第4步] sso认证中心登录成功，生成全局会话和token，将token携带着回到原请求页面, token: [" + token  + "]");        
            return back(returnUrl, token);
        }
        
        model.addAttribute("msg", "用户名或密码错误！");
        return loginPage(returnUrl, session, model);
    }
    
	//注册子系统
    private void registerSubSystem(String token, String returnUrl) {
        String subSystem = getSubSystem(returnUrl);
        System.out.println("注册子系统：" + subSystem);
    }

    private void doLogin(HttpSession session, String username, String token) {
        session.setAttribute("username", username);
        session.setAttribute("token", token);
        TOKENS.add(token);
        TOKEN_MAP.put(token, username);
        System.out.println(TOKEN_MAP);
    }

    private ModelAndView back(String returnUrl, String token) throws IOException{
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
        if(TOKENS.contains(token)){
            return new AjaxResult("100", "校验成功！", TOKEN_MAP.get(token));
        }else{
            return new AjaxResult("101", "校验失败！");
        }
    }
    
}
