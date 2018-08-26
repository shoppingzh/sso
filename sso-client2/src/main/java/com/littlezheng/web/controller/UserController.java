package com.littlezheng.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path="/user")
public class UserController {
    
    @GetMapping(path="/login")
    public ModelAndView loginPage(){
        //CloseableHttpClient client = HttpClients.createDefault();
        return new ModelAndView("user/login");
    }
    
    @RequestMapping(path="/profile")
    public ModelAndView profile(){
        System.out.println("子系统2获取用户信息..");
        return new ModelAndView("user/profile");
    }
  

}
