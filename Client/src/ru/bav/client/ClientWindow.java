package ru.bav.client;

/**
 * @author Barinov 15IT18
 */

import ru.bav.network.TCPConnection;
import ru.bav.network.TCPConnetionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, TCPConnetionListener {

    private static final String IP_ADDR = "192.168.43.191";
    private static final int PORT = 8189;
    private static final int WIDTH = 600;
    private static final int HIGHT = 400;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private final JTextField fieldNickname = new JTextField("Мурманск"); //стандартное имя
    private final JTextArea log = new JTextArea(); //область для чата
    private final JTextField fieldInput = new JTextField(); //строка для ввода текста в чат

    private TCPConnection connection;

    private ClientWindow() { // Для внешнего вида окна
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HIGHT);
        setLocationRelativeTo(null);
        // setAlwaysOnTop(true); // поверх других окон

        add(fieldNickname, BorderLayout.NORTH); //позволяет добавить интерфейсные элементы. (север - вверх, юг - низ, центер - низ)

        log.setEditable(false); //сообщения нельзя редактировать после отправки
        log.setLineWrap(true); // перенос слов
        add(log, BorderLayout.CENTER); //писать в центр

        fieldInput.addActionListener(this); // ждет пока не нажмется ENTER
        add(fieldInput, BorderLayout.SOUTH); // писать внизу (на юге)

        setVisible(true); //все ставновится видимым

        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            printMessage("Connection exception" + e);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText(); // сохраняется текст сообщения в переменную
        if (msg.equals("")) return; //проверка на пустой текст
        fieldInput.setText(null);
        connection.sendString(fieldNickname.getText() + ": " + msg); //передается текст на выход сокета
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMessage("Соединение установлено");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String s) {
        printMessage(s);
    }

    @Override
    public void onDisconnet(TCPConnection tcpConnection) {
        printMessage("Соединение разорвано");
    }

    @Override
    public void onException(TCPConnection tcpConnection, IOException e) {
printMessage("Connection exeption" + e);
    }

    private synchronized void printMessage(String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(message + "\n"); //добавляет сообщение
                log.setCaretPosition(log.getDocument().getLength()); // в самый низ чата кидает
            }
        });
    }
}
