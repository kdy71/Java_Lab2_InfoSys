package client.model;

import common_model.Group;
import common_model.Student;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 11.03.2016.
 */
public interface IoInterface {

    public void saveStudent (Student student) throws JAXBException, ParserConfigurationException;

    public void saveGroup (Group group);

    public void deleteStudent();

    public void deleteGroup();

    public List<Group> readGroups();

    public List<Student> readStudents();

}
