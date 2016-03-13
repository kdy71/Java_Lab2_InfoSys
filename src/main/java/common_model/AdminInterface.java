package common_model;

import java.util.Date;
import java.util.List;

/**
 * Created by Oleksandr Dudkin on 29.02.2016.
 */

public interface AdminInterface {

    public List<Group> getGroups();

    public List<Student> getAllStudents();

    public List<Student> getStudentsFromGroup(Group group);

    public boolean removeAllStudentsFromGroup(Group group);

    public Group createNewGroup(String groupName, String facultyName);

    public boolean addGroup(Group group);

    public boolean removeEmptyGroup(Group group);

    public boolean enrollNewStudent(Student student, Date enrollmentDate, Group group);

    public boolean expelStudent(Student student);

    public boolean changeStudentGroup(Student student, Group newGroup);

    public Group getGroupById(Integer id);

    public String getGroupNameById(Integer id);

    public Group getGroupByName(String groupName);

    public Student getStudentById(Integer id);

    }

