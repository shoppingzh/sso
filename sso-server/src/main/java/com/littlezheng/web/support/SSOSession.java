package com.littlezheng.web.support;

import java.io.IOException;

public interface SSOSession {

    String getToken();
    
    String getUsername();
    
    boolean bindSubSystem(String url);
    
    boolean unbindSubSystem(String url);

    void invalidate() throws IOException;
    
}
