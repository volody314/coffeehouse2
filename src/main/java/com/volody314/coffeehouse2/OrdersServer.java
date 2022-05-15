package com.volody314.coffeehouse2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OrdersServer extends NioServer {

    // Заказы на исполнении
    private static final Map<Integer, Order> ordersRepo = new HashMap<>();
    NioClient baristaClient = new NioClient("localhost", 3001);

    OrdersServer(String addr, int port) {
        super(addr, port);
    }

    /**
     * Старт клиента баристы
     * Вынесен отдельно от конструктора, так как необходимо дождаться подъёма
     * серверов, затем стартовать клиентов
     */
    public void exchangeStart() {
        try {
            baristaClient.startClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void businessLogic() {
        int i = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                //System.out.println("Orders timestamp " + i++);
                TimeUnit.SECONDS.sleep(2);
                synchronized (ordersRepo) {
                    for (Map.Entry<Integer, Order> order : ordersRepo.entrySet()) {
                        System.out.println("Order server sends to barista order " + order.getKey());
                        baristaClient.sendList(order.getValue());
                    }
                    ordersRepo.clear();
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
        System.out.println("Starting orders business logic");
        new Thread(business).start();

        Runnable server = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Starting orders server");
                    startServer(ordersRepo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        //System.out.println("Srarting orders server 1");
        new Thread(server).start();

    }

}
