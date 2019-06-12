package com.example.client_ecommerce;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.client_ecommerce.service.HttpRequest;
import com.example.client_ecommerce.service.HttpResponse;
import com.example.client_ecommerce.service.ThreadActivity;
import com.google.gson.GsonBuilder;

import app.mobile.ecommerce.ecommerce.model.User;

public class LoginActivity extends ThreadActivity {

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
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    String jsonBody = new GsonBuilder().create().toJson(user);
                    System.out.println(jsonBody);

                    HttpResponse response = HttpRequest.getInstance().doRequest(
                            "/user/login",
                            HttpRequest.HttpMethod.POST.name(),
                            "text/plain", jsonBody);

                    if (response.getStatusCode() == 200){
                        Integer userId = Integer.valueOf(response.getBody().replace("\n",""));
                        if(userId >=0){
                            Intent intent = new Intent(getBaseContext(), MenuActivity.class);
                            intent.putExtra("username", username);
                            intent.putExtra("userid", userId);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Usuário ou senha incorretos.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            };
        Thread t = new Thread() {
            @Override
            public void run() {
                runOnUiThread(runnable);
            }
        };
        t.start();
    }
}
