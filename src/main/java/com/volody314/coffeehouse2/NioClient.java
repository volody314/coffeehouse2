package com.volody314.coffeehouse2;

import java.io.IOException;
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
    private int PORT;
    private String ADDRESS;
    private ByteBuffer buffer = allocate(1024);
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

    public void sendList(String message) throws IOException {
        byte [] bmessage = message.getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bmessage);
        channel.write(buffer);
        System.out.println("Sent " + message);
        buffer.clear();
        //return true;
    }
}
