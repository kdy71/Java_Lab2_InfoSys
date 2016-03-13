package server.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by itps13 on 19.02.2016.
 */


public class ClientThread implements Runnable {
    Socket socket;//здесь будем хранить ссылку на наш сокет
    private String myName;
    private InputStream is = null;
    private OutputStream os = null;
    String message = "";


    ClientThread(Socket socket, int num) {//конструктор,в который мы передаем
        this.socket = socket;//ссылку на сокет
        myName = "Client" + num;//порядковый  номер который добавляется к имени клиента
    }

    public void run() {
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //пока сокет "жив"
        while (socket != null) {
            try {
                //получить сообщение, создать документ, вытянуть root элемент
                //Если студент или группа, то создать и дописать новый элемент

                message = readStringFromClient();
                System.out.println("Server have received this message from client: " + message); //debug

                XmlServerOperations.makeAction(message);

                //Document document = XmlServerOperations.getDocumentFromString(message); //cоздаем Документ

                //Object newObject = XmlServerOperations.unmarshalObject(document);//создаем объект

                //XmlServerOperations.addObjectToXmlFile(newObject, document);

                break; //после операций с объектом выход

                // **********************
                // EXECUTING CLIENT QUERY   // обрабатываем запрос клиента
                // *********************

            } catch (IOException ex) {
                System.out.println("Error initialization clients streams:  " + ex.getMessage());
            } finally {//при закрытии сокета
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                    System.out.println("Client disconnect");

                } catch (IOException ex) {
                    System.out.println("Client thread error:  " + ex.getMessage());
                }
            }
        }
    }

    private String readStringFromClient() throws IOException {
        int ch = is.read();
        String message = "";
        while (ch >= 0 && ch != '\r') {
            message += (char) ch;
            ch = is.read();
        }
        return message;
    }

}
