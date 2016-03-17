package client.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Oleksandr Dudkin on 25.02.2016.
 */
public class XmlClientOperations {

    //cоздание нового объекта, который будет дозаписан на сервере в хмл-файл
    public static String createObject(Object object) {
        return modifyObject(object, "create");
    }

    //обновление/перезапись уже существующего на сервере объекта
    public static String updateObject(Object object) {
        return modifyObject(object, "update");
    }

    //удаление существующего на сервере объекта
    public static String deleteObject(Object object) {
        return modifyObject(object, "delete");
    }

    //модификация объекта, маршалинг, добавление ноды action, перевод в строку
    public static String modifyObject(Object object, String actionMessage) {
        Document document = marshalObject(object);
        addActionNode(document, actionMessage);
        return documentToString(document);
    }

    //маршаллизация объекта в строку
    public static Document marshalObject(Object object) {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            document = docBuilderFactory.newDocumentBuilder().newDocument();
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(object, document);
            // TODO: 15.03.2016   Саня, ты тут не обработаешь исключения. Выбрасывай их выше.
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return document;
    }

    //добавление ноды действия
    public static void addActionNode(Document document, String actionMessage) {
        Element root = document.getDocumentElement();
        Element action = document.createElement("action");
        root.appendChild(action);
        action.setTextContent(actionMessage);
    }

    //перевод хмл-документа в строку
    public static String documentToString(Document document) {
        DOMImplementationLS domImplementation = (DOMImplementationLS) document.getImplementation();
        LSSerializer lsSerializer = domImplementation.createLSSerializer();
        return lsSerializer.writeToString(document);
    }

    /**
     * Парсит сообщение с сервера и делает из него объект (список студентов, список групп, команда..? )
     * Вызывается из  Main_Client. stringXML_2Obj
     * @param msgXML
     * @return
     */
    public Object parseServerMsg_2Obj(String msgXML){
        // TODO: 16.03.2016 Создать из строки XML список студентов или групп
        // затем admin. replaceAllStudents(List<Student> newStudents)
        // затем StudentsTableModel. refreshGrid()
        // вопрос как связать классы
        return null;
    }
}


