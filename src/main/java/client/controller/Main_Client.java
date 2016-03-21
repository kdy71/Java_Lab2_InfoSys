package client.controller;

import client.model.*;
import client.view.GroupsGUI;
import client.view.StudentsGUI;
import common_model.AdminInterface;
import common_model.Administration;
import common_model.Group;
import common_model.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 15.02.2016.
 * Information system Client
 */
public class Main_Client {

    private XmlClientOperations xmlCliOpers;
    private AdminInterface admin;
    private StudentsTableModel stm;
    private GroupsTableModel gtm;

    public static void main(String[] args) {
        Main_Client mainClient = new Main_Client();
        mainClient.startMain();

        // TODO: 15.03.2016 Реализовать методы получения данных с сервера (считать, что на сервере всё уже есть.)
        // TODO: 15.03.2016 Слушать сервер в отдельном потоке? И сразу обновлять данные в гридах??
        // -- При вставке/редактиров. студ. и гр. хранить поля во спец. буферном объекте. К гриду не обращаться!
        // -- Доделать вызов методов удаления студ/групп.
        // (ok) 15.03.2016 Доделать сортировку студентов
        // TODO: 15.03.2016 При редактировании студента группу выбирать из списка (не срочно)
        // TODO: 15.03.2016 При перемещении по гриду с группами менять фильтр на гриде студентов. Слушатель событий-? (не обязательно)
        // TODO: 16.03.2016 Разбить проект на 2 мавен- проекта - клиент и сервер
        // TODO: 16.03.2016 На сервере обрабатывать отключение клиентов
    }


    private void startMain() {
        System.out.println("I am client");  // debug
        xmlCliOpers = new XmlClientOperations();

        ISClient client1 = new ISClient();
        ClientEar clientEar1 = new ClientEar(client1.getInputStream(), this);
        Thread t1 = new Thread(clientEar1);
        t1.start();

        admin = new Administration();
        IoInterface io = new IoXML(admin, client1);

        /*GroupsTableModel */ gtm = new GroupsTableModel(admin, io);
        GroupsGUI groupsGUI = new GroupsGUI(gtm);


        /*StudentsTableModel */
        stm = new StudentsTableModel(admin, io);
        StudentsGUI studentsGUI = new StudentsGUI(stm);
        System.out.println(" main adm.getAllStudents().size()= " + admin.getAllStudents().size());  // debug

    }


    /**
     * Получает из ClientEar сообщение, прилетевшее с сервера
     * И отправляет его на парсинг в XmlClientOperations
     *
     * @param stXML
     */
    public void stringXML_2Obj(String stXML) {
//        Object objMsg =  xmlCliOpers.parseServerMsg_2Obj(stXML);
        Object objMsg = xmlCliOpers.parseServerMessageToObjects(stXML);
        System.out.println("---Main_Client.objMsg = "+objMsg); // debug
        if (objMsg instanceof List) {
            ArrayList list = (ArrayList) objMsg;
            System.out.println("---Main_Client.objMsg[0] = "+ list.get(0)); // debug
            // TODO: 21.03.2016 generic ??? Разобраться с типами !!!
//            processingSrvMsg(list);
            if (list.get(0) instanceof Group) {processingSrvMsg1(list);}
            if (list.get(0) instanceof Student) {processingSrvMsg(list);}
//            processingSrvMsg1(list);
        }
    }

    public void processingSrvMsg1(ArrayList<Group> newGroups) {
        admin.replaceAllGroups(newGroups);  // заменить список студентов на новый
        gtm.refreshGrid();                  // обновить отображение списка студентов в гриде
    }

    public void processingSrvMsg(ArrayList<Student> newStudents) {
        admin.replaceAllStudents(newStudents);  // заменить список студентов на новый
        stm.refreshGrid();                  // обновить отображение списка студентов в гриде
    }

}
