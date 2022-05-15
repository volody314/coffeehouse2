package com.volody314.coffeehouse2;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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
    //private static final Map<Integer, Order> PRODUCTION_REPOSITORY_MAP = new HashMap<>();
    private static final Map<Integer, Order> DISTRIBUTION_REPOSITORY_MAP = new HashMap<>();

    // "Внешние" объекты
    BaristaServer barista = new BaristaServer("localhost", 3000);
    NioClient baristaClient = new NioClient("localhost", 3000);

    // Генератор ID заказа
    private static final AtomicInteger ORDER_ID_HOLDER = new AtomicInteger();


    public OrdersImpl() {
        // Старт потока баристы
        barista.start();

        // Старт клиента баристы
        try {
            TimeUnit.SECONDS.sleep(2);
            baristaClient.startClient();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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
        return ORDER_REPOSITORY_MAP.get(orderId).getItems();
    }

    // Дабавление позиции в заказ
    @Override
    public Integer putItem(Integer orderId, OrderItem orderItem) {
        return ORDER_REPOSITORY_MAP.get(orderId).addItem(orderItem);
    }

    // Удаление позиции из заказа
    @Override
    public List<OrderItem> deleteItem(Integer orderId, Integer itemId) {
        //System.out.println("Deleting item "+itemId+" in order"+orderId);
        return ORDER_REPOSITORY_MAP.get(orderId).deleteItem(itemId);
    }

    // Отправка заказа в производство (баристе)
    @Override
    public boolean produceOrder(Integer orderId) {
        Order order = ORDER_REPOSITORY_MAP.get(orderId);
        if (order != null) {
            //PRODUCTION_REPOSITORY_MAP.put(orderId, order);
            ORDER_REPOSITORY_MAP.remove(orderId);

            // Отправка баристе
            System.out.println("Sending to barista order "+orderId);
            baristaClient.sendList(order);

            return true;
        } else return false;
    }

    // Перечень заказов в производстве у баристы
    @Override
    public List<Integer> showProduced() {
        //return PRODUCTION_REPOSITORY_MAP.size();
        // ***************************************************
        return new ArrayList<>(); //(PRODUCTION_REPOSITORY_MAP.keySet());
    }

    // Перечень заказов на выдаче
    @Override
    public List<Integer> showDistribution() {
        //return DISTRIBUTION_REPOSITORY_MAP.size();
        return new ArrayList<>(DISTRIBUTION_REPOSITORY_MAP.keySet());
    }

    // Закрыть заказ
    @Override
    public void closeOrder(Integer orderId) {
        //PRODUCTION_REPOSITORY_MAP.remove(orderId);
        DISTRIBUTION_REPOSITORY_MAP.remove(orderId);
        ORDER_REPOSITORY_MAP.remove(orderId);
    }

    // Проверка наличия заказа в сервисе заказа
    @Override
    public boolean orderExists(Integer orderId) {
        return ORDER_REPOSITORY_MAP.containsKey(orderId);
    }

//    // Проверка наличия заказа в производстве - УДАЛИТЬ
//    @Override
//    public boolean productExists(Integer orderId) {
//        // ************************************************
//        return true; // PRODUCTION_REPOSITORY_MAP.containsKey(orderId);
//    }
//
//    // Проверка наличия заказа на выдаче
//    @Override
//    public boolean distribExists(Integer orderId) {
//        return DISTRIBUTION_REPOSITORY_MAP.containsKey(orderId);
//    }
}
