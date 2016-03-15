package server.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 18.02.2016.
 * Information System Server
 */

//  see http://nouck.net/2014/04/mnogopotochnyj-soket-server-na-java/
//  see http://www.journaldev.com/741/java-socket-server-client-read-write-example
public class ISServer {
    private int port = 50001;
    //private String host = "localhost";
    //private String studentsFileName = "students.xml";
    //private String groupsFileName = "groups.xml";
    //private File studentsFile = new File(studentsFileName);
    //private File groupsFile = new File(groupsFileName);
    private ServerSocket serverSocket;      // A server socket waits for requests to come in over the network.
    private boolean shutdown = false;         // переменная отключения соединения
    private int nextClientId = 1;            // id клиента
    ArrayList clientList = new ArrayList();   // список всех клиентов

    public ISServer(int port) {
        super();
        this.port = port;
        startServer();  //метод запускат сервер
    }

    public ISServer() {
        super();
        startServer();
    }

    private void startServer() {
        int i = 1;
        ArrayList clientList = new ArrayList();
        try {
            serverSocket = new ServerSocket(port); //создаем серверСокет
            System.out.println("Server started");       // debug
            System.out.println("Connection wait...");   // debug

            while (!shutdown) {
                Socket incomingSocket = serverSocket.accept(); //при соединении с клиентом создаем входящий socket
                System.out.println("Client" + nextClientId + " connected");

                ClientThread client = new ClientThread(incomingSocket, nextClientId); //создаем новый тред для каждого клиента, передаем в тред сокет и номер
                clientList.add(client); //добавляем тред клиента в список клиентов
                System.out.println("aa");
                Thread t = new Thread(client);
                nextClientId++;
                System.out.println("bb");
                t.start(); //запускаем метод start у треда
                System.out.println("cc");
            }

            System.out.println("Shutting down Socket server!!"); // сообщение при закрытии соединения
            //close the ServerSocket object
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
