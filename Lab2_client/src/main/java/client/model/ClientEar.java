package client.model;

import client.controller.Main_Client;
import common_model.Util_msg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * "Ухо" клиента.
 * Слушает сообщения с сервера в отдельном потоке в бесконечном цикле.
 * Created by Dmitry Khoruzhenko on 15.03.2016.
 */
public class ClientEar implements Runnable {

    public static final Logger log = LogManager.getLogger(ClientEar.class);
    private InputStreamReader isr = null;
    private String stXML = null;
    private Main_Client mainClient;


    /**
     * конструктор
     * @param is  - ссылка на InputStream
     * @param mainClient  -- ссылка на Main_Client
     */
    public ClientEar(InputStream is, Main_Client mainClient) {
        this.isr =  new InputStreamReader(is, Charset.forName("UTF-8"));
        this.mainClient = mainClient;
    }


    /**
     * метод run() - запускается автоматом при старте потока.
     * Слушает сообщения с сервера в бесконечном цикле.
     * Что услышит - отправляет на обработку.
     */
    public void run() {
        log.info("ClientEar() started ...");
        while (true) {
            try {
//                System.out.println(Util_dates.now2Str()+" ----- ClientEar - жду сообщения с сервера...");
                stXML = readStringFromServer(isr);
//                System.out.println(Util_dates.now2Str()+" ----- ClientEar. from srv stXML= " + stXML.substring(0,20));  // debug
                mainClient.stringXML_2Obj(stXML);
            } catch (IOException e) {
                Util_msg.showError("Ошибка при обработке сообщения от сервера.");
//                System.out.println(Util_dates.now2Str()+" --- ClientEar.  error stXML= " + stXML.substring(0,20));  // debug
                log.error("Exception while running ClientEar." + e);
            }
        }
    }


    /**
     * Считывает сообщение с сервера (строку XML)
     * @param isr - InputStreamReader, откуда читаем сообщение
     * @return  - возвращает считанное с сервера сообщение в строковом виде.
     * @throws IOException
     */
    public String readStringFromServer(InputStreamReader isr) throws IOException {
        int ch = isr.read();
        String message = "";
        while (ch >= 0 && ch != '\r') {
            message += (char) ch;
            ch = isr.read();
        }
        log.info("String from server was read by ClientEar.");
        return message;
    }
}
