package client.model;

import common_model.Group;
import common_model.Student;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

/**
 * Input / output data - for XML storage
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 11.03.2016.
 */
public class IoXML implements IoInterface {


    public void saveStudent (Student student) throws JAXBException, ParserConfigurationException {
        System.out.println("маршализация студента "+student);  // debug
        Document docStudentXml = XmlClientOperations.marshalObject(student);
//        System.out.println("XML: "+docStudentXml);  // debug

        System.out.println(" need to  ISClient.writeStringToServer(String message)... ");  // debug
    }

    public void saveGroup (Group group){

    }

    @Override
    public void deleteStudent() {

    }

    @Override
    public void deleteGroup() {

    }

    public List<Group> readGroups() {
        return null;
    }

    public List<Student> readStudents() {
        return null;
    }




}
