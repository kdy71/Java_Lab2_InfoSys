package client.model;

import java.io.InputStream;
import java.io.OutputStream;
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


    public ClientEar(String serverHostName, int serverPort) {
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
//        run();
    }


    public ClientEar() {
//        run();
    }

    public void run() {
        System.out.println("ClientEar() started ...");
        while (true) {

            System.out.print(".");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
