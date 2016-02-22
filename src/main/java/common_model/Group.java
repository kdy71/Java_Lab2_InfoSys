package common_model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 15.02.2016.
 * Groups of students
 */
public class Group {

    private int id;
    private String name;
    private String facultyName;
    private List<Student> listOfStudents = new ArrayList();

    public Group(String name, String facultyName) {
        this.name = name;
        this.facultyName = facultyName;
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

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public List<Student> getStudentList() {
        return listOfStudents;
    }

    public boolean containsStudent(Student student) {
        return listOfStudents.contains(student);
    }

    public boolean addStudent(Student student) {
        if (student == null) throw new IllegalArgumentException("The added student is null!");
        if (listOfStudents == null) throw new IllegalArgumentException("The list of student is null!");
        student.setGroup(this);
        return listOfStudents.add(student);
    }

    public boolean removeStudent(Student student) {
        return listOfStudents.remove(student);
    }

    public boolean removeAllStudents() {
        listOfStudents = null;
        return true;
    }

    public boolean isEmpty() {
        if (this.listOfStudents == null) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", facultyName='" + facultyName + '\'' +
                ", listOfStudents=" + makeRowOfStudents() +
                '}';
    }

    private StringBuilder makeRowOfStudents() {
        StringBuilder sb = new StringBuilder();
        if (listOfStudents == null) {
            sb = sb.append("list is empty");
        } else {
            for (Student student : listOfStudents) {
                sb.append(" ");
                sb.append(student.getName());
            }
        }
        return sb;
    }
}
