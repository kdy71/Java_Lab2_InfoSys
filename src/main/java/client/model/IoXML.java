package client.model;

import common_model.AdminInterface;
import common_model.Group;
import common_model.Student;
import common_model.Util;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

//import XmlClientOperations;

/**
 * Реализация интерфейса IoInterface для варианта хранения данных в XML
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 11.03.2016.
 */
public class IoXML implements IoInterface {

    private XmlClientOperations cliOpers = new XmlClientOperations();
    private AdminInterface adm;
    private ISClient isClient;

    public IoXML(AdminInterface adm, ISClient isClient) {
        this.adm = adm;
        this.isClient = isClient;
    }

    /**
     * Посылает на сервер команду "сохранить студента"
     * Если id студента пустой - команда Insert, иначе - Update
     *
     * @param student - студент, которого будем записывать
     * @throws JAXBException
     * @throws ParserConfigurationException
     */
    public void saveStudent(Student student) /*throws JAXBException, ParserConfigurationException */ {
        System.out.println("маршализация и отправка на сервер студента " + student);  // debug
        //String stXML = cliOpers.getXmlUpdateObject(student);
        if (student.getId() == null) {
            String stXML = cliOpers.getXmlCreateObject(student);
            isClient.writeStringToServer(stXML);
        } else {
            String stXML = cliOpers.getXmlUpdateObject(student);
            isClient.writeStringToServer(stXML);
        }
    }


    public void saveGroup(Group group) {
        System.out.println("маршализация и отправка на сервер группы " + group);  // debug
        //String stXML = cliOpers.getXmlUpdateObject(group);
        if (group.getId() == null) {
            String stXML = cliOpers.getXmlCreateObject(group);
            isClient.writeStringToServer(stXML);
        } else {
            String stXML = cliOpers.getXmlUpdateObject(group);
            isClient.writeStringToServer(stXML);
        }
    }


    @Override
    public void deleteStudent(int id4Del) {
        System.out.println("маршализация и отправка на сервер команды удаления студента  id4Del= " + id4Del);  // debug
        Student stud4Del = adm.getStudentById(id4Del);
//        System.out.println("удаляемый студент  = "+stud4Del);  // debug
//        System.out.println("adm.getAllStudents().size()= "+adm.getAllStudents().size());  // debug

        if (stud4Del == null) {
            //log4j
            Util.showError("Не могу удалить - нет студентов с id=" + id4Del);
            return;
        }
        String stXML = cliOpers.getXmlDeleteObject(stud4Del);
        isClient.writeStringToServer(stXML);
    }


    @Override
    public void deleteGroup(int id4Del) {
        System.out.println("маршализация и отправка на сервер команды удаления группы id4Del= " + id4Del);  // debug
        Group gr4Del = adm.getGroupById(id4Del);
        System.out.println("удаляемая группа = " + gr4Del);  // debug
        String stXML = cliOpers.getXmlDeleteObject(gr4Del);
        isClient.writeStringToServer(stXML);
    }


//    public List<Group> selectGroups(Group templateGroup) {
    public void selectGroups(Group templateGroup) {
        String stXML = XmlClientOperations.findGroups(templateGroup.getId(),
                templateGroup.getName(), templateGroup.getFacultyName());
        System.out.println("результат запроса групп = " + stXML);  // debug
        isClient.writeStringToServer(stXML);
//        return null;
    }


//    public List<Student> selectStudents(Student templateStudent) {
    public void selectStudents(Student templateStudent) {
        String st = XmlClientOperations.findStudents(templateStudent.getId(),
                templateStudent.getName(), templateStudent.getGroupId(), templateStudent.getEnrollmentDate());
//        System.out.println("результат запроса студентов = " + st);  // debug
        isClient.writeStringToServer(st);
//        return null;
    }


}