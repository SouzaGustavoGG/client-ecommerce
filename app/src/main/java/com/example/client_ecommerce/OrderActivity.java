package com.example.client_ecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.client_ecommerce.service.HttpRequest;
import com.example.client_ecommerce.service.HttpResponse;
import com.example.client_ecommerce.service.ThreadActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import app.mobile.ecommerce.ecommerce.model.Order;
import app.mobile.ecommerce.ecommerce.model.Product;

public class OrderActivity extends ThreadActivity {

    private HttpRequest requester;
    private int countViews = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        requester = HttpRequest.getInstance();

        getOrders();
    }

    private void getOrders(){
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                HttpResponse response = null;
                try {
                    int userId = getIntent().getIntExtra("userid", -1);
                    response = requester.doRequest(
                            "/pedido?user=" + URLEncoder.encode(String.valueOf(userId),"UTF-8"), HttpRequest.HttpMethod.GET.name(),
                            "application/json", null);


                    if(response.getStatusCode() == 200){
                        Gson gson = new GsonBuilder().create();
                        List<Order> orders = gson.fromJson(response.getBody(),  new TypeToken<List<Order>>(){}.getType());

                        addOrdersToView(orders);
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
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

    private void addOrdersToView(List<Order> orders){
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lparams.setMargins(10,10,10, 10);

        Collections.sort(orders, new Comparator<Order>() {
            public int compare(Order o1, Order o2) {
                return -1 * (o1.getCreateDate().compareTo(o2.getCreateDate()));
            }
        });

        for(final Order o : orders){
            LinearLayout cardView = new LinearLayout(OrderActivity.this);
            cardView.setOrientation(LinearLayout.VERTICAL);
            cardView.setLayoutParams(lparams);
            cardView.setPadding(25,25,25,25);

            View divider = new View(this);
            divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));
            divider.setBackgroundColor(Color.GRAY);
            divider.setPadding(0, 10, 0, 10);
            cardView.addView(divider);

            TextView textViewPrice = new TextView(OrderActivity.this);
            textViewPrice.setLayoutParams(lparams);
            textViewPrice.setId(countViews++);
            textViewPrice.setText("R$"+ o.getTotal());
            textViewPrice.setPadding(10,10,10,10);
            textViewPrice.setGravity(Gravity.CENTER);
            cardView.addView(textViewPrice);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            TextView textViewName = new TextView(OrderActivity.this);
            textViewName.setLayoutParams(lparams);
            textViewName.setId(countViews++);
            textViewName.setText(format.format(o.getCreateDate()));
            textViewName.setPadding(10,10,10,10);
            textViewName.setGravity(Gravity.CENTER);
            cardView.addView(textViewName);

            Button buttonInfo = new Button(OrderActivity.this);
            buttonInfo.setLayoutParams(lparams);
            buttonInfo.setId(countViews++);
            buttonInfo.setText("Info Pedido");
            buttonInfo.setPadding(10, 10, 10, 10);
            buttonInfo.setGravity(Gravity.CENTER);
            buttonInfo.setBackgroundColor(Color.GRAY);
            buttonInfo.setTextColor(Color.BLACK);
            buttonInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(), CurrentOrderActivity.class);
                    intent.putExtra("orderid", o.getId());
                    startActivity(intent);
                }
            });
            cardView.addView(buttonInfo);

            Date createTime = o.getCreateDate();
            Date nowTime = Calendar.getInstance().getTime();
            long diff = nowTime.getTime() - createTime.getTime();
            int diffDays = (int) (diff / (24 * 60 * 60 * 1000));

            if(diffDays <= 3) {

                Button buttonDelete = new Button(OrderActivity.this);
                buttonDelete.setLayoutParams(lparams);
                buttonDelete.setId(countViews++);
                buttonDelete.setText("Cancelar Pedido");
                buttonDelete.setTextColor(Color.WHITE);
                buttonDelete.setPadding(10, 10, 10, 10);
                buttonDelete.setGravity(Gravity.CENTER);
                buttonDelete.setBackgroundColor(Color.RED);
                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final int orderId = o.getId();

                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                        builder.setTitle("Excluir pedido");

                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteOrder(orderId);
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.show();
                    }
                });
                cardView.addView(buttonDelete);

            }

            ll.addView(cardView);
        }
    }

    private void deleteOrder(int id){
        HttpResponse response = HttpRequest.getInstance().doRequest(
                "/pedido/"+ id,
                HttpRequest.HttpMethod.DELETE.name(),
                null,null);

        if (response.getStatusCode() == 200){
            Toast.makeText(OrderActivity.this, "Pedido cancelado.", Toast.LENGTH_LONG).show();
            finish();
            startActivity(getIntent());
        } else {
            Toast.makeText(OrderActivity.this,
                    "Não foi possível cancelar pedido. " + (response.getBody() != null ? response.getBody() : ""),
                    Toast.LENGTH_LONG).show();
        }
    }
}
