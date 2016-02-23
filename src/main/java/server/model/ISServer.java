package server.model;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Dmitry Khoruzhenko on 18.02.2016.
 * Information System Server
 */

//  see http://nouck.net/2014/04/mnogopotochnyj-soket-server-na-java/
//  see http://www.journaldev.com/741/java-socket-server-client-read-write-example
public class ISServer {
    private int port = 50001;
//    private String host = "localhost";
    private String studentsFileName = "students.xml";
    private String groupsFileName = "groups.xml";
    private File studentsFile = new File(studentsFileName);
    private File groupsFile = new File(groupsFileName);
    private ServerSocket serverSocket;
    private boolean shutdown=false;
    private int nextClientId =1;
    ArrayList clientList=new ArrayList();



    public ISServer(int port) {
        super();
        this.port = port;
        startServer();
    }

    public ISServer() {
        super();
        startServer();
    }

    private void startServer(){
        int i=1;
        ArrayList clientList=new ArrayList();
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started");       // debug
            System.out.println("Connection wait...");   // debug

            while(!shutdown){

/*
                System.out.println("Waiting for client request");
                Socket socket = serverSocket.accept();  //creating socket and waiting for client connection
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());  //read from socket to ObjectInputStream object
                String message = null;    //convert ObjectInputStream object to String
                try {
                    message = (String) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("Message Received: " + message);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());  //create ObjectOutputStream object
                oos.writeObject("Hi Client "+message);  //write object to Socket
                //close resources
                ois.close();
                oos.close();
                socket.close();
                //terminate the server if client sends exit request
                if(message.equalsIgnoreCase("exit")) break;
*/

                Socket incomingSocket=serverSocket.accept();
                System.out.println("Client"+ nextClientId +" connected");
                ClientThread client=new ClientThread(incomingSocket, nextClientId);
                clientList.add(client);
                Thread t=new Thread(client);
                nextClientId++;
                t.start();
            }
            System.out.println("Shutting down Socket server!!");
            //close the ServerSocket object
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
