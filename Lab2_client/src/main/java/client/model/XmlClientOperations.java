package client.model;

import common_model.Group;
import common_model.Student;
import common_model.Util_msg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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

//import org.apache.log4j.Logger;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 25.02.2016.
 * Class contains methods that are working with xml on the client side.
 */
public class XmlClientOperations {

    public static final Logger log = LogManager.getLogger(XmlClientOperations.class);

    /**
     * Forms xml-string for request to find group(s) with specified parameters.
     * @param id    group id
     * @param name  group name
     * @param facultyName   faculty name
     * @return xml recorded as a string
     */
    public static String findGroups(Integer id, String name, String facultyName) {
        Group group = new Group();
        group.setId(id);
        group.setName(name);
        group.setFacultyName(facultyName);
        String modifiedXml = null;

        if (id == null) {
            group.setId(-1); //can't set null, so insert int -1
        }
        if (name == null) {
            group.setName("");
        }
        if (facultyName == null) {
            group.setFacultyName(""); //can't set null, so insert ""
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

    /**
     * Forms xml-string for request to find student(s) with specified parameters.
     * @param id    student id
     * @param name  student name
     * @param groupId   id of student group
     * @param enrollmentDate enrollment date of student
     * @return xml recorded as a string
     */
    public static String findStudents(Integer id, String name, Integer groupId, Date enrollmentDate) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setGroupId(groupId);
        student.setEnrollmentDate(enrollmentDate);
        String modifiedXml = null;

        if (id == null) {
            student.setId(-1); //can't set null, so insert int -1
        }
        if (name == null) {
            student.setName(""); //can't set null, so insert ""
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

    /**
     * Forms xml-string for request to create new object (student or group).
     * @param object newly created object (Student or Group)
     * @return xml recorded as a string
     */
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

    /**
     * Forms xml-string for request to update already existing object (student or group).
     * @param object updated object (Student or Group)
     * @return xml recorded as a string
     */
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

    /**
     * Forms xml-string for request to delete already existing object (student or group).
     * @param object object to delete (Student or Group)
     * @return xml recorded as a string
     */
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

    /**
     * Modifies xml description for object by appending node with certain action (create | update | delete).
     * @param object object (Student or Group)
     * @param actionMessage action with object
     * @return modified xml as a string
     * @throws JAXBException
     * @throws ParserConfigurationException
     */
    private static String modifyObject(Object object, String actionMessage) throws JAXBException, ParserConfigurationException {
        Document document = marshalObject(object);
        addActionNode(document, actionMessage);
        log.info("End of modification.");
        return documentToString(document);
    }

    /**
     * Marshals object (student or group) to Document.
     * @param object student or group
     * @return document of object's xml-description
     * @throws ParserConfigurationException
     * @throws JAXBException
     */
    private static Document marshalObject(Object object) throws ParserConfigurationException, JAXBException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        Document document = docBuilderFactory.newDocumentBuilder().newDocument();
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(object, document);

        log.info("Object was marshaled to document: " + object.toString());
        return document;
    }

    /**
     * Adds to document of object's xml-description new node that contains certain action.
     * @param document document of object's xml-description
     * @param actionMessage action with object
     */
    private static void addActionNode(Document document, String actionMessage) {
        Element root = document.getDocumentElement();
        Element action = document.createElement("action");
        root.appendChild(action);
        action.setTextContent(actionMessage);
        log.info("Node of action was added. Action: " + actionMessage);
    }

    /**
     * Converts document of object's xml-description to string.
     * @param document document of object's xml-description
     * @return xml as a string
     */
    private static String documentToString(Document document) {
        DOMImplementationLS domImplementation = (DOMImplementationLS) document.getImplementation();
        LSSerializer lsSerializer = domImplementation.createLSSerializer();
        log.info("Document of object was converted to String.");
        return lsSerializer.writeToString(document);
    }

    /**
     * Parses xml-string received from server, gets type of objects, create list of received objects.
     * @param messageFromServer xml-string from server
     * @return list of objects
     * @see class Main_Client
     */
    public static List parseServerMessageToObjects(String messageFromServer) {
        List foundObjects = new ArrayList();

        try {
            Document document = getDocumentFromString(messageFromServer);

            // in cycle gets every object and adds to list
            if ("students".equals(document.getDocumentElement().getTagName())) {
                NodeList items = document.getDocumentElement().getChildNodes(); //gets set of nodes
                for (int i = 0; i < items.getLength(); i++) {
                    if (items.item(i).getNodeName().equals("student")) { //if node is a student
                        JAXBContext jaxbContext = JAXBContext.newInstance(Student.class);
                        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                        Student student = (Student) jaxbUnmarshaller.unmarshal(items.item(i));
                        foundObjects.add(student);
                    }
                }
            }
            if ("groups".equals(document.getDocumentElement().getTagName())) {
                NodeList items = document.getDocumentElement().getChildNodes(); //gets set of nodes
                for (int i = 0; i < items.getLength(); i++) {
                    if (items.item(i).getNodeName().equals("group")) { //if node is a group
                        JAXBContext jaxbContext = JAXBContext.newInstance(Group.class);
                        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                        Group group = (Group) jaxbUnmarshaller.unmarshal(items.item(i));
                        foundObjects.add(group);
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

    /**
     * Creates Document from xml-string received from server.
     * @param message xml-string received from server
     * @return document
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private static Document getDocumentFromString(String message)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(message));
        Document document = db.parse(is);

        log.info("CLIENT. Document from string was created.");
        return document;
    }
}


