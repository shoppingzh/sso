package com.littlezheng.web.support;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.littlezheng.util.HttpUtils;
import com.littlezheng.util.sso.TokenUtils;

public class BasicSSOSession implements SSOSession{

    private String token;
    private String username;
    private HttpSession httpSession;
    private Set<String> subSystems;

    public BasicSSOSession(HttpSession httpSession, String username) {
        init(httpSession, username);
    }
    
    private void init(HttpSession httpSession, String username) {
        this.subSystems = new HashSet<String>();
        this.httpSession = httpSession;
        this.username = username;
        this.httpSession = httpSession;
        this.token = TokenUtils.genToken();
        this.httpSession.setAttribute("sso_session", this);
        this.httpSession.setAttribute("username", username);
        this.httpSession.setAttribute("token", token);
    }

    @Override
    public String getUsername() {
        return username;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public void invalidate() throws IOException {
        this.httpSession.removeAttribute("sso_session");
        this.httpSession.removeAttribute("username");
        this.httpSession.removeAttribute("token");
        System.out.println("注销所有子系统：" + this.subSystems);
        for(String subSystem : subSystems){
            Map<String, String> param = new HashMap<String, String>();
            param.put("token", token);
            HttpUtils.post(subSystem + "/user/logout", param);
        }
    }

    @Override
    public String toString() {
        return "BasicSSOSession [token=" + token + ", username=" + username + ", httpSession=" + httpSession + "]";
    }

    @Override
    public boolean bindSubSystem(String url) {
        return subSystems.add(url);
    }

    @Override
    public boolean unbindSubSystem(String url) {
        return subSystems.remove(url);
    }

}
