package Demo;

import common_model.Administration;
import common_model.Group;
import common_model.Student;

import java.util.Date;

/**
 * Created by Oleksandr Dudkin on 21.02.2016.
 */
public class DemoAdministration {

    public static void main(String[] args) {
        Administration administration = new Administration();

        Group group1 = administration.createNewGroup("E-91", "Econom");
        Group group2 = administration.createNewGroup("M-91", "Gumfak");
        group1.setId(1); //перед тем, как добавлять студентов нужно точно знать что у группы уникальный id!!!
        group2.setId(2);
        System.out.println(administration.addGroup(group1));
        System.out.println(administration.addGroup(group2));

        System.out.println(administration.getGroups());

        Student student11 = new Student("Ivanov");
        Student student12 = new Student("Petrov");
        administration.enrollNewStudent(student11, new Date(100), group1);
        administration.enrollNewStudent(student12, new Date(200), group1);

        Student student21 = new Student("Sydorov");
        administration.enrollNewStudent(student21, new Date(300), group2);

        System.out.println(administration.getAllStudents());
        System.out.println(administration.getStudentsFromGroup(group1));

        administration.removeAllStudentsFromGroup(group1);
        System.out.println(administration.getAllStudents());
        System.out.println(administration.removeEmptyGroup(group1));

        System.out.println(administration.getGroups());
        System.out.println(administration.getAllStudents());

        //System.out.println(administration.expelStudent(student21));
        //System.out.println(administration.getStudentsFromGroup(group2));

        Group group3 = administration.createNewGroup("E-91", "Econom");
        administration.addGroup(group3);
        administration.changeStudentGroup(student21, group3);

        System.out.println(administration.getAllStudents());
        System.out.println(administration.getGroups());
    }
}
