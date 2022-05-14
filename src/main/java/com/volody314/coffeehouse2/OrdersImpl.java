package com.volody314.coffeehouse2;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Реализация списка заказов
 */
@Service
public class OrdersImpl implements Orders {

    // Хранилище заказов
    private static final Map<Integer, Order> ORDER_REPOSITORY_MAP = new HashMap<>();
    private static final Map<Integer, Order> PRODUCTION_REPOSITORY_MAP = new HashMap<>();
    private static final Map<Integer, Order> DISTRIBUTION_REPOSITORY_MAP = new HashMap<>();

    // Генератор ID заказа
    private static final AtomicInteger ORDER_ID_HOLDER = new AtomicInteger();


    @Override
    public Integer create() {
        final int orderId = ORDER_ID_HOLDER.incrementAndGet();
        Order order = new Order(orderId);
        ORDER_REPOSITORY_MAP.put(orderId, order);
        return orderId;
    }

    @Override
    public List<OrderItem> read(Integer orderId) {
        return ORDER_REPOSITORY_MAP.get(orderId).getItems();
    }

    @Override
    public Integer putItem(Integer orderId, OrderItem orderItem) {
        return ORDER_REPOSITORY_MAP.get(orderId).addItem(orderItem);
    }

    @Override
    public List<OrderItem> deleteItem(Integer orderId, Integer itemId) {
        //System.out.println("Deleting item "+itemId+" in order"+orderId);
        return ORDER_REPOSITORY_MAP.get(orderId).deleteItem(itemId);
    }

    @Override
    public boolean produceOrder(Integer orderId) {
        Order order = ORDER_REPOSITORY_MAP.get(orderId);
        if (order != null) {
            PRODUCTION_REPOSITORY_MAP.put(orderId, order);
            ORDER_REPOSITORY_MAP.remove(orderId);
            return true;
        } else return false;
    }

    @Override
    public boolean distributeOrder(Integer orderId) {
        Order order = PRODUCTION_REPOSITORY_MAP.get(orderId);
        if (order != null) {
            DISTRIBUTION_REPOSITORY_MAP.put(orderId, order);
            PRODUCTION_REPOSITORY_MAP.remove(orderId);
            return true;
        } else return false;
    }

    @Override
    public List<Integer> showProduced() {
        //return PRODUCTION_REPOSITORY_MAP.size();
        return new ArrayList<>(PRODUCTION_REPOSITORY_MAP.keySet());
    }

    @Override
    public List<Integer> showDistribution() {
        //return DISTRIBUTION_REPOSITORY_MAP.size();
        return new ArrayList<>(DISTRIBUTION_REPOSITORY_MAP.keySet());
    }

    @Override
    public void closeOrder(Integer orderId) {
        PRODUCTION_REPOSITORY_MAP.remove(orderId);
        DISTRIBUTION_REPOSITORY_MAP.remove(orderId);
        ORDER_REPOSITORY_MAP.remove(orderId);
    }

    @Override
    public boolean orderExists(Integer orderId) {
        return ORDER_REPOSITORY_MAP.containsKey(orderId);
    }

    @Override
    public boolean productExists(Integer orderId) {
        return PRODUCTION_REPOSITORY_MAP.containsKey(orderId);
    }

    @Override
    public boolean distribExists(Integer orderId) {
        return DISTRIBUTION_REPOSITORY_MAP.containsKey(orderId);
    }
}
