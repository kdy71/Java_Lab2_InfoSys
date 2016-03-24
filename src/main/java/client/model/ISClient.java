package client.model;

import common_model.Util;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by Dmitry Khoruzhenko on 19.02.2016.
 * Information System Client
 */
public class ISClient {
    private String serverHostName = "localhost";
    private int serverPort = 50001;
//    private String message;
//    private String returnedMessage;
    private InputStream is = null;
//    private OutputStream os = null;
    private OutputStreamWriter osw = null;
    Socket socket = null;

    public ISClient(String message) {
        super();
//        this.message = message;
        startClient();
    }

    public ISClient() {
        super();
        startClient();
    }

    public InputStream getInputStream() {
        return is;
    }
/*
    public OutputStream getOutputStream() {
        return os;
    }
*/
    public Socket getSocket() {
        return socket;
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
//            os = socket.getOutputStream(); //write to socket using ObjectOutputStream
            osw =  new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8"));
            is = socket.getInputStream();
            System.out.println("Sending request to Socket Server ");

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
//    }

    }

//    public void writeStringToServer(String message) throws IOException {
    public void writeStringToServer(String message)  {
        if (message != null) {
            System.out.println("-----------------------------) "); // debug
//            System.out.println("ISClient.writeStringToServer отправляет сообщ. на сервер (OutputStream) \n"+message); // debug
            try {
/*                for (int j = 0; j < message.length(); j++) {
//                    os.write((byte) message.charAt(j));
                    osw.write((byte) message.charAt(j));  //  ТУТ ОШИБКА !!!
                    System.out.println((byte)message.charAt(j)+" ");   // debug
                }
                */
//                os.write('\r');
//                os.flush();
                osw.write(message);
                osw.write('\r');
                osw.flush();
            }
            catch ( IOException e) {
                Util.showError("Ошибка при отправке запроса на сервер. \n"+e.getMessage());
                e.printStackTrace();  // убрать !!!
                // TODO: 22.03.2016   Log4j !!!
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
