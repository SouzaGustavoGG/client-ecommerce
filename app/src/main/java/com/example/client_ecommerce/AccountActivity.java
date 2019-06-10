package com.example.client_ecommerce;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.client_ecommerce.service.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.mobile.ecommerce.ecommerce.model.Address;
import app.mobile.ecommerce.ecommerce.model.User;

public class AccountActivity extends Activity {

    private HttpRequest requester;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        requester = HttpRequest.getInstance();

        if(android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getUser();

        Button buttonUpdate = (Button) findViewById(R.id.buttonUpdateAccount);
        buttonUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        int userId = getIntent().getIntExtra("userid", -1);

                        final TextView username = (TextView) findViewById(R.id.textViewUsername);
                        final EditText password = (EditText) findViewById(R.id.editTextPassword);
                        final EditText street = (EditText) findViewById(R.id.editTextStreet);
                        final EditText num = (EditText) findViewById(R.id.editTextNum);
                        final EditText zipCode = (EditText) findViewById(R.id.editTextZipCode);
                        final EditText extra = (EditText) findViewById(R.id.editTextExtra);
                        final EditText city = (EditText) findViewById(R.id.editTextCity);
                        final EditText state = (EditText) findViewById(R.id.editTextState);
                        final EditText country = (EditText) findViewById(R.id.editTextCountry);

                        Address address = new Address();
                        address.setId(user.getId());
                        address.setStreet(street.getText().toString());
                        address.setNum(Integer.valueOf(num.getText().toString()));
                        address.setZipCode(zipCode.getText().toString());
                        address.setExtra(extra.getText().toString());
                        address.setCity(street.getText().toString());
                        address.setState(state.getText().toString());
                        address.setCountry(country.getText().toString());

                        User user = new User();
                        user.setUsername(username.getText().toString());
                        user.setPassword(password.getText().toString());
                        user.setAddress(address);

                        requester.doRequest(
                                "/user/" + userId,
                                HttpRequest.HttpMethod.PUT.name(),
                                "application/json",
                                new GsonBuilder().create().toJson(user));

                        getUser();

                    }
                };

                Thread t = new Thread(){
                    @Override
                    public void run(){
                        runOnUiThread(runnable);
                    }
                };
                t.start();
            }
        });
    }

    private void getUser(){
        final TextView username = (TextView) findViewById(R.id.textViewUsername);
        final EditText password = (EditText) findViewById(R.id.editTextPassword);
        final EditText street = (EditText) findViewById(R.id.editTextStreet);
        final EditText num = (EditText) findViewById(R.id.editTextNum);
        final EditText zipCode = (EditText) findViewById(R.id.editTextZipCode);
        final EditText extra = (EditText) findViewById(R.id.editTextExtra);
        final EditText city = (EditText) findViewById(R.id.editTextCity);
        final EditText state = (EditText) findViewById(R.id.editTextState);
        final EditText country = (EditText) findViewById(R.id.editTextCountry);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int userId = getIntent().getIntExtra("userid", -1);

                String content = requester.doRequest(
                        "/user/" + userId,
                        HttpRequest.HttpMethod.GET.name(),
                        "application/json",
                        null);

                Gson gson = new GsonBuilder().create();
                user = gson.fromJson(content,  User.class);

                username.setText(user.getUsername());
                password.setText(user.getPassword());
                street.setText(user.getAddress().getStreet());
                num.setText(String.valueOf(user.getAddress().getNum()));
                extra.setText(user.getAddress().getExtra());
                zipCode.setText(user.getAddress().getZipCode());
                city.setText(user.getAddress().getCity());
                state.setText(user.getAddress().getState());
                country.setText(user.getAddress().getCountry());

            }
        };

        Thread t = new Thread(){
            @Override
            public void run(){
                runOnUiThread(runnable);
            }
        };
        t.start();

    }
}
