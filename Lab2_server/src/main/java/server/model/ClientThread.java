package server.model;

import common_model.Util_dates;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by khoruzh on 19.02.2016.
 * Поток для общения с клиентом.
 * Такие потоки создаются сервером для каждого клиента, при подключении клиента к серверу.
 */
public class ClientThread implements Runnable {
    Socket socket;//здесь будем хранить ссылку на наш сокет
    private String myName;
    private OutputStreamWriter osw = null;
    private InputStreamReader isr = null;

    String message = "";   // сообщение от клиента
    String returnedMessage = "";  // сообщение клиенту от сервера

    public static final Logger log = LogManager.getLogger(ClientThread.class);


    /**
     * конструктор,в который мы передаем ссылку на сокет
     * @param socket - ссылка на сокет
     * @param num  - порядковый  номер который добавляется к имени клиента
     */
    ClientThread(Socket socket, int num) {//конструктор,в который мы передаем
        this.socket = socket;//ссылку на сокет
        myName = "Client" + num;//порядковый  номер который добавляется к имени клиента
        log.info("ClientThread constructor. Client thread " + num);
    }

    /**
     * Метод run() - запускается автоматом при старте потока
     * В цикле ждёт сообщене от клиента (через сокет),
     * затем обрабатывает сообщение и сформированный ответ отправляет обратно клиенту.
     */
    public void run() {
        try {
            isr = new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8"));
            osw = new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //пока сокет "жив"
        while (socket != null) {
            try {
                System.out.println("waiting for client message... "); //debug
                message = readStringFromClient();
                returnedMessage = XmlServerOperations.makeAction(message);  // здесь обрабатываем запрос клиента
                if (!returnedMessage.equals("")) {
                    writeStringToClient(returnedMessage);
                }
            } catch (IOException ex) {
                // Если мы сюда попали - значит, клиент отключился. Надо всё закрывать и завершать поток.
                try {
                    if (isr != null) {
                        isr.close();
                    }
                    if (osw != null) {
                        osw.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                    System.out.println(Util_dates.now2Str() + " --- Client disconnect ok");  // debug
                    log.info(myName+" disconnected. " + ex);
                    break;
                } catch (IOException ex2) {
                    System.out.println(Util_dates.now2Str() + " --- Client thread error:  " + ex2.getMessage());
                    log.error("Exception while disconnection client thread. " + ex2);
                    break;
                }
            }
        }
        log.info(myName +" thread was run.");
    }


    /**
     * Считываем сообщение от клиента. Сообщение - как правило, строка XML
     * @return  - сообщение от клиента
     * @throws IOException - если ошибка при чтении сообщения
     */
    private String readStringFromClient() throws IOException {
        int ch = isr.read();
        String message = "";
        while (ch >= 0 && ch != '\r') {
            message += (char) ch;
            ch = isr.read();
        }
        log.info("String from client was read in client thread.");
        return message;
    }


    /**
     * Отправить сообщение  клиенту. Сообщение, - как правило строка XML
     * @param message - сообщение, которое будем отправлять.
     * @throws IOException - если ошибка при отправке сообщения.
     */
    private void writeStringToClient(String message) throws IOException {
        if (message != null) {
            System.out.println(Util_dates.now2Str() + " ---- ClientThread.writeStringToClient посылаю сообщение на клиента(OutputStream): " + message.substring(0, 20)); // debug
            osw.write(message);
            osw.write('\r');
            osw.flush();
            log.info("String to client was written in client thread.");
        }
    }
}
