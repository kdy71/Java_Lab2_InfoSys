package client.model;

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

    //конструктор с сообщением - хмл строкой объекта
    public void setMessage(String message) {
        this.message = message;
    }

    private void startClient() {
        for (int i = 0; i < 1; i++) {
            try {
                socket = new Socket(serverHostName, serverPort);  //establish socket connection to server
                os = socket.getOutputStream(); //write to socket using ObjectOutputStream
                System.out.println("Sending request to Socket Server " + i);


                if (i == 0) {
                    writeStringToServer(message);
                }

                if (i == 1) {
                    message = "EXIT";
                    writeStringToServer(message);
                }

                os.close();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
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
