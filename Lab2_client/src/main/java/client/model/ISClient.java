package client.model;

import common_model.Util_msg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by Dmitry Khoruzhenko on 19.02.2016.
 * Information System Client
 * Здесь создаётся клиентский сокет.
 * И прописаны методы для отправки сообщений на сервер.
 */
public class ISClient {
    private String serverHostName = "localhost";
    private int serverPort = 50001;
    private InputStream is = null;
    private OutputStreamWriter osw = null;
    Socket socket = null;

    public static final Logger log = LogManager.getLogger(ISClient.class);


    /**
     * конструктор
     */
    public ISClient() {
        super();
        startClient();
        log.info("ISClient constructor");
    }


    /**
     * Возвращает InputStream для клиентского сокета
     * @return  InputStream для клиентского сокета
     */
    public InputStream getInputStream() {
        return  is;
    }


    /**
     * Возвращает ссылку на клиентский сокет
     * @return  - ссылка на клиентский сокет
     */
    public Socket getSocket() {
        return socket;
    }


    /**
     * Здесь создаётся подключение к серверу (сокет).
     * Затем для сокета создаём OutputStreamWriter - чтоб отправлять сообщения на сервер.
     */
    private void startClient() {
        try {
            socket = new Socket(serverHostName, serverPort);  //establish socket connection to server
        } catch (IOException e) {
            Util_msg.showError("Нет соединения с сервером. Приложение будет закрыто.");
            log.error("Exception while processing startClient. There is no connection with server." +
                    " Application closed. " + e);
            System.exit(1);
        }
        try {
            osw = new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8"));
            is = socket.getInputStream();
            log.info("Sending request to Socket Server ");

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            log.error("There is no connection with server. Application closed" + e);
        }
        log.info("Client was started.");
    }


    /**
     * Отправка сообщения на сервер.
     * @param message  - строка сообщения.
     */
    public void writeStringToServer(String message) {
        if (message != null) {
            System.out.println("-----------------------------) "); // debug
            try {
                osw.write(message);
                osw.write('\r');
                osw.flush();
            } catch (IOException e) {
                Util_msg.showError("Ошибка при отправке запроса на сервер. \n" + e.getMessage());
                log.error("Ошибка при отправке запроса на сервер. " + e.getMessage());
            }
        }
    }


}
