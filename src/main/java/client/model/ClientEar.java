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

/*
    public ClientEar() {
        super();
    }
*/
    public ClientEar(InputStream is, Main_Client mainClient) {
        this.is = is;
        this.mainClient = mainClient;
    }

    public ClientEar(Socket socket, Main_Client mainClient) throws IOException {
        this.socket = socket;
        this.is = socket.getInputStream();
        this.mainClient = mainClient;
    }

    public void setInputStream(InputStream is) { this.is = is;}


    public void run() {
        System.out.println("ClientEar() started ...");  // debug
        dis = new DataInputStream(is);

        if (dis == null) {
            System.out.println("dis==null!" + dis);  // debug
        }
        System.out.println("dis= " + dis);  // debug
/*
        try {
            InputStreamReader isr = new InputStreamReader(socket.getInputStream(),"UTF8");//входящий поток данных
            ObjectInputStream ois = new ObjectInputStream(is);
            try {
                String st = (String)ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        int i;
        while (true) {

            try {
                System.out.println("dis2= " + dis);  // debug
                stXML = dis.readUTF(); // ждем пока сервер отошлет строку
                mainClient.stringXML_2Obj(stXML);
//                i = is.read();
//                i = dis.read();
//                System.out.println("stXML= " + stXML);
//                System.out.println("i= " + i);  // debug

            } catch (IOException e) {
                System.out.println("stXML= " + stXML);  // debug
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

}
