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

import com.littlezheng.util.sso.HttpUtils;
import com.littlezheng.web.support.AjaxResult;

@Controller
@RequestMapping(path="/user")
public class UserController {
    
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWROD = "123";
    private static final Map<String, String> TOKEN_MAP = new HashMap<String, String>();
    private static final Set<String> SUB_SYSTEMS = new HashSet<String>();
    private static final Map<String, String> SUB_SYSTEM_MAP = new HashMap<String, String>();
    

    @GetMapping(path="/login")
    public ModelAndView loginPage(@RequestParam(name="returnUrl") String returnUrl, 
    		HttpSession session,
        Model model) throws IOException{
    	if(isLogin(session)){
    		return back(returnUrl, TOKEN_MAP.get(session.getId()));
    	}
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
            //��������
            String token = UUID.randomUUID().toString();
            TOKEN_MAP.put(session.getId(), token);
            String subSystem = getSubSystem(returnUrl);
            System.out.println("��ǰ��ϵͳ��" + subSystem);
            SUB_SYSTEMS.add(subSystem);
            SUB_SYSTEM_MAP.put(subSystem, token);
            
            System.out.println("[��4��] sso��֤���ĵ�¼�ɹ�������ȫ�ֻỰ��token����tokenЯ���Żص�ԭ����ҳ��, token: [" + token  + "]");
            return back(returnUrl, token);
        }
        
        model.addAttribute("msg", "�û������������");
        return loginPage(returnUrl, session, model);
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
        if(TOKEN_MAP.containsValue(token)){
            System.out.println("��ʼע��������ϵͳ");
            for(String sub : SUB_SYSTEMS){
                String logoutUrl = sub + "/user/logout";
                System.out.println("ע��URL��" + logoutUrl);
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", SUB_SYSTEM_MAP.get(sub));
                System.out.println(TOKEN_MAP);
                System.out.println("ע��token��" + SUB_SYSTEM_MAP.get(sub));
                String resp = HttpUtils.post(logoutUrl, params);
                System.out.println(resp);
            }
            
            //session.removeAttribute("isLogin");
            //session.invalidate();
            //TOKEN_MAP.clear();
            return new AjaxResult("200", "ע���ɹ���");
        }
        return new AjaxResult("201", "ע��ʧ�ܣ�");
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
        if(TOKEN_MAP.containsValue(token)){
            return new AjaxResult("100", "У��ɹ���");
        }else{
            return new AjaxResult("400", "У��ʧ�ܣ�");
        }
    }
    
}
