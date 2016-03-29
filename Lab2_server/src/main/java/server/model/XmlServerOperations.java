package server.model;

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
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;

/**
 * Created by Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 25.02.2016.
 * Class contains methods that are working with xml on the server side.
 */
public class XmlServerOperations {

    private static String STUDENTS_XML_PATH = ".\\resources\\students.xml";
    private static String GROUPS_XML_PATH = ".\\resources\\groups.xml";

    public static final Logger log = LogManager.getLogger(XmlServerOperations.class);

    /**
     * Performs one of the actions (add / delete / update / find) depending on content of the message received
     * from client.
     * @param message xml-string received from client
     * @return xml-string with found objects for action "find", empty string for other actions.
     */
    public static synchronized String makeAction(String message) {
        try {
            Document document = getDocumentFromString(message);
            Object object = XmlServerOperations.unmarshalObject(document);

            if ("create".equals(document.getDocumentElement().getLastChild().getTextContent())) {
                addObjectToXmlFile(object, document);
                return "";
            }

            if ("delete".equals(document.getDocumentElement().getLastChild().getTextContent())) {
                deleteObjectFromXmlFile(document);
                return "";
            }

            if ("update".equals(document.getDocumentElement().getLastChild().getTextContent())) {
                updateObjectInXmlFile(object, document);
                return "";
            }

            if ("find".equals(document.getDocumentElement().getLastChild().getTextContent())) {
                return findObjectsInXmlFile(document);
            }
        }
        catch (ParserConfigurationException | SAXException | IOException | TransformerException | JAXBException e) {
            Util_msg.showError("Ошибка при обработке команды от клиента.\n"+e.getMessage());
            e.printStackTrace();
            log.error("Exception while executing action(create, delete, update, find) with message from client." + e);
        }
        return "";
    }

    /**
     * Identify type of objects in xml-document and looking for objects.
     * @param document document of xml-string from client
     * @return xml-string of found objects
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private static String findObjectsInXmlFile(Document document)
            throws ParserConfigurationException, SAXException, IOException {
        //узнаем тип искомого объекта
        if ("group".equals(document.getDocumentElement().getTagName())) {
            return findGroups(document);
        }
        if ("student".equals(document.getDocumentElement().getTagName())) {
            return findStudents(document);
        }
        log.info("Search of object(s) was performed.");
        return "";
    }

    /**
     * Looks for groups with given parameters. If already existed group matches with necessary
     * parameters it goes to the document of found results. Then method form xml-string of found groups.
     * @param document document of of request from client
     * @return xml-string of found groups
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    private static String findGroups(Document document) throws IOException, SAXException, ParserConfigurationException {
        int amountOfNullParameters = countNullParameters(document);

        String id = document.getDocumentElement().getFirstChild().getTextContent();
        String name = document.getDocumentElement().getFirstChild().getNextSibling().getTextContent();
        String facultyName = document.getDocumentElement().getFirstChild().getNextSibling().
                getNextSibling().getTextContent();

        Document documentOfXmlFile = getDocumentFromFile(GROUPS_XML_PATH);
        Document documentOfFoundResults;

        NodeList items = documentOfXmlFile.getDocumentElement().getChildNodes(); //gets all nodes
        for (int i = 0; i < items.getLength(); i++) {
            if ("group".equals(items.item(i).getNodeName())) {
                Element element = (Element) items.item(i);
                NodeList nodes = element.getChildNodes();
                int countOfMatchings = 0;

                for (int j = 0; j < nodes.getLength(); j++) { //in cycle gets every node
                    if (nodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        if ("name".equals(nodes.item(j).getNodeName()) //doesn't look up by ID
                                && name.equals(nodes.item(j).getTextContent())
                                || "facultyName".equals(nodes.item(j).getNodeName())
                                && facultyName.equals(nodes.item(j).getTextContent())) {
                            countOfMatchings++;
                        }
                    }
                }
                if (countOfMatchings < 3 - amountOfNullParameters) { //every group has only 3 parameters
                    documentOfXmlFile.getDocumentElement().removeChild(items.item(i));
                }
            }
        }
        documentOfFoundResults = documentOfXmlFile;
        log.info("Search of group(s) in xml-file was performed. Document of found group(s) was created.");
        return documentToString(documentOfFoundResults);
    }

    /**
     * Looks for students with given parameters. If already existed student matches with necessary
     * parameters he/she goes to the document of found results. Then method form xml-string of found students.
     * @param document document of request from client
     * @return xml-string of found student
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    private static String findStudents(Document document) throws IOException, SAXException, ParserConfigurationException {
        int amountOfNullParameters = countNullParameters(document);
        if (amountOfNullParameters == 4 ) {
            return documentToString(getDocumentFromFile(STUDENTS_XML_PATH)); //if request is empty, returns all existed students
        }

        String id = document.getDocumentElement().getFirstChild().getTextContent();
        String name = document.getDocumentElement().getFirstChild().getNextSibling().getTextContent();
        String groupId = document.getDocumentElement().getFirstChild().getNextSibling().
                getNextSibling().getTextContent();
        String enrollmentDate = document.getDocumentElement().getFirstChild().getNextSibling().
                getNextSibling().getNextSibling().getTextContent();

        Document documentOfXmlFile = getDocumentFromFile(STUDENTS_XML_PATH);
        Document documentOfFoundResults;

        NodeList items = documentOfXmlFile.getDocumentElement().getChildNodes(); ////gets all nodes
        for (int i = 0; i < items.getLength(); i++) {
            if ("student".equals(items.item(i).getNodeName())) {
                Element element = (Element) items.item(i);
                NodeList nodes = element.getChildNodes();
                int countOfMatchings = 0;

                for (int j = 0; j < nodes.getLength(); j++) { //в цикле выводим содержимое каждого тега внутри группы
                    if (nodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        if (("name".equals(nodes.item(j).getNodeName()) //doesn't look up by ID
                                && name.equals(nodes.item(j).getTextContent()))
                                || ("groupId".equals(nodes.item(j).getNodeName())
                                && groupId.equals(nodes.item(j).getTextContent()))
                                || ("enrollmentDate".equals(nodes.item(j).getNodeName())
                                && enrollmentDate.equals(nodes.item(j).getTextContent()))) {
                            countOfMatchings++;
                        }
                    }
                }
                if (countOfMatchings < 4 - amountOfNullParameters) { //every student has only 4 parameters
                    documentOfXmlFile.getDocumentElement().removeChild(items.item(i));
                }
            }
        }
        documentOfFoundResults = documentOfXmlFile;
        System.out.println(documentToString(documentOfFoundResults));
        log.info("Search of student(s) was performed. Document of found student(s) was created.");
        return documentToString(documentOfFoundResults);
    }

    /**
     * Counts number of parameters that wasn't specified in find-request.
     * @param document document of request from client
     * @return amount of null parameters
     */
    private static int countNullParameters(Document document) {
        int amountOfNullParameters = 0;
        NodeList items = document.getDocumentElement().getChildNodes();

        for (int i = 0; i < items.getLength(); i++) {
            if ("-1".equals(items.item(i).getTextContent()) || // value for null ID
                    "".equals(items.item(i).getTextContent()) // value for string fields of Student or Group
                    || "1970-01-01 02:00:00".equalsIgnoreCase(items.item(i).getTextContent())) { //value for Date(0)
                amountOfNullParameters++;
            }
        }
        log.info("Null parameters in find-request were counted. Amount = " + amountOfNullParameters);
        return amountOfNullParameters;
    }


    /**
     * Specifies type of object (student or group). Then adds information about object to the xml-file
     * that contains information about all similar objects.
     * @param object added object(student or group)
     * @param documentOfObject document of added object
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static void addObjectToXmlFile(Object object, Document documentOfObject) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        if ("group".equals(documentOfObject.getDocumentElement().getTagName())) {
            addGroupToXmlFile((Group) object);
            log.info("Information about group was added to corresponding xml-file.");
        }
        if ("student".equals(documentOfObject.getDocumentElement().getTagName())) {
            addStudentToXmlFile((Student) object);
            log.info("Information about student was added to corresponding xml-file.");
        }
    }

    /**
     * Adds information about student to the xml-file that contains information about all students.
     * @param student new student
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws TransformerException
     */
    public static void addStudentToXmlFile(Student student) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = db.parse(STUDENTS_XML_PATH); //создали документ файла Students

        Element root = document.getDocumentElement();
        Element elementStudent = document.createElement("student");
        root.appendChild(elementStudent);

        Element id = document.createElement("id");
        id.setTextContent(String.valueOf(student.getId()));
        Element name = document.createElement("name");
        name.setTextContent(student.getName());
        Element groupId = document.createElement("groupId");
        groupId.setTextContent(String.valueOf(student.getGroupId()));
        Element enrollmentDate = document.createElement("enrollmentDate");
        enrollmentDate.setTextContent(dateFormat.format(student.getEnrollmentDate())); //set date in appropriate way to unmarshal

        elementStudent.appendChild(id);
        elementStudent.appendChild(name);
        elementStudent.appendChild(groupId);
        elementStudent.appendChild(enrollmentDate);

        log.info("Node of student was created.");
        rewriteXmlFile(document, STUDENTS_XML_PATH);
    }

    /**
     * Adds information about group to the xml-file that contains information about all groups.
     * @param group new group
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws TransformerException
     */
    private static void addGroupToXmlFile(Group group) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = db.parse(GROUPS_XML_PATH); //создали документ файла Groups

        Element root = document.getDocumentElement();
        Element elementGroup = document.createElement("group");
        root.appendChild(elementGroup);

        Element id = document.createElement("id");
        id.setTextContent(String.valueOf(group.getId()));
        Element name = document.createElement("name");
        name.setTextContent(group.getName());
        Element facultyName = document.createElement("facultyName");
        facultyName.setTextContent(String.valueOf(group.getFacultyName()));

        elementGroup.appendChild(id);
        elementGroup.appendChild(name);
        elementGroup.appendChild(facultyName);

        log.info("Node of group was created. Group: " + group);
        rewriteXmlFile(document, GROUPS_XML_PATH);
    }

    /**
     * Specifies type of object (student or group). Then deletes information about object from the xml-file
     * that contains information about all similar objects.
     * @param document document of deleted object
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException
     */
    public static void deleteObjectFromXmlFile(Document document) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        if ("student".equals(document.getDocumentElement().getTagName())) {
            delete(document, STUDENTS_XML_PATH);
            log.info("Information about student was deleted from corresponding xml-file.");
        }
        if ("group".equals(document.getDocumentElement().getTagName())) {
            delete(document, GROUPS_XML_PATH);
            log.info("Information about group was deleted from corresponding xml-file.");
        }

    }

    /**
     * Deletes information about object from the xml-file of similar objects.
     * @param document document of deleted object
     * @param filePath path to xml-file of students or groups
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    private static void delete(Document document, String filePath)
            throws IOException, SAXException, ParserConfigurationException, TransformerException {

        Element elementId = (Element) document.getDocumentElement().getFirstChild();
        int objectId = Integer.valueOf(elementId.getTextContent()); //gets ID

        Document documentForObjects = getDocumentFromFile(filePath);

        NodeList items = documentForObjects.getDocumentElement().getChildNodes(); //gets all nodes

        for (int i = 0; i < items.getLength(); i++) { //looks for ID and deletes
            if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
                NodeList nodes = items.item(i).getChildNodes();
                for (int j = 0; j < nodes.getLength(); j++) {
                    if ("id".equals(nodes.item(j).getNodeName())
                            && (String.valueOf(objectId)).equals(nodes.item(j).getTextContent())) {
                        documentForObjects.getDocumentElement().removeChild(items.item(i));
                    }
                }
            }
        }
        log.info("Node of object was deleted.");
        rewriteXmlFile(documentForObjects, filePath);
    }

    /**
     * Specifies type of the object (student or group). Then updates information about the object in the xml-file
     * that contains information about all similar objects.
     * @param object updated object (student or group)
     * @param documentOfObject document of updated object
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static void updateObjectInXmlFile(Object object, Document documentOfObject)
            throws IOException, SAXException, ParserConfigurationException, TransformerException {

        if ("group".equals(documentOfObject.getDocumentElement().getTagName())) {
            delete(documentOfObject, GROUPS_XML_PATH);
            addGroupToXmlFile((Group) object);
            log.info("Information about group was updated from corresponding xml-file. Updated group: " + object);
        }
        if ("student".equals(documentOfObject.getDocumentElement().getTagName())) {
            delete(documentOfObject, STUDENTS_XML_PATH);
            addStudentToXmlFile((Student) object);
            log.info("Information about student was updated from corresponding xml-file. Updated student: " + object);
        }
    }

    /**
     * Creates document from xml-string received from client.
     * @param message message from client
     * @return document
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Document getDocumentFromString(String message) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(message));
        Document document = db.parse(is);

        log.info("SERVER. Document from client xml-request was formed.");
        return document;
    }

    /**
     * Creates document from xml-file.
     * @param filePath path to xml-file
     * @return document of xml-file
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static Document getDocumentFromFile(String filePath)
            throws ParserConfigurationException, IOException, SAXException {

        log.info("Xml-file of objects was parsed. Document was created.");
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filePath);
    }

    /**
     * Unmarshals object from its document. If the object is a new one, then set unique ID.
     * @param documentOfObject document of object
     * @return unmarshaled object
     * @throws JAXBException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static Object unmarshalObject(Document documentOfObject)
            throws JAXBException, IOException, SAXException, ParserConfigurationException, TransformerException {
        String GROUP_ID_PATH = ".\\resources\\groupId.xml";
        String STUD_ID_PATH = ".\\resources\\studId.xml";

        if (documentOfObject == null) throw new IllegalArgumentException("Parameter Document is null");
        Node node = documentOfObject.getDocumentElement();
        Object object = null;

        if ("group".equals(documentOfObject.getDocumentElement().getTagName())) {
            JAXBContext jaxbContext = JAXBContext.newInstance(Group.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            object = jaxbUnmarshaller.unmarshal(node);
            if (((Group) object).getId() == null) {
                ((Group) object).setId(getUniqueId(GROUP_ID_PATH)); //set ID if only id is null
            }
            log.info("Group was unmarshaled from Document. Group: " + object);
        }
        if ("student".equals(documentOfObject.getDocumentElement().getTagName())) {
            JAXBContext jaxbContext = JAXBContext.newInstance(Student.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            object = jaxbUnmarshaller.unmarshal(node);
            if (((Student) object).getId() == null) {
                ((Student) object).setId(getUniqueId(STUD_ID_PATH)); //set ID if only id is null
            }
            log.info("Student was unmarshaled from Document. Student: " + object);
        }
        return object;
    }

    /**
     * Sets unique ID to newly created object. Updates value of unique id in xml-file.
     * @param pathToId path to xml-file with unique ID
     * @return int value of unique id
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws TransformerException
     */
    private static int getUniqueId(String pathToId)
            throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pathToId);
        Element element = document.getDocumentElement();
        Integer id = Integer.parseInt(element.getTextContent());
        int newId = id + 1;
        element.setTextContent(String.valueOf((newId)));

        log.info("Unique ID was assigned to newly created object. Id: " + newId);
        rewriteXmlFile(document, pathToId);

        return id;
    }

    /**
     * Rewrites xml-file after adding new object.
     * @param document document with added object
     * @param rewrittenFilePath path to xml-file of students or groups
     * @throws TransformerException
     */
    private static void rewriteXmlFile(Document document, String rewrittenFilePath) throws TransformerException {
        Source domSource = new DOMSource(document);
        Result fileResult = new StreamResult(new File(rewrittenFilePath));
        TransformerFactory factory = TransformerFactory.newInstance();

        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //type of data in file
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //write every tag from new line
        transformer.transform(domSource, fileResult);

        log.info("Xml-file of objects was renewed. File: " + rewrittenFilePath);
    }

    /**
     * Creates xml-string from document.
     * @param document document
     * @return xml as a string
     */
    public static String documentToString(Document document) {
        DOMImplementationLS domImplementation = (DOMImplementationLS) document.getImplementation();
        LSSerializer lsSerializer = domImplementation.createLSSerializer();

        log.info("Document was converted to String.");
        return lsSerializer.writeToString(document);
    }
}