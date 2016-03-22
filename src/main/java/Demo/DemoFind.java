package Demo;

import client.model.XmlClientOperations;
import common_model.Student;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import server.model.XmlServerOperations;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Oleksandr Dudkin on 16.03.2016.
 */
public class DemoFind {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, JAXBException, TransformerException {

        //закидываем в клиент
        //ISClient client = new ISClient(XmlClientOperations.findGroups(null, "E-91", "Econom"));

        //ISClient client2 = new ISClient(XmlClientOperations.findStudents(null, "Ivanov", 1, null)); //todo разобраться с датой, как ее обработать

        Student student = new Student("Vasya", new Date(0), 1);
        System.out.println(student);
        String s = XmlClientOperations.getXmlCreateObject(student);
        System.out.println(s);

        Document document = XmlServerOperations.getDocumentFromString(s);
        Student student1 = (Student) XmlServerOperations.unmarshalObject(document);

        System.out.println(student1);
    }
}

