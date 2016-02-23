package client.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Dmitry Khoruzhenko on 19.02.2016.
 * Information System Client
 */
public class ISClient {
    private String serverHostName = "localhost";
    private int  serverPort = 50001;

    public ISClient(String serverHostName, int serverPort) {
        super();
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
        StartClient();
    }

    public ISClient() {
        super();
        StartClient();
    }

    private void StartClient()  {

        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        for(int i=0; i<5;i++){
            try {
                socket = new Socket(serverHostName, serverPort);  //establish socket connection to server
                oos = new ObjectOutputStream(socket.getOutputStream());  //write to socket using ObjectOutputStream
                System.out.println("Sending request to Socket Server "+i);

                if(i==4) {
                    oos.writeObject("exit");
                    break;
                }
                else {
                    oos.writeObject("hello from client "+i);
                }


                ois = new ObjectInputStream(socket.getInputStream());  //read the server response message
                String message = null;
                try {
                    message = (String) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("Message: " + message);

                //close resources
                ois.close();
                oos.close();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
//                throw new IOException(e);
                e.printStackTrace();
            }
        }
    }

}
