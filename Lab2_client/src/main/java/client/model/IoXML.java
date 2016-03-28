package client.model;

import common_model.AdminInterface;
import common_model.Group;
import common_model.Student;
import common_model.Util_msg;
//import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

//import XmlClientOperations;

/**
 * Реализация интерфейса IoInterface для варианта хранения данных в XML
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 11.03.2016.
 */
public class IoXML implements IoInterface {

    public static final Logger log = LogManager.getLogger(ISClient.class);

    private XmlClientOperations cliOpers = new XmlClientOperations();
    private AdminInterface adm;
    private ISClient isClient;


    /**
     * Конструктор
     * @param adm  - ссылка на эклемпляр класса AdminInterface
     * @param isClient  - ссылка на эклемпляр класса ISClient
     */
    public IoXML(AdminInterface adm, ISClient isClient) {
        this.adm = adm;
        this.isClient = isClient;
        log.info("IoXML constructor.");
    }

    /**
     * Посылает на сервер команду "сохранить студента"
     * Если id студента пустой - команда Insert, иначе - Update
     * @param student - студент, которого будем записывать
     */
    public void saveStudent(Student student)  {
        System.out.println("маршализация и отправка на сервер студента " + student);  // debug
        if (student.getId() == null) {
            String stXML = cliOpers.getXmlCreateObject(student);
            isClient.writeStringToServer(stXML);
        } else {
            String stXML = cliOpers.getXmlUpdateObject(student);
            isClient.writeStringToServer(stXML);
        }
        log.info("Student was saved.");
    }


    /**
     * Посылает на сервер команду "сохранить группу"
     * Если id группы пустой - команда Insert, иначе - Update
     * @param group  - группа, которую будем записывать
     */
    public void saveGroup(Group group) {
        System.out.println("маршализация и отправка на сервер группы " + group);  // debug
        if (group.getId() == null) {
            String stXML = cliOpers.getXmlCreateObject(group);
            isClient.writeStringToServer(stXML);
        } else {
            String stXML = cliOpers.getXmlUpdateObject(group);
            isClient.writeStringToServer(stXML);
        }
        log.info("Group was saved.");
    }


    /**
     * Посылает на сервер команду "Удалить студента"
     * @param id4Del - id студента, которого надо удалить
     */
    public void deleteStudent(int id4Del) {
        System.out.println("маршализация и отправка на сервер команды удаления студента  id4Del= " + id4Del);  // debug
        Student stud4Del = adm.getStudentById(id4Del);
        if (stud4Del == null) {
            Util_msg.showError("Не могу удалить - нет студентов с id=" + id4Del);
            return;
        }
        String stXML = cliOpers.getXmlDeleteObject(stud4Del);
        isClient.writeStringToServer(stXML);
        log.info("Student was deleted.");
    }


    /**
     * Посылает на сервер команду "Удалить группу"
     * @param id4Del  - id группы, которую надо удалить
     */
    public void deleteGroup(int id4Del) {
        System.out.println("маршализация и отправка на сервер команды удаления группы id4Del= " + id4Del);  // debug
        Group gr4Del = adm.getGroupById(id4Del);
        System.out.println("удаляемая группа = " + gr4Del);  // debug
        String stXML = cliOpers.getXmlDeleteObject(gr4Del);
        isClient.writeStringToServer(stXML);
        log.info("Group was deleted.");
    }


    /**
     * Посылает на сервер команду "Select" для групп
     * @param templateGroup  - группа-шаблон, по атрибутам которой будем производить поиск
     */
    public void selectGroups(Group templateGroup) {
        String stXML = XmlClientOperations.findGroups(templateGroup.getId(),
                templateGroup.getName(), templateGroup.getFacultyName());
        isClient.writeStringToServer(stXML);
        log.info("Request to find group(s) was created.");
    }


    /**
     * Посылает на сервер команду "Select" для студентов
     * @param templateStudent  - студент-шаблон, по атрибутам которого будем производить поиск.
     */
    public void selectStudents(Student templateStudent) {
        String st = XmlClientOperations.findStudents(templateStudent.getId(),
                templateStudent.getName(), templateStudent.getGroupId(), templateStudent.getEnrollmentDate());
        isClient.writeStringToServer(st);
        log.info("Request to find student(s) was created.");
    }

}