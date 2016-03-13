package client.controller;

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
    private static List<Group> groups = new ArrayList<Group>();  // список групп из GroupsTableModel
    private static List<Student> students = new ArrayList<Student>();  // список студентов

    public static void main(String[] args){
        System.out.println("I am client");  // debug
//        ISClient client1 = new ISClient();

        AdminInterface admin = new Administration(groups, students);
//        GroupsGUI groupsGUI = new GroupsGUI();
        GroupsTableModel gtm = new GroupsTableModel(admin);
        GroupsGUI groupsGUI = new GroupsGUI(gtm);

        StudentsTableModel stm = new StudentsTableModel(admin);
        StudentsGUI studentsGUI = new StudentsGUI(stm);

    }


}
