package client.model;

import client.controller.Main_Client;

import java.io.*;
import java.net.Socket;

/**
 * "Ухо" клиента.
 * Слушает сообщения с сервера в отдельном потоке в бесконечном цикле.
 * Created by Dmitry Khoruzhenko on 15.03.2016.
 */
public class ClientEar implements Runnable {
    private String serverHostName = "localhost";
    private int serverPort = 50001;
    private String messageFromSrv;
    private InputStream is = null;
    private OutputStream os = null;
    Socket socket = null;
    String stXML = null;
    DataInputStream dis = null;
    private Main_Client mainClient;


    public ClientEar(String serverHostName, int serverPort) {
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
    }


    public ClientEar(InputStream is, Main_Client mainClient) {
        this.is = is;
        this.mainClient = mainClient;
    }

    public ClientEar(Socket socket, Main_Client mainClient) throws IOException {
        this.socket = socket;
        this.is = socket.getInputStream();
        this.mainClient = mainClient;
    }

//    public void setInputStream(InputStream is) { this.is = is;}


    public void run() {
        System.out.println("ClientEar() started ...");  // debug
        dis = new DataInputStream(is);

        if (dis == null) {
            System.out.println("dis==null!" + dis);  // debug
        }
        System.out.println("dis= " + dis);  // debug
        while (true) {

            try {
//                System.out.println("dis2= " + dis);  // debug
//                stXML = dis.readUTF(); // ждем пока сервер отошлет строку
                System.out.println("----- ClientEar - жду сообщения с сервера...");
                stXML = readStringFromServer(is);
                System.out.println("----- ClientEar. from srv stXML= " + stXML);  // debug
//                if ( !stXML.equals("")) {
                    mainClient.stringXML_2Obj(stXML);
//                }

            } catch (IOException e) {
                System.out.println("ClientEar.  error stXML= " + stXML);  // debug
                e.printStackTrace();
                // log4j
            }

            System.out.print(".");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    //    public String readStringFromServer() throws IOException {
    public String readStringFromServer(InputStream is) throws IOException {
        int ch = is.read();
        String message = "";
        while (ch >= 0 && ch != '\r') {
            message += (char) ch;
            ch = is.read();
        }
        return message;
    }


}
