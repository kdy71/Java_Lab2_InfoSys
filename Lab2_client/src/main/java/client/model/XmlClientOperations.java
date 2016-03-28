package client.model;

import common_model.Group;
import common_model.Student;
import common_model.Util_msg;
//import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Oleksandr Dudkin on 25.02.2016.
 */
public class XmlClientOperations {

    //public static final Logger log = Logger.getLogger(XmlClientOperations.class);
    public static final Logger log = LogManager.getLogger(XmlClientOperations.class);

    //сформировать запрос на поиск группы в виде строки из ХМЛ-документа
    public static String findGroups(Integer id, String name, String facultyName) {
        Group group = new Group();
        group.setId(id);
        group.setName(name);
        group.setFacultyName(facultyName);
        String modifiedXml = null;

        if (id == null) {
            group.setId(-1);
        } //null не вставляется, чтобы был тег, нужно вставить что-то
        if (name == null) {
            group.setName("");
        }
        if (facultyName == null) {
            group.setFacultyName("");
        }

        try {
            modifiedXml = modifyObject(group, "find");
        } catch (JAXBException | ParserConfigurationException e) {
            Util_msg.showError("Ошибка при формировании запроса на поиск группы.\n" + e.getMessage());
            e.printStackTrace();
            log.error("Exception while forming find-request for group." + e);
        }
        log.info("End of formation xml-string request for finding groups.");
        return modifiedXml;
    }

    //сформировать запрос на поиск студента в виде строки из ХМЛ-документа
    public static String findStudents(Integer id, String name, Integer groupId, Date enrollmentDate) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setGroupId(groupId);
        student.setEnrollmentDate(enrollmentDate);
        String modifiedXml = null;

        if (id == null) {
            student.setId(-1);
        } //null не вставляется, чтобы был тег, нужно вставить что-то
        if (name == null) {
            student.setName("");
        }
        if (groupId == null) {
            student.setGroupId(-1);
        }
        if (enrollmentDate == null) {
            student.setEnrollmentDate(new Date(0));
        }

        try {
            modifiedXml = modifyObject(student, "find");
        } catch (JAXBException | ParserConfigurationException e) {
            Util_msg.showError("Ошибка при формировании запроса на поиск студента.\n" + e.getMessage());
            e.printStackTrace();
            log.error("Exception while forming find-request for student." + e);
        }
        log.info("End of formation xml-string request for finding students.");
        return modifiedXml;
    }

    //cоздание нового объекта, который будет дозаписан на сервере в хмл-файл
    public static String getXmlCreateObject(Object object) {
        String createdXml = null;
        try {
            createdXml = modifyObject(object, "create");
        } catch (JAXBException | ParserConfigurationException e) {
            Util_msg.showError("Ошибка при формировании запроса на создание записи объекта.\n" + e.getMessage());
            e.printStackTrace();
            log.error("Exception while forming create-request for object." + e);
        }
        log.info("Xml for new object was created. Object: " + object.toString());
        return createdXml;
    }

    //обновление/перезапись уже существующего на сервере объекта
    public static String getXmlUpdateObject(Object object) {
        String updatedXml = null;
        try {
            updatedXml = modifyObject(object, "update");
        } catch (JAXBException | ParserConfigurationException e) {
            Util_msg.showError("Ошибка при формировании запроса на обновление записи объекта.\n" + e.getMessage());
            e.printStackTrace();
            log.error("Exception while forming create-request for object." + e);
        }
        log.info("Xml for updated object was created. Object: " + object.toString());
        return updatedXml;
    }

    //удаление существующего на сервере объекта
    public static String getXmlDeleteObject(Object object) {
        String deletedXml = null;
        try {
            deletedXml = modifyObject(object, "delete");
        } catch (JAXBException | ParserConfigurationException e) {
            Util_msg.showError("Ошибка при формировании запроса на удаление записи объекта.\n" + e.getMessage());
            e.printStackTrace();
            log.error("Exception while forming delete-request for object." + e);
        }
        log.info("Xml for deleted object was created. Object: " + object.toString());
        return deletedXml;
    }

    //модификация объекта, маршалинг, добавление ноды action, перевод в строку
    public static String modifyObject(Object object, String actionMessage) throws JAXBException, ParserConfigurationException {
        Document document = marshalObject(object);
        addActionNode(document, actionMessage);
        log.info("End of modification.");
        return documentToString(document);
    }

    //маршаллизация объекта в документ
    public static Document marshalObject(Object object) throws ParserConfigurationException, JAXBException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        Document document = docBuilderFactory.newDocumentBuilder().newDocument();
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(object, document);

        log.info("Object was marshaled to document: " + object.toString());
        return document;
    }

    //добавление ноды действия
    public static void addActionNode(Document document, String actionMessage) {
        Element root = document.getDocumentElement();
        Element action = document.createElement("action");
        root.appendChild(action);
        action.setTextContent(actionMessage);
        log.info("Node of action was added. Action: " + actionMessage);
    }

    //перевод хмл-документа в строку
    public static String documentToString(Document document) {
        DOMImplementationLS domImplementation = (DOMImplementationLS) document.getImplementation();
        LSSerializer lsSerializer = domImplementation.createLSSerializer();
        log.info("Document of object was converted to String.");
        return lsSerializer.writeToString(document);
    }

    /**
     * Парсит сообщение с сервера и делает из него объект (список студентов, список групп, команда..? )
     * Вызывается из  Main_Client.parseServerMessageToObjects()
     *
     * @param messageFromServer
     * @return
     */
    public static List parseServerMessageToObjects(String messageFromServer) {
        //создать документ
//        List foundObjects = new LinkedList();
        List foundObjects = new ArrayList();

        try {
            Document document = getDocumentFromString(messageFromServer);

            //пропарсить документ, узнать тип объектов
            if ("students".equals(document.getDocumentElement().getTagName())) {
                //в цикле вытащить каждый объект анмаршалингом и добавить в список
                NodeList items = document.getDocumentElement().getChildNodes(); //cоздаем набор нод
                for (int i = 0; i < items.getLength(); i++) {
                    if (items.item(i).getNodeName().equals("student")) { //если нода - это студент

                        JAXBContext jaxbContext = null;

                        jaxbContext = JAXBContext.newInstance(Student.class);
                        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                        Student student = (Student) jaxbUnmarshaller.unmarshal(items.item(i));
                        //System.out.println(student);
                        foundObjects.add(student);
                        //log.info("Object Student was added to list.");
                    }
                }
            }
            if ("groups".equals(document.getDocumentElement().getTagName())) {
                //в цикле вытащить каждый объект анмаршалингом и добавить в список
                NodeList items = document.getDocumentElement().getChildNodes(); //cоздаем набор нод
                for (int i = 0; i < items.getLength(); i++) {
                    if (items.item(i).getNodeName().equals("group")) { //если нода - это студент
                        JAXBContext jaxbContext = null;

                        jaxbContext = JAXBContext.newInstance(Group.class);
                        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                        Group group = (Group) jaxbUnmarshaller.unmarshal(items.item(i));
                        //System.out.println(group);
                        foundObjects.add(group);
                        //log.info("Object Group was added to list.");
                    }
                }
            }
        } catch (JAXBException | SAXException | ParserConfigurationException | IOException e) {
            Util_msg.showError("Ошибка при обработке сообщения от сервера.\n" + e.getMessage());
            e.printStackTrace();
            log.error("Exception while parsing message from server." + e);
        }
        log.info("Message from server was parsed, list of objects was created: " + foundObjects);
        return foundObjects;
    }

    //получить документ из строки
    public static Document getDocumentFromString(String message)
            throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilder db = null;
        Document document = null;

        db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(message));
        document = db.parse(is);

        log.info("CLIENT. Document from string was created.");
        return document;
    }

    //воссоздаем новый объект
    public static Object unmarshalObject(Document document) {
        if (document == null) throw new IllegalArgumentException("Parameter Document is null");
        Node node = document.getDocumentElement();
        Object object = null;
        try {
            if ("group".equals(document.getDocumentElement().getTagName())) {
                JAXBContext jaxbContext = JAXBContext.newInstance(Group.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                object = jaxbUnmarshaller.unmarshal(node);
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return object;
    }
}


