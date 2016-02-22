package common_model;

import java.util.Date;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 15.02.2016.
 * Students
 */
public class Student {

    private int id;
    private String name;
    private Group group;
    private Date enrollmentDate;

    public Student(String name) {
        this.name = name;
    }
    
    public Student(String name, Date enrollmentDate, Group group) {
        this.name = name;
        this.enrollmentDate = enrollmentDate;
        this.group = group;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        if (this.enrollmentDate == null) {
        this.enrollmentDate = enrollmentDate;
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", group=" + group +
                ", enrollmentDate=" + enrollmentDate +
                '}';
    }
}
