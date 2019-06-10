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
    private Map<Product, Item> cart = new HashMap<>();

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


        Item itemMap = cart.get(p);
        if(itemMap == null){
            itemMap = new Item();
            itemMap.setProduct(p);
            itemMap.setQuantity(quantity);
        } else {
            Integer qtd = itemMap.getQuantity() + quantity;
            itemMap.setQuantity(qtd);
        }
        cart.put(p, itemMap);

        Double newTotal = (order.getTotal() == null ? 0 : order.getTotal()) +
                            (p.getPrice() * itemMap.getQuantity());
        order.setTotal(newTotal);

    }

    public void postOrder(User user){
        List<Item> items = new ArrayList<>();
        for (Item value : cart.values()) {
            items.add(value);
        }
        order.setItems(items);
        order.setUser(user);
    }

    public Order getOrder(){
        return this.order;
    }

    public void setOrder(Order order){
        this.order = order;
    }


}
