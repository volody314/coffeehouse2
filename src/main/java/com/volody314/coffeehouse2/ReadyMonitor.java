package com.volody314.coffeehouse2;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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

        Runnable cleaner = new Runnable() {
            @Override
            public void run() {
                // Удаляет из мониторинга заказы, помеченные для удаления
                synchronized (ordersRepo) {
                    //ordersRepo.removeIf();
                    Iterator<Map.Entry<Integer, Order>> entryIterator = ordersRepo.entrySet().iterator();
                    while (entryIterator.hasNext()) {
                        Map.Entry<Integer, Order> entry = entryIterator.next();
                        if (entry.getValue().isRemoved()) {
                            entryIterator.remove();
                        }
                    }
                }
                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        };
        System.out.println("Starting monitor cleaner");
        new Thread(cleaner).start();
    }

    public Map<Integer, Order> getMonitor() {
        return ordersRepo;
    }
}
