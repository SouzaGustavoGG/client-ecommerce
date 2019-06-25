package com.example.client_ecommerce;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.client_ecommerce.service.EcommerceService;
import com.example.client_ecommerce.service.HttpRequest;
import com.example.client_ecommerce.service.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import app.mobile.ecommerce.ecommerce.model.Item;
import app.mobile.ecommerce.ecommerce.model.Order;
import app.mobile.ecommerce.ecommerce.model.User;

public class CurrentOrderActivity extends Activity {

    private HttpRequest requester = HttpRequest.getInstance();
    private EcommerceService ecommerceService = EcommerceService.getInstance();
    private int countViews = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int id = getIntent().getIntExtra("orderid", -1);

                if(id != -1){
                    HttpResponse response  = requester.doRequest(
                            "/order/" + id,
                            HttpRequest.HttpMethod.GET.name(),
                            "application/json",
                            null);

                    if(response.getStatusCode() == 200){
                        Gson gson = new GsonBuilder().create();
                        Order order = gson.fromJson(response.getBody(),  Order.class);
                        addOrderToView(order);
                    } else {
                        Toast.makeText(CurrentOrderActivity.this, "Pedido não encontrado",  Toast.LENGTH_LONG).show();;
                    }
                } else {
                    Order order = ecommerceService.getOrder();
                    addOrderToView(order);
                }
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

    private void addOrderToView(Order order){
        final TextView textViewOrderLabel = (TextView) findViewById(R.id.textViewOrderLabel);
        textViewOrderLabel.setText(textViewOrderLabel.getText().toString() +
                (order.getId() != null ? " #" + order.getId() : ""));

        LinearLayout ll = (LinearLayout) findViewById(R.id.llItems);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lparams.setMargins(10,10,10, 10);

        for(final Item i : order.getItems()){
            LinearLayout cardView = new LinearLayout(CurrentOrderActivity.this);
            cardView.setOrientation(LinearLayout.VERTICAL);
            cardView.setLayoutParams(lparams);
            cardView.setPadding(25,25,25,25);

            View divider = new View(this);
            divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));
            divider.setBackgroundColor(Color.GRAY);
            divider.setPadding(0, 10, 0, 10);
            cardView.addView(divider);

            TextView textViewProduct = new TextView(CurrentOrderActivity.this);
            textViewProduct.setLayoutParams(lparams);
            textViewProduct.setId(countViews++);
            textViewProduct.setText(i.getProduct().getName() + " - R$" + i.getProduct().getPrice() +
                    " " + i.getQuantity() + "x");
            textViewProduct.setPadding(10,10,10,10);
            textViewProduct.setGravity(Gravity.CENTER);
            cardView.addView(textViewProduct);

            ll.addView(cardView);
        }
    }
}
