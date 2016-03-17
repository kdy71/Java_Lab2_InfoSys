package client.model;

import common_model.AdminInterface;
import common_model.Group;
import common_model.Student;
import common_model.Util;
import org.w3c.dom.Document;
//import XmlClientOperations;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;

/**
 * Реализация интерфейса IoInterface для варианта хранения данных в XML
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 11.03.2016.
 */
public class IoXML implements IoInterface {

    private XmlClientOperations cliOpers = new XmlClientOperations();
    private AdminInterface adm;

    public IoXML(AdminInterface adm) {
        this.adm = adm;
    }

    /**
     * Посылает на сервер команду "сохранить студента"
     * Если id студента пустой - команда Insert, иначе - Update
     * @param student - студент, которого будем записывать
     * @throws JAXBException
     * @throws ParserConfigurationException
     */
    public void saveStudent (Student student) /*throws JAXBException, ParserConfigurationException */ {
        System.out.println("маршализация и отправка на сервер студента "+student);  // debug
        cliOpers.updateObject(student);
//        Document docStudentXml = XmlClientOperations.marshalObject(student);
//        System.out.println("XML: "+docStudentXml);  // debug
//        System.out.println(" need to  ISClient.writeStringToServer(String message)... ");  // debug
    }


    public void saveGroup (Group group){
        System.out.println("маршализация и отправка на сервер группы "+group);  // debug
        cliOpers.updateObject(group);
    }


    @Override
    public void deleteStudent(int id4Del) {
        System.out.println("маршализация и отправка на сервер команды удаления студента  id4Del= "+id4Del);  // debug
        Student stud4Del = adm.getStudentById(id4Del);
//        System.out.println("удаляемый студент  = "+stud4Del);  // debug
//        System.out.println("adm.getAllStudents().size()= "+adm.getAllStudents().size());  // debug

        if (stud4Del == null) {
            //log4j
            Util.showError("Не могу удалить - нет студентов с id="+id4Del);
            return;
        }
        cliOpers.deleteObject(stud4Del);
    }


    @Override
    public void deleteGroup(int id4Del) {
        System.out.println("маршализация и отправка на сервер команды удаления группы id4Del= "+id4Del);  // debug
        Group gr4Del = adm.getGroupById(id4Del);
        System.out.println("удаляемая группа = "+gr4Del);  // debug
        cliOpers.deleteObject(gr4Del);
    }


    public List<Group> selectGroups(Group templateGroup) {
        return null;
    }

    public List<Student> selectStudents(Student templateStudent) {
        return null;
    }




}
