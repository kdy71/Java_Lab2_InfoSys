package client.model;

import common_model.Util_msg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 19.02.2016.
 * Information System Client
 * Здесь создаётся клиентский сокет.
 * И прописаны методы для отправки сообщений на сервер.
 */
public class ISClient {
    private String serverHostName = "localhost";
    private Integer serverPort = 50001;
    private InputStream is = null;
    private OutputStreamWriter osw = null;
    Socket socket = null;

//    private static String SERVER_PARAMS = ".\\Lab2_client\\src\\main\\java\\client\\model\\clientConfig.xml";
    private static String SERVER_PARAMS = "clientConfig.xml";
    public static final Logger log = LogManager.getLogger(ISClient.class);


    /**
     * конструктор
     */
    public ISClient() {
        super();
        setServerNameAndPortFromXml();
        startClient();
        log.info("ISClient constructor");
    }


    /**
     * Достает из файла и устанавливает параметры сервера, к которому нужно подключиться клиенту.
     */
    public void setServerNameAndPortFromXml() {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(SERVER_PARAMS);
            NodeList nodes = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                if ("serverName".equals(nodes.item(i).getNodeName())) {
                    serverHostName = nodes.item(i).getTextContent();
                }
                if ("serverPort".equals(nodes.item(i).getNodeName())) {
                    serverPort = Integer.valueOf(nodes.item(i).getTextContent());
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            Util_msg.showError("Ошибка при обработке файла с параметрами сервера.\n" + e.getMessage()+
            "\n\n Будут применены параметры сервера по умолчанию: "+serverHostName+":"+serverPort);
//            e.printStackTrace();
            log.error("Exception while parsing xml-file with server name and port." + e);
        }
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
