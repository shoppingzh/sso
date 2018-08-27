package com.littlezheng.util.sso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

    /**
     * ∑¢ÀÕpost«Î«Û
     * @param url
     * @param paramMap
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> paramMap) throws IOException{
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for(Entry<String, String> entry : paramMap.entrySet()){
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        post.setEntity(new UrlEncodedFormEntity(params));
        String resp = EntityUtils.toString(client.execute(post).getEntity());
        client.close();
        return resp;
    }
    
}
