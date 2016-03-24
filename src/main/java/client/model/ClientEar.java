package client.model;

import client.controller.Main_Client;
import common_model.Util;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * "Ухо" клиента.
 * Слушает сообщения с сервера в отдельном потоке в бесконечном цикле.
 * Created by Dmitry Khoruzhenko on 15.03.2016.
 */
public class ClientEar implements Runnable {
    private String serverHostName = "localhost";
    private int serverPort = 50001;
    private String messageFromSrv;
//    private InputStream is = null;
//    private OutputStream os = null;
    private InputStreamReader isr = null;
    Socket socket = null;
    String stXML = null;
//    DataInputStream dis = null;
    private Main_Client mainClient;


    public ClientEar(String serverHostName, int serverPort) {
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
    }


    public ClientEar(InputStream is, Main_Client mainClient) {
//        this.is = is;
        this.isr =  new InputStreamReader(is, Charset.forName("UTF-8"));
        this.mainClient = mainClient;
    }

    public ClientEar(Socket socket, Main_Client mainClient) throws IOException {
        this.socket = socket;
//        this.is = socket.getInputStream();
        this.isr =  new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8"));
        this.mainClient = mainClient;
    }


    public void run() {
        System.out.println("ClientEar() started ...");  // debug
        while (true) {

            try {
                System.out.println(Util.now2Str()+" ----- ClientEar - жду сообщения с сервера...");
                stXML = readStringFromServer(isr);
                System.out.println(Util.now2Str()+" ----- ClientEar. from srv stXML= " + stXML.substring(0,20));  // debug
//                if ( !stXML.equals("")) {
                    mainClient.stringXML_2Obj(stXML);
//                }

            } catch (IOException e) {
                System.out.println(Util.now2Str()+" --- ClientEar.  error stXML= " + stXML.substring(0,20));  // debug
                e.printStackTrace();
                // log4j
            }
        }

    }

    //    public String readStringFromServer() throws IOException {
//    public String readStringFromServer(InputStream is) throws IOException {
    public String readStringFromServer(InputStreamReader isr) throws IOException {
//        int ch = is.read();
        int ch = isr.read();
        String message = "";
        while (ch >= 0 && ch != '\r') {
            message += (char) ch;
//            ch = is.read();
            ch = isr.read();
        }
        return message;
    }


}
