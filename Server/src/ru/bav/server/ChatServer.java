package ru.bav.server;

/**
 * @author Barinov 15IT18
 */

import ru.bav.network.TCPConnection;
import ru.bav.network.TCPConnetionListener;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnetionListener {
    public static void main(String[] args) {
        new ChatServer();
    }
    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ChatServer(){
        System.out.println("Сервер запущен...");
        try(ServerSocket serverSocket = new ServerSocket(8189)){
            while (true){
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e){
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(ru.bav.network.TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Клиент подключился: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(ru.bav.network.TCPConnection tcpConnection, String s) {
sendToAllConnections(s);
    }

    @Override
    public synchronized void onDisconnet(ru.bav.network.TCPConnection tcpConnection) {
connections.remove(tcpConnection);
sendToAllConnections("Клиент отключился: " + tcpConnection);
    }

    @Override
    public synchronized void onException(ru.bav.network.TCPConnection tcpConnection, IOException e) {
        System.out.println("TCPConnection exeption: " + e);
    }

    private void sendToAllConnections(String value){
        System.out.println(value);
        for (TCPConnection connection: connections) {
            connection.sendString(value);
        }
    }
}
