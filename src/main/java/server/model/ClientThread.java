package server.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by khoruzh on 19.02.2016.
 */


public class ClientThread implements Runnable {
    Socket socket;//здесь будем хранить ссылку на наш сокет
    private String myName;
    private InputStream is = null;
    private OutputStream os = null;
    String message = "";
    String returnedMessage = "";


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
                System.out.println("waiting for client message... "); //debug
                message = readStringFromClient();
                System.out.println("--------------------------------------------- "); //debug
                System.out.println("Server have received this message from client: \n" + message); //debug
                System.out.println("--------------------------------------------- "); //debug
                // EXECUTING CLIENT QUERY   // обрабатываем запрос клиента
                returnedMessage = XmlServerOperations.makeAction(message);
                if (! returnedMessage.equals("")) {
                    writeStringToClient(returnedMessage);
                }

            }
            catch (IOException ex) {
                // Если мы сюда попали - значит, клиент отключился. Надо всё закрывать и завершать поток.
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
                    System.out.println("Client disconnect ok");  // debug
                    break;
                } catch (IOException ex2) {
                    System.out.println("Client thread error:  " + ex2.getMessage());
                    break;
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

    private void writeStringToClient(String message) throws IOException {
        if (message != null) {
            System.out.println("---- ClientThread.writeStringToClient посылаю сообщение на клиента(OutputStream): "+message); // debug
            for (int j = 0; j < message.length(); j++) {
                os.write((byte) message.charAt(j));
            }
            os.write('\r');
            os.flush();
        }
    }

}
