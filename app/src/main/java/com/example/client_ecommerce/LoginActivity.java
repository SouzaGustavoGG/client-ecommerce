package com.example.client_ecommerce;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.client_ecommerce.service.HttpRequest;
import com.google.gson.GsonBuilder;

import app.mobile.ecommerce.ecommerce.model.User;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username = (EditText) findViewById(R.id.editTextUser);
        final EditText password = (EditText) findViewById(R.id.editText2);

        final Button loginButton = (Button) findViewById(R.id.button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin(username.getText().toString(), password.getText().toString());
            }
        });


    }

    public void doLogin(final String username, final String password){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);

                String content = HttpRequest.getInstance().doRequest(
                        "/user/login",
                        HttpRequest.HttpMethod.POST.name(),
                        "text/plain", new GsonBuilder().create().toJson(user));

                boolean success = Boolean.valueOf(content);

                if(success){
                    Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }
        };

        Thread t = new Thread(runnable);
        t.start();
    }
}
