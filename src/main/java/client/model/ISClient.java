package client.model;

import common_model.Util;

import java.io.*;
import java.net.Socket;

/**
 * Created by Dmitry Khoruzhenko on 19.02.2016.
 * Information System Client
 */
public class ISClient {
    private String serverHostName = "localhost";
    private int serverPort = 50001;
    private String message;
    private InputStream is = null;
    private OutputStream os = null;
    Socket socket = null;


    public ISClient(String message) {
        super();
        this.message = message;
        startClient();
    }

    public ISClient() {
        super();
        startClient();
    }

    public InputStream getInputStream() { return is;}

    public OutputStream getOutputStream() { return os;}

    public Socket getSocket() {return socket;}

    //конструктор с сообщением - хмл строкой объекта
    public void setMessage(String message) {
        this.message = message;
    }

    private void startClient() {
//        for (int i = 0; i < 1; i++) {
        try {
            socket = new Socket(serverHostName, serverPort);  //establish socket connection to server
        } catch (IOException e) {
            // log4j
            Util.showError("Нет соединения с сервером. Приложение будет закрыто.");
            System.exit(1);
        }
        try {
                os = socket.getOutputStream(); //write to socket using ObjectOutputStream
                is = socket.getInputStream();
                System.out.println("Sending request to Socket Server " );


//                if (i == 0) {
                    writeStringToServer(message);
//                }

/*                if (i == 1) {
                    message = "EXIT";
                    writeStringToServer(message);
                }
*/
                System.out.println("socket 1 =" + socket); // debug
          //      os.close();
                System.out.println("socket 2 =" + socket); // debug
                try {
                    Thread.sleep(100);
                    System.out.println("socket 3 =" + socket); // debug
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
//        }
    }

    private void writeStringToServer(String message) throws IOException {
        if (message != null) {
            for (int j = 0; j < message.length(); j++) {
                os.write((byte) message.charAt(j));
            }
            os.write('\r');
            os.flush();

        }
    }
}
