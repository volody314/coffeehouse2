package com.volody314.coffeehouse2;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Реализация списка заказов
 */
@Service
public class OrdersImpl implements Orders {

    // Хранилище заказов
    private static final Map<Integer, Order> ORDER_REPOSITORY_MAP = new HashMap<>();

    // "Внешние" объекты

    // Сервер производства (бариста)
    BaristaServer baristaServer = new BaristaServer("localhost", 3001);
    // Сервер заказов
    OrdersServer ordersServer = new OrdersServer("localhost", 3000);
    ReadyMonitor readyMonitor = new ReadyMonitor("localhost", 3002);
    // Клиент для обращения к серверу заказов
    NioClient ordersClient = new NioClient("localhost", 3000);

    // Генератор ID заказа
    private static final AtomicInteger ORDER_ID_HOLDER = new AtomicInteger();


    public OrdersImpl() {
        // Старт потока баристы
        baristaServer.start();
        ordersServer.start();
        readyMonitor.start();

        // Старт клиента заказов
        try {
            TimeUnit.SECONDS.sleep(2);
            ordersClient.startClient();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        ordersServer.exchangeStart();
        baristaServer.exchangeStart();
    }

    // Создание заказа
    @Override
    public Integer create() {
        final int orderId = ORDER_ID_HOLDER.incrementAndGet();
        Order order = new Order(orderId);
        ORDER_REPOSITORY_MAP.put(orderId, order);
        return orderId;
    }

    // Чтение позиций заказа при заказе
    @Override
    public List<OrderItem> read(Integer orderId) {
        if (orderExists(orderId))
            return ORDER_REPOSITORY_MAP.get(orderId).getItems();
        else return null;
    }

    // Дабавление позиции в заказ
    @Override
    public Integer putItem(Integer orderId, OrderItem orderItem) {
        if (orderExists(orderId))
            return ORDER_REPOSITORY_MAP.get(orderId).addItem(orderItem);
        else return -1;
    }

    // Удаление позиции из заказа
    @Override
    public List<OrderItem> deleteItem(Integer orderId, Integer itemId) {
        return ORDER_REPOSITORY_MAP.get(orderId).deleteItem(itemId);
    }

    // Отправка заказа на сервер
    @Override
    public boolean produceOrder(Integer orderId) {
        Order order = ORDER_REPOSITORY_MAP.get(orderId);
        if (order != null) {
            ORDER_REPOSITORY_MAP.remove(orderId);
            System.out.println("Web sends order "+orderId+" to order server");
            ordersClient.sendList(order);
            return true;
        } else return false;
    }

    // Перечень заказов в производстве у баристы
    @Override
    public List<Integer> showProducing() {
        List<Integer> rez = new ArrayList<>();
        for (Map.Entry<Integer, Order> order : readyMonitor.getMonitor().entrySet()) {
            if (!(order.getValue().isProduced())) { rez.add(order.getKey()); }
        }
        //return new ArrayList<>(readyMonitor.getMonitor().keySet());
        return rez;
    }

    // Перечень заказов на выдаче
    @Override
    public List<Integer> showDistribution() {
        List<Integer> rez = new ArrayList<>();
        for (Map.Entry<Integer, Order> order : readyMonitor.getMonitor().entrySet()) {
            if (order.getValue().isProduced()) { rez.add(order.getKey()); }
        }
        //return new ArrayList<>(readyMonitor.getMonitor().keySet());
        return rez;
    }

    // Закрыть заказ
    @Override
    public void closeOrder(Integer orderId) {
        ORDER_REPOSITORY_MAP.remove(orderId);
    }

    // Проверка наличия заказа в сервисе заказа
    @Override
    public boolean orderExists(Integer orderId) {
        return ORDER_REPOSITORY_MAP.containsKey(orderId);
    }
}
