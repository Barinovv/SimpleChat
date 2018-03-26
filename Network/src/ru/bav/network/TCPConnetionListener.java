package ru.bav.network;

/**
 * @author Barinov 15IT18
 */

import java.io.IOException;

public interface TCPConnetionListener {

    void onConnectionReady(TCPConnection tcpConnection);

    void onReceiveString (TCPConnection tcpConnection, String s);

    void onDisconnet(TCPConnection tcpConnection);

    void onException(TCPConnection tcpConnection, IOException e);

}
