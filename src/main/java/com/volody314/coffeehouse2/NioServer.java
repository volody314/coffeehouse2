package com.volody314.coffeehouse2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Класс серверной части канала
 */
public class NioServer {

    private Selector selector;
    private final InetSocketAddress listenAddress;
    private final String ADDRESS;
    private final int PORT;
    private ServerSocketChannel serverChannel;
    private final Map<SocketChannel, List> dataMapper = new HashMap<SocketChannel,List>();

    public NioServer(String addr, int port) {
        listenAddress = new InetSocketAddress(addr, port);
        this.ADDRESS = addr;
        this.PORT = port;
    }

    public void startServer() throws IOException {
        System.out.println("Starting NIO server");
        this.selector = Selector.open();
        System.out.println("Selector opened");
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(listenAddress);
        System.out.println("Socket binded");
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        System.out.println("NIO Server "+ADDRESS+":"+PORT+" started");

        while (true) {
            this.selector.select();

            Iterator keys = this.selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = (SelectionKey) keys.next();
                keys.remove();
                if (!key.isValid()) {
                    continue;
                }
                if (key.isAcceptable()) {
                    this.accept(key);
                }
                else if (key.isReadable()) {
                    this.read(key);
                }
            }
        }
    }

    //accept a connection made to this channel's socket
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel remoteChannel = serverChannel.accept();
        remoteChannel.configureBlocking(false);
        Socket socket = remoteChannel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);

        // register channel with selector for further IO
        dataMapper.put(remoteChannel, new ArrayList());
        remoteChannel.register(this.selector, SelectionKey.OP_READ);
    }

    //read from the socket channel
    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        int numRead = -1;
        numRead = channel.read(buffer);

        if (numRead == -1) {
            this.dataMapper.remove(channel);
            Socket socket = channel.socket();
            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
            System.out.println("Connection closed by client: " + remoteAddr);
            channel.close();
            key.cancel();
            return;
        }

        Order order = new Order(-1);
        byte[] data = new byte[numRead];
        System.arraycopy(buffer.array(), 0, data, 0, numRead);

        // Десериализация
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            order = (Order) in.readObject(); // **************************************
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }

        //String recvData = new String(data);
        System.out.println("Recieved from "+channel.socket().getRemoteSocketAddress()+" order " + order.getId());
    }
}
