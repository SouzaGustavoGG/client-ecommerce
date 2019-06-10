package com.example.client_ecommerce;

import android.app.Activity;
import android.os.Bundle;

import com.example.client_ecommerce.service.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import app.mobile.ecommerce.ecommerce.model.Order;

public class ProductActivity extends Activity {

    private HttpRequest requester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requester = HttpRequest.getInstance();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String content = requester.doRequest("/produto", HttpRequest.HttpMethod.GET.name(), "application/json", null);
                Gson gson = new GsonBuilder().create();

                List<Order> orders = gson.fromJson(content,  new TypeToken<List<Order>>(){}.getType());
            }
        };

        ProductActivity.this.runOnUiThread(runnable);
    }


}
