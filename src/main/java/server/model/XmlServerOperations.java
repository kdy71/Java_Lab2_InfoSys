package server.model;

import common_model.Group;
import common_model.Student;
import common_model.Util;
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

/**
 * Created by Oleksandr Dudkin on 25.02.2016.
 * <p>
 * БД на сервере, клиент тонкий, идет обмен сообщениями между клиентами и серверами.
 * Команды от клиента и изменения должны передаваться в виде строки, которая представляет собой хмл-элементы.
 */
public class XmlServerOperations {

    private static String STUDENTS_XML_PATH = ".\\src\\main\\java\\server\\model\\resources\\students.xml";
    private static String GROUPS_XML_PATH = ".\\src\\main\\java\\server\\model\\resources\\groups.xml";

    //основной метод. выполняет одно из действий, добавление нового объекта, удаление существующего, обновление существующего
    public static String makeAction(String message) {
        try {

            //построить документ из сообщения
            Document document = getDocumentFromString(message);
            Object object = XmlServerOperations.unmarshalObject(document);

            //если create, то анмаршалинг, присвоение ID, дозапись объекта в файл
            if ("create".equals(document.getDocumentElement().getLastChild().getTextContent())) {
                addObjectToXmlFile(object, document);
                return "";
            }

            //если delete, то получение ID и типа объекта, получение документа аналогичных объектов,
            // поиск в нем ID объекта и удаление этого объекта
            if ("delete".equals(document.getDocumentElement().getLastChild().getTextContent())) {
                deleteObjectFromXmlFile(document);
                return "";
            }

            //если update, то получение ID и типа объекта, получение документа аналогичных объектов,
            // поиск в нем ID объекта и перезапись этого объекта
            if ("update".equals(document.getDocumentElement().getLastChild().getTextContent())) {
                updateObjectInXmlFile(object, document);
                return "";
            }

            if ("find".equals(document.getDocumentElement().getLastChild().getTextContent())) {
                return findObjectsInXmlFile(document);
            }
        } catch (ParserConfigurationException e) {
            Util.showError("");
            e.printStackTrace();
        } catch (SAXException e) {
            Util.showError("");
            e.printStackTrace();
        } catch (IOException e) {
            Util.showError("");
            e.printStackTrace();
        } catch (TransformerException e) {
            Util.showError("");
            e.printStackTrace();
        } catch (JAXBException e) {
            Util.showError("");
            e.printStackTrace();
        }
        return "";
    }


    //Узнает тип искомого объекта и ищет объекты с параметрами из запроса
    private static String findObjectsInXmlFile(Document document) throws ParserConfigurationException, SAXException, IOException {
        //узнаем тип искомого объекта
        if ("group".equals(document.getDocumentElement().getTagName())) {
            return findGroups(document);
        }
        if ("student".equals(document.getDocumentElement().getTagName())) {
            return findStudents(document);
        }
        return "";
    }

    //поиск группы
    private static String findGroups(Document document) throws IOException, SAXException, ParserConfigurationException {
        int amountOfNullParameters = countNullParameters(document);

        //извлекаем параметры из документа объекта
        String id = document.getDocumentElement().getFirstChild().getTextContent();
        String name = document.getDocumentElement().getFirstChild().getNextSibling().getTextContent();
        String facultyName = document.getDocumentElement().getFirstChild().getNextSibling().
                getNextSibling().getTextContent();

        //делаем дом файлa с объектами и дом результатов поиска
        Document documentOfXmlFile = getDocumentFromFile(GROUPS_XML_PATH);
        Document documentOfFoundResults;

        //ищем в доме нужные параметры
        NodeList items = documentOfXmlFile.getDocumentElement().getChildNodes(); //достаем корневой элемент, а от него все дочерние

        for (int i = 0; i < items.getLength(); i++) {
            if ("group".equals(items.item(i).getNodeName())) { // для всех нод обеспечиваем вывод информации
                Element element = (Element) items.item(i); // создаем из йтема объект типа элемент и...
                NodeList nodes = element.getChildNodes(); //создаем список дочерних узлов в каждом студенте/группе
                int countOfMatchings = 0;

                for (int j = 0; j < nodes.getLength(); j++) { //в цикле выводим содержимое каждого тега внутри группы
                    if (nodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        //сравниваем значения и если не выполняется то удаляем из дом (не ищем по ID!!)
                        if ("name".equals(nodes.item(j).getNodeName())
                                && name.equals(nodes.item(j).getTextContent())
                                || "facultyName".equals(nodes.item(j).getNodeName())
                                && facultyName.equals(nodes.item(j).getTextContent())) {
                            countOfMatchings++;
                        }
                    }
                }
                if (countOfMatchings < 3 - amountOfNullParameters) { //3 параметра у группы
                    documentOfXmlFile.getDocumentElement().removeChild(items.item(i));
                }
            }
        }
        documentOfFoundResults = documentOfXmlFile;
        System.out.println(documentToString(documentOfFoundResults));
        return documentToString(documentOfFoundResults);
    }

    //поиск студента
    private static String findStudents(Document document) throws IOException, SAXException, ParserConfigurationException {
        int amountOfNullParameters = countNullParameters(document);

        //извлекаем параметры из документа объекта
        String id = document.getDocumentElement().getFirstChild().getTextContent();
        String name = document.getDocumentElement().getFirstChild().getNextSibling().getTextContent();
        String groupId = document.getDocumentElement().getFirstChild().getNextSibling().
                getNextSibling().getTextContent();
        String enrollmentDate = document.getDocumentElement().getFirstChild().getNextSibling().
                getNextSibling().getNextSibling().getTextContent();

        //делаем дом файлa с объектами и дом результатов поиска
        Document documentOfXmlFile = getDocumentFromFile(STUDENTS_XML_PATH);
        Document documentOfFoundResults;

        //ищем в доме нужные параметры
        NodeList items = documentOfXmlFile.getDocumentElement().getChildNodes(); //достаем корневой элемент, а от него все дочерние

        for (int i = 0; i < items.getLength(); i++) {
            if ("student".equals(items.item(i).getNodeName())) { // для всех нод обеспечиваем вывод информации
                Element element = (Element) items.item(i); // создаем из йтема объект типа элемент и...
                NodeList nodes = element.getChildNodes(); //создаем список дочерних узлов в каждом студенте/группе
                int countOfMatchings = 0;

                for (int j = 0; j < nodes.getLength(); j++) { //в цикле выводим содержимое каждого тега внутри группы
                    if (nodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                        //сравниваем значения и если не выполняется то удаляем из дом (не ищем по ID!!)
                        if ("name".equals(nodes.item(j).getNodeName())
                                && name.equals(nodes.item(j).getTextContent())
                                || "groupId".equals(nodes.item(j).getNodeName())
                                && groupId.equals(nodes.item(j).getTextContent())
                                || "enrollmentDate".equals(nodes.item(j).getNodeName())
                                && enrollmentDate.equals(nodes.item(j).getTextContent())) {
                            countOfMatchings++;
                        }
                    }
                }
                if (countOfMatchings < 4 - amountOfNullParameters) { //4 параметра у студента
                    documentOfXmlFile.getDocumentElement().removeChild(items.item(i));
                }
            }
        }
        documentOfFoundResults = documentOfXmlFile;
        System.out.println(documentToString(documentOfFoundResults));
        return documentToString(documentOfFoundResults);
    }

    //cчитает параменты поиска, которые не были заданны на клиенте
    private static int countNullParameters(Document document) {
        int amountOfNullParameters = 0;
        NodeList items = document.getDocumentElement().getChildNodes();

        for (int i = 0; i < items.getLength(); i++) {
            if ("-1".equals(items.item(i).getTextContent()) ||
                    "".equals(items.item(i).getTextContent())
                    || "1970-01-01T02:00:00+02:00".equalsIgnoreCase(items.item(i).getTextContent())) { //значение для Date(0)
                amountOfNullParameters++;
            }
        }
        return amountOfNullParameters;
    }

    //добавление нового объекта в ХМЛ-файл
    public static void addObjectToXmlFile(Object object, Document documentOfObject) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        //Нужно распознать что это - студент или группа
        if ("group".equals(documentOfObject.getDocumentElement().getTagName())) {
            addGroupToXmlFile((Group) object);
        }
        if ("student".equals(documentOfObject.getDocumentElement().getTagName())) {
            addStudentToXmlFile((Student) object);
        }
    }

    //Дозапись в ХМЛ-файл студента
    private static void addStudentToXmlFile(Student student) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilder db = null;
        Document document = null;

        db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        document = db.parse(STUDENTS_XML_PATH); //создали документ файла Students

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
        enrollmentDate.setTextContent(String.valueOf(student.getEnrollmentDate()));

        elementStudent.appendChild(id);
        elementStudent.appendChild(name);
        elementStudent.appendChild(groupId);
        elementStudent.appendChild(enrollmentDate);

        rewriteXmlFile(document, STUDENTS_XML_PATH);
    }

    //Дозапись в ХМЛ-файл группу
    private static void addGroupToXmlFile(Group group) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilder db = null;
        Document document = null;

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
    }

    //узнаем тип удаляемого объекта и удаляем
    public static void deleteObjectFromXmlFile(Document document) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        if ("student".equals(document.getDocumentElement().getTagName())) {
            delete(document, STUDENTS_XML_PATH);
        }
        if ("group".equals(document.getDocumentElement().getTagName())) {
            delete(document, GROUPS_XML_PATH);
        }
    }

    //удаляем объект
    private static void delete(Document document, String filePath) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        int objectId = -1;
        Element elementId = (Element) document.getDocumentElement().getFirstChild(); //узнаем айди объекта
        objectId = Integer.valueOf(elementId.getTextContent());

        Document documentForObjects = getDocumentFromFile(filePath);

        NodeList items = documentForObjects.getDocumentElement().getChildNodes(); //список нод под корневым элементом

        for (int i = 0; i < items.getLength(); i++) {       //поиск в нем ID объекта и удаление этого объекта
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
        rewriteXmlFile(documentForObjects, filePath);//перезапись Документа в файл
    }

    //узнаем тип обновляемого объекта и перезаписываем
    public static void updateObjectInXmlFile(Object object, Document document) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        if ("group".equals(document.getDocumentElement().getTagName())) {
            delete(document, GROUPS_XML_PATH);
            addGroupToXmlFile((Group) object);
        }
        if ("student".equals(document.getDocumentElement().getTagName())) {
            delete(document, STUDENTS_XML_PATH);
            addStudentToXmlFile((Student) object);
        }
    }

    //создает Документ из message
    public static Document getDocumentFromString(String message) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder db = null;
        Document document = null;

        db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(message));
        document = db.parse(is);

        return document;
    }

    //создает Документ из файла
    public static Document getDocumentFromFile(String filePath)
            throws ParserConfigurationException, IOException, SAXException {

        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filePath);
    }

    //воссоздаем новый объект (студент или группа) и если ID = 0, то сетим уникальный ID
    public static Object unmarshalObject(Document document) throws JAXBException, IOException, SAXException, ParserConfigurationException, TransformerException {
        String GROUP_ID_PATH = ".\\src\\main\\java\\server\\model\\resources\\groupId.xml";
        String STUD_ID_PATH = ".\\src\\main\\java\\server\\model\\resources\\studId.xml";

        if (document == null) throw new IllegalArgumentException("Parameter Document is null");
        Node node = document.getDocumentElement();
        Object object = null;

        if ("group".equals(document.getDocumentElement().getTagName())) {
            JAXBContext jaxbContext = JAXBContext.newInstance(Group.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            object = jaxbUnmarshaller.unmarshal(node);
            if (((Group) object).getId() != null && ((Group) object).getId() == 0) {
                ((Group) object).setId(getUniqueId(GROUP_ID_PATH)); //присвоение ID только если он равен 0 - объект новый
            }
        }
        if ("student".equals(document.getDocumentElement().getTagName())) {
            JAXBContext jaxbContext = JAXBContext.newInstance(Student.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            object = jaxbUnmarshaller.unmarshal(node);
            if (((Student) object).getId() != null && ((Student) object).getId() == 0) {
                ((Student) object).setId(getUniqueId(STUD_ID_PATH)); //присвоение ID только если он равен 0, т.е. объект новый
            }
        }

        return object;
    }

    // присвоить объекту уникальный ID
    private static int getUniqueId(String pathToId) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Integer id = -1;

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pathToId);
        Element element = document.getDocumentElement();
        id = Integer.parseInt(element.getTextContent());
        int newId = id + 1;
        element.setTextContent(String.valueOf((newId)));

        rewriteXmlFile(document, pathToId);

        return id;
    }

    //перезапись xml-файла после изменений в Документе
    private static void rewriteXmlFile(Document document, String rewrittenFilePath) throws TransformerException {
        Source domSource = new DOMSource(document);
        Result fileResult = new StreamResult(new File(rewrittenFilePath));
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;

        transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(domSource, fileResult);
    }

/*    private static void printStudent(Node node) {
        NodeList nodes = node.getChildNodes(); //создаем список дочерних узлов

        for (int i = 0; i < nodes.getLength(); i++) { //в цикле выводим содержимое каждого тега внутри хобби
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                System.out.println("" + nodes.item(i).getNodeName() + ": " + //печатаем имя и...
                        nodes.item(i).getFirstChild().getNodeValue()); //значение тега
            }
        }
        System.out.println("");
    }*/

/*    private static Document marshalStudent(Student student) throws ParserConfigurationException, JAXBException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        Document documentStud = null;
        JAXBContext context = null;
        Marshaller marshaller = null;

        documentStud = docBuilderFactory.newDocumentBuilder().newDocument();
        context = JAXBContext.newInstance(Student.class);
        marshaller = context.createMarshaller();
        marshaller.marshal(student, documentStud);
        return documentStud;
    }*/

    //перевод хмл-документа в строку
    public static String documentToString(Document document) {
        DOMImplementationLS domImplementation = (DOMImplementationLS) document.getImplementation();
        LSSerializer lsSerializer = domImplementation.createLSSerializer();
        return lsSerializer.writeToString(document);
    }
}