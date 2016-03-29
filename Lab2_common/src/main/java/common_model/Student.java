package common_model;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 15.02.2016.
 * Students
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "student")
@XmlType(propOrder = {"id", "name", "groupId", "enrollmentDate"})
public class Student {

    private Integer id = null;
    private String name = null;
    private Integer groupId = null;
    private Date enrollmentDate = null;
//    private Group group;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public Student(String name, Date enrollmentDate, int groupId) {
        this.name = name;
        this.enrollmentDate = enrollmentDate;
        this.groupId = groupId;
    }

    public Student(Integer id, String name, Date enrollmentDate, Integer groupId) {
        this.id = id;
        this.name = name;
        this.enrollmentDate = enrollmentDate;
        this.groupId = groupId;
    }


    /*
    public Student(String name, Date enrollmentDate, Group group) {
        this.name = name;
        this.enrollmentDate = enrollmentDate;
        this.group = group;
        this.groupId = group.getId();
    }
*/

    @XmlElement
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @XmlElement
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    @XmlElement(name = "enrollmentDate", required = true)
    @XmlJavaTypeAdapter(DateAdapter.class)
    public void setEnrollmentDate(Date enrollmentDate) {
        if (this.enrollmentDate == null) {
            this.enrollmentDate = enrollmentDate;
        }
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", groupId=" + groupId +
                ", enrollmentDate=" + enrollmentDate +
                '}';
    }


}
