package com.littlezheng.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.littlezheng.util.sso.AjaxResult;
import com.littlezheng.util.sso.SSOUtils;

@Controller
@RequestMapping(path="/user")
public class UserController {
    
    @GetMapping(path="/logout")
    public ModelAndView logout(HttpSession session) throws IOException{
        String sessionToken = (String) session.getAttribute("token");
        if(sessionToken != null){
            SSOUtils.logout(sessionToken);
        }
        return new ModelAndView();
    }
    
    @PostMapping(path="/logout")
    public @ResponseBody AjaxResult logout(String token,
        HttpSession session){
        String sessionToken = (String) session.getAttribute("token");
        System.out.println("token: " + token + ", session token: " + sessionToken);
        if(token != null && sessionToken != null && token.equals(sessionToken)){
            doLogout(session);
            return new AjaxResult("200", "注销成功！");
        }
        
        return new AjaxResult("201", "注销失败！");
    }
    
    private void doLogout(HttpSession session) {
        System.out.println("注销子系统");
        session.removeAttribute("isLogin");
        session.removeAttribute("token");
        session.invalidate();
    }

    @RequestMapping(path="/profile")
    public ModelAndView profile(){
        System.out.println("获取用户信息..");
        return new ModelAndView("user/profile");
    }
  

}
