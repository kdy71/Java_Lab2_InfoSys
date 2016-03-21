package common_model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 15.02.2016.
 * Groups of students
 */
@XmlRootElement(name = "group")
@XmlType(propOrder = {"id", "name", "facultyName"})
public class Group {

    private Integer id;
    private String name;
    private String facultyName;

    public Group() {
    }


    public Group(String name, String facultyName) {
        this.name = name;
        this.facultyName = facultyName;
    }

    public Group(Integer id, String name, String facultyName) {
        this.id = id;
        this.name = name;
        this.facultyName = facultyName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", facultyName='" + facultyName + '\'' +
                '}';
    }
}
