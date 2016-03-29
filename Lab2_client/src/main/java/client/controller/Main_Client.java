package client.controller;

import client.model.*;
import client.view.GroupsGUI;
import client.view.StudentsGUI;
import common_model.AdministrationInterface;
import common_model.Administration;
import common_model.Group;
import common_model.Student;
//import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//import org.apache.log4j.Logger;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 15.02.2016.
 * Information system Client
 */
public class Main_Client {

    private XmlClientOperations xmlCliOpers;
    private AdministrationInterface admin;
    private StudentsTableModel stm;
    private GroupsTableModel gtm;

    public static final Logger log = LogManager.getLogger(Main_Client.class);


    /**
     * main
     */
    public static void main(String[] args) {
        Main_Client mainClient = new Main_Client();
        mainClient.startMain();
        log.info("Client was launched.");
    }


    /**
     * Здесь создаются экземпляры классов.
     * Почему не в методе main - чтобы уйти от статики.
     */
    private void startMain() {
        System.out.println("I am client");  // debug
        xmlCliOpers = new XmlClientOperations();

        ISClient client1 = new ISClient();
        ClientEar clientEar1 = new ClientEar(client1.getInputStream(), this);
        Thread t1 = new Thread(clientEar1);
        t1.start();

        admin = new Administration();
        IoInterface io = new IoXML(admin, client1);

        gtm = new GroupsTableModel(admin, io);
        GroupsGUI groupsGUI = new GroupsGUI(gtm);

        stm = new StudentsTableModel(admin, io);
        StudentsGUI studentsGUI = new StudentsGUI(stm);

        log.info("End of preparation for launching client.");
    }


    /**
     * Получает из ClientEar сообщение, прилетевшее с сервера
     * И отправляет его на парсинг в xmlCliOpers - там из него формируется объект (список групп или студентов).
     * А затем этот список отображаем в GUI.
     * @param stXML - сообщение с сервера (строка XML)
     */
    public void stringXML_2Obj(String stXML) {
        Object objMsg = xmlCliOpers.parseServerMessageToObjects(stXML);
        if (objMsg instanceof List) {
            ArrayList list = (ArrayList) objMsg;
            //  generic ???
            if (list.size() == 0) {return;}
            if (list.get(0) instanceof Group) {
                processingSrvMsg_groups(list);}
            if (list.get(0) instanceof Student) {
                processingSrvMsg_students(list);}
        }
    }


    /**
     * Заменяет и перерисовывает список групп
     * @param newGroups  - новый список групп
     */
    public void processingSrvMsg_groups(ArrayList<Group> newGroups) {
        admin.replaceAllGroups(newGroups);  // заменить список студентов на новый
        gtm.refreshGrid();                  // обновить отображение списка студентов в гриде
        log.info("List of groups in GUI was renewed.");
    }


    /**
     * Заменяем и перерисовывает список студентов
     * @param newStudents - новый список студентов
     */
    public void processingSrvMsg_students(ArrayList<Student> newStudents) {
        admin.replaceAllStudents(newStudents);  // заменить список студентов на новый
        stm.refreshGrid();                  // обновить отображение списка студентов в гриде
        log.info("List of students in GUI was renewed.");
    }
}
