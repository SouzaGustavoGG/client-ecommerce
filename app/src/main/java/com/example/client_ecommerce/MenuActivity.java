package com.example.client_ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Button buy = (Button) findViewById(R.id.buttonBuy);
        final Button order = (Button) findViewById(R.id.buttonOrder);
        final Button account  = (Button) findViewById(R.id.buttonAccount);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userId = getIntent().getIntExtra("userid", -1);
                Intent intent = new Intent(getBaseContext(), ProductActivity.class);
                intent.putExtra("userid", userId);
                startActivity(intent);
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userId = getIntent().getIntExtra("userid", -1);
                Intent intent = new Intent(getBaseContext(), OrderActivity.class);
                intent.putExtra("userid", userId);
                startActivity(intent);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userId = getIntent().getIntExtra("userid", -1);
                Intent intent = new Intent(getBaseContext(), AccountActivity.class);
                intent.putExtra("userid", userId);
                startActivity(intent);
            }
        });
    }
}
