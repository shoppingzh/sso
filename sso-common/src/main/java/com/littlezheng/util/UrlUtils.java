package com.littlezheng.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class UrlUtils {
    
    private static final String ENCODE_CHARSET = "UTF-8";
    
    /**
     * ��ȡ����������·�����磺http://192.168.1.10:8080/app/path?query1=xxx&query2=xxx
     * @param request
     * @return
     */
    public static String getFullURL(HttpServletRequest request){
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if(StringUtils.isNotBlank(queryString)){
            url = url + "?" + getQueryString(request);
        }
        return url;
    }
    
    /**
     * ��ȡ�������������URL����
     * @param request
     * @return
     */
    public static String getQueryString(HttpServletRequest request){
        String queryString = request.getQueryString();
        if(StringUtils.isNotBlank(queryString)){
            queryString = encodeQueryString(queryString);
        }else{
            queryString = "";
        }
        return queryString;
     }
    
    //�����������
    private static String encodeQueryString(String queryString) {
        StringBuilder sb = new StringBuilder();
        String[] queryParts = queryString.split("&");
        if(queryParts != null){
            for(String query : queryParts){
                int subIdx = 0;
                if((subIdx = query.indexOf('=')) != -1){
                    String name = query.substring(0, subIdx);
                    String value = query.substring(subIdx+1);
                    try {
                        sb.append("&").append(name).append("=").append(URLEncoder.encode(value, ENCODE_CHARSET));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(sb.length() > 1){
                sb.deleteCharAt(0);
            }
        }
        return sb.toString();
    }
    
}
