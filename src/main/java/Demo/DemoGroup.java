package Demo;

import common_model.Group;
import common_model.Student;

import java.util.Date;

/**
 * Created by Oleksandr Dudkin on 21.02.2016.
 */
public class DemoGroup {

    public static void main(String[] args) {
        Group group = new Group("E-91", "Econom");
        Group group2 = new Group("M-91", "Econom");

        System.out.println(group.getId());
        System.out.println(group.getName());
        System.out.println(group.getFacultyName());
        System.out.println(group.getStudentList());

        System.out.println("");
        group.setName("Z-91");
        group.setFacultyName("Gumfak");
        System.out.println(group);

        Student student1 = new Student("Ivanov", new Date(100), group);
        Student student2 = new Student("Petrov", new Date(100), group);
        Student student3 = new Student("Sydorov", new Date(100), group);
        group.addStudent(student1);
        group.addStudent(student2);
        group.addStudent(student3);
        System.out.println(group);

        System.out.println("\nContains student");
        System.out.println(group.containsStudent(student1));

        System.out.println("\nRemove student");
        System.out.println(group.removeStudent(student3));
        System.out.println(group);

        System.out.println("\nRemove all student");
        System.out.println(group.removeAllStudents());
        System.out.println(group);

        System.out.println("\nIs empty?");
        System.out.println(group.isEmpty());

    }
}
