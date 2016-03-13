package server.model;

import common_model.Group;
import common_model.Student;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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

/**
 * Created by Oleksandr Dudkin on 25.02.2016.
 * <p>
 * БД на сервере, клиент тонкий, идет обмен сообщениями между клиентами и серверами.
 * Команды от клиента и изменения должны передаваться в виде строки, которая представляет собой хмл-элементы.
 */
public class XmlServerOperations {

    private static String STUDENTS_XML_PATH = ".\\src\\main\\java\\server\\model\\resources\\students.xml";
    private static String GROUPS_XML_PATH = ".\\src\\main\\java\\server\\model\\resources\\groups.xml";
    private static String GROUP_ID_PATH = ".\\src\\main\\java\\server\\model\\resources\\groupId.xml";
    private static String STUD_ID_PATH = ".\\src\\main\\java\\server\\model\\resources\\studId.xml";

    //основной метод. выполняет одно из действий, добавление нового объекта, удаление существующего, обновление существующего
    public static void makeAction(String message) {
        //построить документ из сообщения
        Document document = getDocumentFromString(message);
        Object object = XmlServerOperations.unmarshalObject(document);

        //если create, то анмаршалинг, присвоение ID, дозапись объекта в файл
        if (document.getDocumentElement().getLastChild().getTextContent().equals("create")) {
            addObjectToXmlFile(object, document);
        }

        //если delete, то получение ID и типа объекта, получение документа аналогичных объектов,
        // поиск в нем ID объекта и удаление этого объекта
        if (document.getDocumentElement().getLastChild().getTextContent().equals("delete")) {
            deleteObjectFromXmlFile(document);
        }

        //если update, то получение ID и типа объекта, получение документа аналогичных объектов,
        // поиск в нем ID объекта и перезапись этого объекта
        if (document.getDocumentElement().getLastChild().getTextContent().equals("update")) {
            updateObjectInXmlFile(object, document);
        }
    }

    //добавление нового объекта в ХМЛ-файл
    public static void addObjectToXmlFile(Object object, Document documentOfObject) {
        //Нужно как-то распознать что это - студент или группа
        if (documentOfObject.getDocumentElement().getTagName() == "group") {
            addGroupToXmlFile((Group) object);
        }
        if (documentOfObject.getDocumentElement().getTagName() == "student") {
            addStudentToXmlFile((Student) object);
        }
    }

    //Дозапись в ХМЛ-файл студента
    private static void addStudentToXmlFile(Student student) {
        DocumentBuilder db = null;
        Document document = null;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = db.parse(STUDENTS_XML_PATH); //создали документ файла Students

            Element root = document.getDocumentElement();
            Element elementStudent = document.createElement("student");
            root.appendChild(elementStudent);

            Element id = document.createElement("id");
            id.setTextContent(String.valueOf(student.getId()));
            Element name = document.createElement("name");
            name.setTextContent(student.getName());
            Element group = document.createElement("group");
            group.setTextContent(String.valueOf(student.getGroupId()));
            Element enrollmentDate = document.createElement("enrollmentDate");
            enrollmentDate.setTextContent(String.valueOf(student.getEnrollmentDate()));

            elementStudent.appendChild(id);
            elementStudent.appendChild(name);
            elementStudent.appendChild(group);
            elementStudent.appendChild(enrollmentDate);

            rewriteXmlFile(document, STUDENTS_XML_PATH);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Дозапись в ХМЛ-файл группу
    private static void addGroupToXmlFile(Group group) {
        DocumentBuilder db = null;
        Document document = null;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = db.parse(GROUPS_XML_PATH); //создали документ файла Groups

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

            rewriteXmlFile(document, GROUPS_XML_PATH);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //узнаем тип удаляемого объекта и удаляем
    public static void deleteObjectFromXmlFile(Document document) {
        if (document.getDocumentElement().getTagName().equals("student")) {
            delete(document, STUDENTS_XML_PATH);
        }
        if (document.getDocumentElement().getTagName().equals("group")) {
            delete(document, GROUPS_XML_PATH);
        }
    }

    //удаляем объект
    private static void delete(Document document, String filePath) {
        int objectId = -1;
        Element elementId = (Element) document.getDocumentElement().getFirstChild(); //узнаем айди объекта
        objectId = Integer.valueOf(elementId.getTextContent());

        Document documentForObjects = getDocumentFromFile(filePath);

        NodeList items = documentForObjects.getDocumentElement().getChildNodes(); //список нод под корневым элементом

        for (int i = 0; i < items.getLength(); i++) {       //поиск в нем ID объекта и удаление этого объекта
            if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
                NodeList nodes = items.item(i).getChildNodes();
                for (int j = 0; j < nodes.getLength(); j++) {
                    if (nodes.item(j).getNodeName().equals("id") && nodes.item(j).getTextContent().equals(String.valueOf(objectId))) {
                        documentForObjects.getDocumentElement().removeChild(items.item(i));
                        //addObjectToXmlFile(document, filePath);
                    }
                }
            }
        }
        rewriteXmlFile(documentForObjects, filePath);//перезапись Документа в файл
    }

    //узнаем тип обновляемого объекта и перезаписываем
    public static void updateObjectInXmlFile(Object object, Document document) {
        if (document.getDocumentElement().getTagName().equals("group")) {
            delete(document, GROUPS_XML_PATH);
            addGroupToXmlFile((Group) object);
        }
        if (document.getDocumentElement().getTagName().equals("student")) {
            delete(document, STUDENTS_XML_PATH);
            addStudentToXmlFile((Student) object);
        }
    }

    //создает Документ из message
    public static Document getDocumentFromString(String message) {
        DocumentBuilder db = null;
        Document document = null;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(message));
            document = db.parse(is);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    //создает Документ из файла
    public static Document getDocumentFromFile(String filePath) {
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filePath);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return document;
    }

    //воссоздаем новый объект (студент или группа) и если ID = 0, то сетим уникальный ID
    public static Object unmarshalObject(Document document) {
        if (document == null) throw new IllegalArgumentException("Parameter Document is null");
        Node node = document.getDocumentElement();
        Object object = null;
        try {
            if (document.getDocumentElement().getTagName() == "group") {
                JAXBContext jaxbContext = JAXBContext.newInstance(Group.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                object = jaxbUnmarshaller.unmarshal(node);
                if (((Group) object).getId() == 0) {
                    ((Group) object).setId(getUniqueId(GROUP_ID_PATH)); //присвоение ID только если он равен 0 - объект новый
                }
            }
            if (document.getDocumentElement().getTagName() == "student") {
                JAXBContext jaxbContext = JAXBContext.newInstance(Student.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                object = jaxbUnmarshaller.unmarshal(node);
                if (((Student) object).getId() == 0) {
                    ((Student) object).setId(getUniqueId(STUD_ID_PATH)); //присвоение ID только если он равен 0 - объект новый
                }
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return object;
    }

    // присвоить объекту уникальный ID
    private static int getUniqueId(String pathToId) {
        Integer id = -1;
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pathToId);
            Element element = document.getDocumentElement();
            id = Integer.parseInt(element.getTextContent());
            int newId = id + 1;
            element.setTextContent(String.valueOf((newId)));
            rewriteXmlFile(document, pathToId);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return id;
    }

    //перезапись xml-файла после изменений в Документе
    private static void rewriteXmlFile(Document document, String rewrittenFilePath) {
        Source domSource = new DOMSource(document);
        Result fileResult = new StreamResult(new File(rewrittenFilePath));
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, fileResult);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void printStudent(Node node) {
        NodeList nodes = node.getChildNodes(); //создаем список дочерних узлов

        for (int i = 0; i < nodes.getLength(); i++) { //в цикле выводим содержимое каждого тега внутри хобби
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                System.out.println("" + nodes.item(i).getNodeName() + ": " + //печатаем имя и...
                        nodes.item(i).getFirstChild().getNodeValue()); //значение тега
            }
        }
        System.out.println("");
    }

    private static Document marshalStudent(Student student) {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        Document documentStud = null;
        JAXBContext context = null;
        Marshaller marshaller = null;
        try {
            documentStud = docBuilderFactory.newDocumentBuilder().newDocument();
            context = JAXBContext.newInstance(Student.class);
            marshaller = context.createMarshaller();
            marshaller.marshal(student, documentStud);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return documentStud;
    }
}