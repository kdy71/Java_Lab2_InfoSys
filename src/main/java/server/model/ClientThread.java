package server.model;
import java.io.*;
import java.net.Socket;

/**
 * Created by itps13 on 19.02.2016.
 */


public class ClientThread implements Runnable {
    Socket socket;//здесь будем хранить ссылку на наш сокет
//    private boolean shutdown=false;
//    private InputStreamReader inStream;
//    private OutputStreamWriter outStream;
//    private Scanner scanner;
//    private String message;
//    private PrintWriter out;
    private String myName;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    ClientThread(Socket socket, int num){//конструктор,в который мы передаем
        this.socket = socket;//ссылку на сокет
        myName="Client"+num;//порядковый  номер который добавляется к имени клиента
    }

    @Override
    public void run() {

        while(socket !=null){ //пока сокет "жив"
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                String messageFromClient = null;
                try {
                    messageFromClient = (String) ois.readObject();  // получаем запрос от клиента
                }
                catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("Server have received this message from client: " + messageFromClient);

                // **********************
                // EXECUTING CLIENT QUERY   // обрабатываем запрос клиента
                //  . . .
                // *********************
                if (messageFromClient != null &&  messageFromClient.equalsIgnoreCase("exit")) {break;}

                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(" Bla-bla-bla  from server...");  // отвечаем клиенту на его запрос
            }
            catch (IOException ex) {
                System.out.println("Error initialization clients streams:  "+ex.getMessage());
            }
            finally{//при закрытии сокета
                try {
                    if (ois != null)    {ois.close();}
                    if (oos != null)    {oos.close();}
                    if (socket != null) {socket.close();} //закрываем сокет
                    socket =null;
                    System.out.println("Client disconnect");
                }
                catch (IOException ex) {
                    System.out.println("Client thread error:  "+ex.getMessage());
                }

            }
        }
    }
}
