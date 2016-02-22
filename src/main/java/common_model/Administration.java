package common_model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 21.02.2016.
 * Administration of university.
 */
public class Administration {
    private List<Group> groups = new ArrayList();

    public List<Group> getGroups() {
        return groups;
    }

    public List<Student> getAllStudents() {
        List<Student> allStudents = new ArrayList();
        for (Group group : groups) {
            allStudents.addAll(group.getStudentList());
        }
        return allStudents;
    }

    public Group createNewGroup(String groupName, String facultyName) {
        if (groups != null) {
            for (Group group : groups) {
                if (group.getName().equals(groupName)) {
                    throw new IllegalArgumentException("This group was already created!");
                }
            }
        }
        return new Group(groupName, facultyName);
    }

    public boolean addGroup(Group group) {
        if (group == null) {
            throw new IllegalArgumentException("The added group is null!");
        }
            return groups.add(group);
    }

    public boolean removeGroup(Group group) {
        if (!group.isEmpty()) throw new IllegalArgumentException("The group is not empty!");
        return groups.remove(group);
    }

    public boolean enrollNewStudent(Student student, Date enrollmentDate, Group group) {
        if (student == null) throw new IllegalArgumentException("The student is null!");
        if (!groups.contains(group)) throw new IllegalArgumentException("The group is null!");
        if (enrollmentDate == null) throw new IllegalArgumentException("Enrollment date is null!");
        student.setEnrollmentDate(enrollmentDate);
        return group.addStudent(student);
    }

    public boolean expelStudent(Student student) {
        if (student == null) throw new IllegalArgumentException("The student is null");
        return student.getGroup().removeStudent(student);
    }

    public boolean changeStudentGroup(Student student, Group newGroup) {
        if (!groups.contains(newGroup)) {
            throw new IllegalArgumentException("This group doesn't exist yet!");
        }
        student.getGroup().removeStudent(student);
        return newGroup.addStudent(student);
    }
}
