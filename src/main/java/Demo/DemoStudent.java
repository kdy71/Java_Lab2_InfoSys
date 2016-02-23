package Demo;

import common_model.Group;
import common_model.Student;

import java.util.Date;

/**
 * Created by Oleksandr Dudkin on 21.02.2016.
 */
public class DemoStudent {

    public static void main(String[] args) {
        Group group = new Group("E-91", "Econom");
        Group group2 = new Group("M-91", "Econom");

        Student student = new Student("Ivanov", new Date(100), group);
        Student student2 = new Student("Petrov", new Date(100), group);

        System.out.println(student);
        System.out.println(student2);


        System.out.println(student.getId());
        System.out.println(student.getEnrollmentDate());
        System.out.println(student.getGroup());

        student.setEnrollmentDate(new Date(200000));
        student.setGroup(group2);
        student.setName("Sydorov");
        System.out.println(student);
    }
}
