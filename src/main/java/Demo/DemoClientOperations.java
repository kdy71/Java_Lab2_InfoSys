package Demo;

import client.model.ISClient;
import client.model.XmlClientOperations;
import common_model.Group;
import common_model.Student;
// import org.jdom2.JDOMException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Oleksandr Dudkin on 28.02.2016.
 */
public class DemoClientOperations {

    public static void main(String[] args) throws NullPointerException, IOException, JAXBException, ParserConfigurationException {

        // TODO: поправить дату в хмл,
        // todo: проверить уникальность студента перед тем как сетить айди

        //cоздали студента, перебросили хмл строку в клиент, а оттуда может вытянуть сервер
        Group group = new Group("E-91", "Econom");
        //ISClient client1 = new ISClient(XmlClientOperations.getXmlCreateObject(group));

        Student student = new Student("Ivanov", new Date(100), 1);
        //ISClient client2 = new ISClient(XmlClientOperations.getXmlCreateObject(student)); //в клиент забрасываем строку студента

        student.setId(13);
        //ISClient client3 = new ISClient(XmlClientOperations.deleteObject(student)); //в клиент забрасываем строку студента уже имеющимся айди

        group.setId(18);
        //ISClient client4 = new ISClient(XmlClientOperations.deleteObject(group));

        student.setName("Nikolay");
        ISClient client5 = new ISClient(XmlClientOperations.getXmlUpdateObject(student));

        group.setName("E-00");
        ISClient client6 = new ISClient(XmlClientOperations.getXmlUpdateObject(group));
    }
}
