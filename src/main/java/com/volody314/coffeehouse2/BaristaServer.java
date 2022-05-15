package com.volody314.coffeehouse2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Поток приготовления заказов
 */
public class BaristaServer extends NioServer {

    // Заказы на исполнении
    private static final Map<Integer, Order> PRODUCTION_REPOSITORY_MAP = new HashMap<>();

    BaristaServer(String addr, int port) {
        super(addr, port);
    }
    private void businessLogic() {
        int i = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println("Barista timestamp " + i++);
                TimeUnit.SECONDS.sleep(40);
                //Thread.currentThread().yield();
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
                    System.out.println("Starting barista server 2");
                    startServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        System.out.println("Srarting barista server 1");
        new Thread(server).start();

    }
}
