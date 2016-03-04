package client.controller;

import client.view.GroupsGUI;
import client.view.StudentsGUI;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 15.02.2016.
 * Information system Client
 */
public class Main_Client {

    public static void main(String[] args){
        System.out.println("I am client");  // debug
//        ISClient client1 = new ISClient();
        GroupsGUI groupsGUI = new GroupsGUI();
        StudentsGUI studentsGUI = new StudentsGUI();

    }


}
