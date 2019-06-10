package com.example.client_ecommerce.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequest {

    private static HttpRequest httpRequest;
    private static final String URL = "http://192.168.15.6:8081";
    private static final String ENCODING = "UTF-8";

    public enum HttpMethod{ GET, POST, PUT, DELETE }

    public static HttpRequest getInstance(){
        if(httpRequest == null){
            httpRequest = new HttpRequest();
        }
        return httpRequest;
    }

    public String doRequest(String path, String method, String accept, String body){
        InputStreamReader isr = null;
        try {
            URL url = new URL(URL + path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Accept", accept);
            con.setDoOutput(false);

            if(body != null){
                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = body.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            isr = new InputStreamReader(con.getInputStream(), ENCODING);
            return Stream.convertInputStreamToString(isr);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Stream.closeAllStreams(isr);
        }
        return null;

    }
}