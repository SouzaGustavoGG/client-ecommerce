package com.example.client_ecommerce.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequest {

    private static HttpRequest httpRequest;
    private static final String URL = "http://192.168.15.26:8081";
    private static final String ENCODING = "UTF-8";

    public enum HttpMethod{ GET, POST, PUT, DELETE }

    public static HttpRequest getInstance(){
        if(httpRequest == null){
            httpRequest = new HttpRequest();
        }
        return httpRequest;
    }

    public HttpResponse doRequest(String path, String method, String accept, String body){
        HttpResponse response = new HttpResponse();
        InputStreamReader isr = null;
        try {
            URL url = new URL(URL + path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Accept", accept);
            con.setRequestProperty("Content-Type", "application/json; utf-8");

            if(body != null) {
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                os.write(body.getBytes(ENCODING));
                os.close();
            }

            isr = new InputStreamReader(con.getInputStream(), ENCODING);
            response.setBody(Stream.convertInputStreamToString(isr));
            response.setStatusCode(con.getResponseCode());
            response.setHeaders(con.getHeaderFields());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Stream.closeAllStreams(isr);
        }
        return response;

    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return getResizedBitmap(myBitmap, 15, 15);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }
}
