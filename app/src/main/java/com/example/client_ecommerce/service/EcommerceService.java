package com.example.client_ecommerce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.mobile.ecommerce.ecommerce.model.Item;
import app.mobile.ecommerce.ecommerce.model.Order;
import app.mobile.ecommerce.ecommerce.model.Product;
import app.mobile.ecommerce.ecommerce.model.User;

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

        Item item = null;

        for(Item i : order.getItems()){
            if(i.getProduct().getSku() == p.getSku()){
                item = i;
                break;
            }
        }

        if(item == null){
            item= new Item();
            item.setProduct(p);
            item.setQuantity(quantity);
            order.getItems().add(item);

        } else {
            Integer newQuantity = item.getQuantity() + quantity;
            item.setQuantity(newQuantity);
        }


        Double newTotal = (order.getTotal() == null ? 0 : order.getTotal()) +
                            (p.getPrice() * item.getQuantity());
        order.setTotal(newTotal);

    }

    public void postOrder(User user){
        order.setUser(user);
    }

    public Order getOrder(){
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
