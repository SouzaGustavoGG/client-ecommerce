package com.example.client_ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import app.mobile.ecommerce.ecommerce.model.Item;
import app.mobile.ecommerce.ecommerce.model.Order;
import app.mobile.ecommerce.ecommerce.model.Product;

public class EcommerceService {

    private static EcommerceService ecommerceService;

    private Order order;

    public static EcommerceService getInstance(){
        if(ecommerceService == null){
            ecommerceService = new EcommerceService();
        }
        return ecommerceService;
    }

    public void addItem(Product p, int quantity){
        if(order == null){
            order = new Order();
            order.setItems(new ArrayList<Item>());
        }
        Item item = new Item();
        item.setProduct(p);
        item.setQuantity(quantity);

        List<Item> items = order.getItems();
        items.add(item);
    }
}
