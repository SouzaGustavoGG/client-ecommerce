package com.example.client_ecommerce;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.client_ecommerce.service.EcommerceService;
import com.example.client_ecommerce.service.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import app.mobile.ecommerce.ecommerce.model.Order;
import app.mobile.ecommerce.ecommerce.model.Product;

public class ProductActivity extends Activity {

    private HttpRequest requester;
    private int countViews = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        requester = HttpRequest.getInstance();

        if(android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
            String content = requester.doRequest("/product", HttpRequest.HttpMethod.GET.name(), "application/json", null);
            Gson gson = new GsonBuilder().create();
            List<Product> products = gson.fromJson(content,  new TypeToken<List<Product>>(){}.getType());

            addProductsToView(products);

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

    private void addProductsToView(List<Product> products){
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lparams.setMargins(10,10,10, 10);

        for(final Product p : products){
            LinearLayout cardView = new LinearLayout(ProductActivity.this);
            cardView.setBackgroundColor(Color.CYAN);
            cardView.setOrientation(LinearLayout.VERTICAL);
            cardView.setLayoutParams(lparams);
            cardView.setPadding(25,25,25,25);

            TextView textViewName = new TextView(ProductActivity.this);
            textViewName.setLayoutParams(lparams);
            textViewName.setId(countViews++);
            textViewName.setText(p.getName());
            textViewName.setPadding(10,10,10,10);
            textViewName.setGravity(Gravity.CENTER);
            cardView.addView(textViewName);

            TextView textViewPrice = new TextView(ProductActivity.this);
            textViewPrice.setLayoutParams(lparams);
            textViewPrice.setId(countViews++);
            textViewPrice.setText("R$"+ p.getPrice());
            textViewPrice.setPadding(10,10,10,10);
            textViewPrice.setGravity(Gravity.CENTER);
            cardView.addView(textViewPrice);

            Button buttonAdd = new Button(ProductActivity.this);
            buttonAdd.setLayoutParams(lparams);
            buttonAdd.setId(countViews++);
            buttonAdd.setText("Adicionar Item");
            buttonAdd.setPadding(10,10,10,10);
            buttonAdd.setGravity(Gravity.CENTER);
            buttonAdd.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                    builder.setTitle("Unidades");

                    final EditText input = new EditText(ProductActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int quantity = Integer.valueOf(input.getText().toString());
                            EcommerceService.getInstance().addItem(p, quantity);
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
            cardView.addView(buttonAdd);
            ll.addView(cardView);
        }
    }


}
