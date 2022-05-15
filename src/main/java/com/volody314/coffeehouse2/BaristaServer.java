package com.volody314.coffeehouse2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Поток приготовления заказов
 */
public class BaristaServer extends NioServer {

    // Заказы на исполнении
    private static final Map<Integer, Order> ordersRepo = new HashMap<>();
    NioClient monitorClient = new NioClient("localhost", 3002);

    BaristaServer(String addr, int port) {
        super(addr, port);
    }

    /**
     * Старт клиента мониторинга
     * Вынесен отдельно от конструктора, так как необходимо дождаться подъёма
     * серверов, затем стартовать клиентов
     */
    public void exchangeStart() {
        try {
            monitorClient.startClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void businessLogic() {
        int i = 0;
        while (!Thread.currentThread().isInterrupted()) {
            // synchronized (ordersRepo) {
            try {
                //System.out.println("Barista timestamp " + i++ + " orders count "+ordersRepo.size());
                TimeUnit.SECONDS.sleep(20);
                // Обход заказов и имитация деятельности
                // Удалить заказы помеченные под удаление
                synchronized (ordersRepo) {
                    Iterator<Map.Entry<Integer, Order>> entryIterator = ordersRepo.entrySet().iterator();
                    while (entryIterator.hasNext()) {
                        Map.Entry<Integer, Order> entry = entryIterator.next();
                        if (entry.getValue().isRemoved()) {
                            entryIterator.remove();
                        }
                    }
                }
                System.out.println("Barista timestamp " + i++ + " orders count "+ordersRepo.size());
                //ordersRepo.clear();
                synchronized (ordersRepo) {
                    for (Map.Entry<Integer, Order> order : ordersRepo.entrySet()) {
                        // Если найден новый заказ (timeStamp == -1), то типа начать исполнение
                        if (order.getValue().getTimeStamp() == -1) { order.getValue().setTimeStamp(i); }
                        // Если заказ в работе, отметить выполненным
                        else if (!order.getValue().isProduced()) { order.getValue().setProduced(); }
                        // Если заказ выполнен, пометить на удаление
                        else { order.getValue().setRemoved(true); }
                        System.out.println("Barista sends to monitor order " + order.getKey() + " prod "
                            + order.getValue().isProduced() + " removed " + order.getValue().isRemoved());
                        monitorClient.sendList(order.getValue());
                    }
                }
                Thread.currentThread().yield();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    public void start() {
        Runnable business = new Runnable() {
            @Override
            public void run() {
                businessLogic();
            }
        };
        System.out.println("Starting barista business logic");
        new Thread(business).start();

        Runnable server = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Starting barista server");
                    startServer(ordersRepo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        //System.out.println("Srarting barista server 1");
        new Thread(server).start();

    }
}
