package com.volody314.coffeehouse2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import static java.nio.ByteBuffer.allocate;

/**
 * Класс клиентской части канала
 */
public class NioClient {
    private final int PORT;
    private final String ADDRESS;
    private final ByteBuffer buffer = allocate(8192);
    SocketChannel channel;
    Selector selector;

    NioClient(String addr, int port) {
        this.PORT = port;
        this.ADDRESS = addr;
    }

    // Открывает канал
    public void startClient() throws IOException {
//        channel = SocketChannel.open();
//        channel.configureBlocking(false);
//        selector = Selector.open();
//        channel.register(selector, SelectionKey.OP_CONNECT);
//        channel.connect(new InetSocketAddress(this.ADDRESS, this.PORT));

        System.out.println("Starting NIO client");
        InetSocketAddress hostAddress =
                new InetSocketAddress(this.ADDRESS, this.PORT);
        channel = SocketChannel.open(hostAddress);
        System.out.println("NIO Client " + ADDRESS + ":" + PORT + " started");
    }

    // Закрывает канал
    public void closeClient() throws IOException {
        channel.close();
        System.out.println("NIO Client " + ADDRESS + ":" + PORT + " closed");
    }

    /**
     * Сериализует и отправляет серверу заказ
     * @param order Заказ
     * @throws IOException
     */
    public void sendList(Order order) {
        //byte [] bmessage = message.getBytes();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(order);
            out.flush();
            byte[] byteObj = bos.toByteArray();
            ByteBuffer buffer = ByteBuffer.wrap(byteObj);
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Sent");
        buffer.clear();
        //return true;
    }
}
