package com.volody314.coffeehouse2;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ReadyMonitor extends NioServer {
    // Заказы на исполнении
    private static final Map<Integer, Order> ordersRepo = new HashMap<>();

    ReadyMonitor(String addr, int port) {
        super(addr, port);
    }

    public void start() {
        Runnable server = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Starting monitoring server");
                    startServer(ordersRepo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(server).start();

    }

}
