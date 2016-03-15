package client.controller;

import client.model.ClientEar;
import client.model.GroupsTableModel;
import client.model.ISClient;
import client.model.StudentsTableModel;
import client.view.GroupsGUI;
import client.view.StudentsGUI;
import common_model.AdminInterface;
import common_model.Administration;
import common_model.Group;
import common_model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 15.02.2016.
 * Information system Client
 */
public class Main_Client {

    public static void main(String[] args){
        System.out.println("I am client");  // debug

        ISClient client1 = new ISClient();
        ClientEar clientEar1 = new ClientEar();
        Thread t1 = new Thread(clientEar1);
        t1.start();


        AdminInterface admin = new Administration();
        GroupsTableModel gtm = new GroupsTableModel(admin);
        GroupsGUI groupsGUI = new GroupsGUI(gtm);

        StudentsTableModel stm = new StudentsTableModel(admin);
        StudentsGUI studentsGUI = new StudentsGUI(stm);
        System.out.println(" main adm.getAllStudents().size()= "+admin.getAllStudents().size());  // debug

        // TODO: 15.03.2016 Реализовать методы получения данных с сервера (считать, что на сервере всё уже есть.)
        // TODO: 15.03.2016 Слушать сервер в отдельном потоке? И сразу обновлять данные в гридах??
        // -- При вставке/редактиров. студ. и гр. хранить поля во спец. буферном объекте. К гриду не обращаться!
        // -- Доделать вызов методов удаления студ/групп.
        // TODO: 15.03.2016 Доделать сортировку студентов (и групп?)
        // TODO: 15.03.2016 Пересмотреть класс хранилища списка студентов/групп. Нужна сортировка по компаратору и быстрый поиск по индексу.
        // TODO: 15.03.2016 При редактировании студента группу выбирать из списка (не срочно)
        // TODO: 15.03.2016 При перемещении по гриду с группами менять фильтр на гриде студентов. Слушатель событий-? (не обязательно)

    }


}
