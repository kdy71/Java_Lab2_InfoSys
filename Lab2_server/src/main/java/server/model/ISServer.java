package server.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 18.02.2016.
 * Information System Server
 * Ждёт подключения новых клиентов.
 * Для каждого подключенного клиента создаёт отдельный поток - экземпляр класса ClientThread implements Runnable
 */

public class ISServer {
    private int port = 50001;
    private ServerSocket serverSocket;      // A server socket waits for requests to come in over the network.
    private boolean shutdown = false;         // переменная отключения соединения
    private int nextClientId = 1;            // id клиента
    ArrayList clientList = new ArrayList();   // список всех клиентов

    public static final Logger log = LogManager.getLogger(ISServer.class);


    /**
     * Конструктор
     * @param port - порт для серверного сокета
     */
    public ISServer(int port) {
        super();
        this.port = port;
        startServer();  //метод запускат сервер
    }


    /**
     * Конструктор
     */
    public ISServer() {
        super();
        log.info("Server constructor.");
        startServer();
    }


    /**
     * Ждёт подключения новых клиентов.
     * Для каждого подключенного клиента создаёт отдельный поток - экземпляр класса ClientThread implements Runnable
     */
    private void startServer() {
        ArrayList clientList = new ArrayList();
        try {
            serverSocket = new ServerSocket(port); //создаем серверСокет
            System.out.println("Server started");       // debug
            System.out.println("Connection wait...");   // debug

            while (!shutdown) {
                Socket incomingSocket = serverSocket.accept(); //при соединении с клиентом создаем входящий socket
                System.out.println("Client" + nextClientId + " connected");
                log.info("Client" + nextClientId + " connected");

                ClientThread client = new ClientThread(incomingSocket, nextClientId); //создаем новый тред для каждого клиента, передаем в тред сокет и номер
                clientList.add(client); //добавляем тред клиента в список клиентов
                Thread t = new Thread(client);
                nextClientId++;
                t.start(); //запускаем метод start у треда
                log.info("Server was started.");
            }

            System.out.println("Shutting down Socket server!!"); // сообщение при закрытии соединения
            serverSocket.close();           //close the ServerSocket object
        } catch (IOException e) {
            log.error("Exception while processing start server " + e);
            e.printStackTrace();
        }
    }

}
