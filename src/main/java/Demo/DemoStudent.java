package Demo;

import common_model.Student;

import java.util.Date;

/**
 * Created by Oleksandr Dudkin on 21.02.2016.
 */
public class DemoStudent {

    public static void main(String[] args) {
        //Group group = new Group("E-91", "Econom");
        //Group group2 = new Group("M-91", "Econom");

        Student student = new Student("Ivanov", new Date(100), 1);
        Student student2 = new Student("Petrov");

        System.out.println(student);
        System.out.println(student2);

        System.out.println(student.getName());
        System.out.println(student.getId());
        System.out.println(student.getEnrollmentDate());
        System.out.println(student.getGroupId());

        student.setId(3);
        student.setEnrollmentDate(new Date(200000));
        student.setName("Sydorov");
        System.out.println(student);
    }
}
