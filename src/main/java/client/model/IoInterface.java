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

    public void deleteStudent(int id4Del);

    public void deleteGroup(int id4Del);

    /**
     * Посылает на сервер запрос на выборку групп
     * Параметр - шаблон для условий выборки по атрибутам (если атрибут != null - значит по нему будет фильтр)
     * @param templateGroup
     * @return
     */
    public List<Group> selectGroups(Group templateGroup);


    /**
     * Посылает на сервер запрос на выборку студентов
     * Параметр - шаблон для условий выборки по атрибутам (если атрибут != null - значит по нему будет фильтр)
     * @param templateStudent
     * @return
     */
    public List<Student> selectStudents(Student templateStudent);


}
