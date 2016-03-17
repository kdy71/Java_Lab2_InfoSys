package client.controller;

import client.model.*;
import client.view.GroupsGUI;
import client.view.StudentsGUI;
import common_model.AdminInterface;
import common_model.Administration;
import common_model.Student;

import java.util.List;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 15.02.2016.
 * Information system Client
 */
public class Main_Client {

    private  XmlClientOperations xmlCliOpers;
    private AdminInterface admin;
    private StudentsTableModel stm;

    public static void main(String[] args){
        Main_Client mainClient = new Main_Client();
        mainClient.startMain();

        // TODO: 15.03.2016 Реализовать методы получения данных с сервера (считать, что на сервере всё уже есть.)
        // TODO: 15.03.2016 Слушать сервер в отдельном потоке? И сразу обновлять данные в гридах??
        // -- При вставке/редактиров. студ. и гр. хранить поля во спец. буферном объекте. К гриду не обращаться!
        // -- Доделать вызов методов удаления студ/групп.
        // TODO: 15.03.2016 Доделать сортировку студентов (и групп?)
        // TODO: 15.03.2016 Пересмотреть класс хранилища списка студентов/групп. Нужна сортировка по компаратору и быстрый поиск по индексу.
        // TODO: 15.03.2016 При редактировании студента группу выбирать из списка (не срочно)
        // TODO: 15.03.2016 При перемещении по гриду с группами менять фильтр на гриде студентов. Слушатель событий-? (не обязательно)
        // TODO: 16.03.2016 Разбить проект на 2 мавен- проекта - клиент и сервер
        // TODO: 16.03.2016 На сервере обрабатывать отключение клиентов
    }


    private void startMain(){
        System.out.println("I am client");  // debug
        xmlCliOpers = new XmlClientOperations();

        ISClient client1 = new ISClient();
        ClientEar clientEar1 = new ClientEar(client1.getInputStream() , this);
        Thread t1 = new Thread(clientEar1);
        t1.start();

        admin = new Administration();
        GroupsTableModel gtm = new GroupsTableModel(admin);
        GroupsGUI groupsGUI = new GroupsGUI(gtm);

        /*StudentsTableModel */ stm = new StudentsTableModel(admin);
        StudentsGUI studentsGUI = new StudentsGUI(stm);
        System.out.println(" main adm.getAllStudents().size()= "+admin.getAllStudents().size());  // debug

    }


    /**
     * Получает из ClientEar сообщение, прилетевшее с сервера
     * И отправляет его на парсинг в XmlClientOperations
     * @param stXML
     */
    public void stringXML_2Obj(String stXML) {
        Object objMsg =  xmlCliOpers.parseServerMsg_2Obj(stXML);
        if (objMsg instanceof List ) {processingSrvMsg((List) objMsg);}
    }

    public void processingSrvMsg (List<Student> newStudents) {
        admin.replaceAllStudents(newStudents);  // заменить список студентов на новый
        stm.refreshGrid();                  // обновить отображение списка студентов в гриде

    }


}
