package com.volody314.coffeehouse2;

//import java.util.concurrent.*;
//import java.lang.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Поток приготовления заказов
 */
public class BaristaServer extends NioServer {
    BaristaServer(String addr, int port) {
        super(addr, port);
    }
    private void businessLogic() {
        int i = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println("Barista " + i++);
                TimeUnit.SECONDS.sleep(60);
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

        //baristaServer = new NioServer("localhost", 3000);
    }
}
