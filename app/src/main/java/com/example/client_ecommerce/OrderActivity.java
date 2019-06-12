package com.example.client_ecommerce;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.client_ecommerce.service.HttpRequest;
import com.example.client_ecommerce.service.HttpResponse;
import com.example.client_ecommerce.service.ThreadActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
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

        for(final Order o : orders){
            LinearLayout cardView = new LinearLayout(OrderActivity.this);
            cardView.setBackgroundColor(Color.LTGRAY);
            cardView.setOrientation(LinearLayout.VERTICAL);
            cardView.setLayoutParams(lparams);
            cardView.setPadding(25,25,25,25);

            TextView textViewPrice = new TextView(OrderActivity.this);
            textViewPrice.setLayoutParams(lparams);
            textViewPrice.setId(countViews++);
            textViewPrice.setText("R$"+ o.getTotal());
            textViewPrice.setPadding(10,10,10,10);
            textViewPrice.setGravity(Gravity.CENTER);
            cardView.addView(textViewPrice);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            TextView textViewName = new TextView(OrderActivity.this);
            textViewName.setLayoutParams(lparams);
            textViewName.setId(countViews++);
            textViewName.setText(format.format(o.getCreateDate()));
            textViewName.setPadding(10,10,10,10);
            textViewName.setGravity(Gravity.CENTER);
            cardView.addView(textViewName);

            Button buttonDelete = new Button(OrderActivity.this);
            buttonDelete.setLayoutParams(lparams);
            buttonDelete.setId(countViews++);
            buttonDelete.setText("Cancelar Pedido");
            buttonDelete.setPadding(10,10,10,10);
            buttonDelete.setGravity(Gravity.CENTER);
            buttonDelete.setBackgroundColor(Color.RED);
            buttonDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                    builder.setTitle("Excluir pedido");

                    final EditText input = new EditText(OrderActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteOrder();
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
            ll.addView(cardView);
        }
    }

    private void deleteOrder(){

    }
}
