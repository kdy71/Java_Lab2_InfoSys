package common_model;

import java.util.Date;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 15.02.2016.
 * Students
 */
public class Student {
    private int id;
    private String name;
    private int group_id;
    private /*Group*/String  group;
    private Date enrollmentDate;

//    public Student(int id, String name, Date enrollmentDate, Group group) {
    public Student(int id, String name, Date enrollmentDate, String group) {
        this.id = id;
        this.name = name;
        this.enrollmentDate = enrollmentDate;
        this.group = group;
    }

    public Student(int id, String name, Date enrollmentDate, int group_id) {
        this.id = id;
        this.name = name;
        this.enrollmentDate = enrollmentDate;
        this.group_id = group_id;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

//    public Group getGroup() {
    public String getGroup() {
        return group;
    }

//    public void setGroup(Group group) {
    public void setGroup(String group) {
        this.group = group;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", group_id=" + group_id +
                ", group=" + group +
                ", enrollmentDate=" + enrollmentDate +
                '}';
    }
}
