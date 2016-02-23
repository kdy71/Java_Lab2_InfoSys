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
        Group group2 = administration.createNewGroup("M-91", "Econom");
        System.out.println(administration.addGroup(group1));
        System.out.println(administration.addGroup(group2));

        System.out.println(administration.getGroups());

        Student student11 = new Student("Ivanov", new Date(100), group1);
        Student student12 = new Student("Petrov", new Date(100), group1);
        Student student21 = new Student("Sydorov", new Date(100), group2);
        group1.addStudent(student11);
        group1.addStudent(student12);
        group2.addStudent(student21);


        System.out.println(group1.getStudentList());
        System.out.println(group2.getStudentList());

        System.out.println(administration.getAllStudents());

        group2.removeAllStudents();
        System.out.println(administration.removeGroup(group2));

        Student newStudent = new Student("Vasyl'ev");
        System.out.println(administration.enrollNewStudent(newStudent, new Date(200), group1));
        System.out.println(administration.getAllStudents());

        System.out.println(administration.expelStudent(newStudent));
        System.out.println(administration.getAllStudents());

        Group group3 = administration.createNewGroup("Z-91", "Yurfak");
        administration.addGroup(group3);
        System.out.println(administration.changeStudentGroup(student11, group3));
        System.out.println(administration.getAllStudents());

    }
}
