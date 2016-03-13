package common_model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 21.02.2016.
 * Administration of university.
 */
public class Administration implements AdminInterface {

    private List<Group> groups; // = new ArrayList();
    private List<Student> students; // = new ArrayList<Student>();

    public Administration(List<Group> groups, List<Student> students) {
        this.groups = groups;
        this.students = students;
    }

    public Administration() {
        this.groups =  new ArrayList();
        this.students = new ArrayList<Student>();
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public List<Student> getStudentsFromGroup(Group group) {
        if (group == null) throw new IllegalArgumentException("The group is null!");
        List<Student> studentsFromGroup = new ArrayList<Student>();
        for (Student student : students) {
            if (student.getGroupId() == group.getId()) {
                studentsFromGroup.add(student);
            }
        }
        return studentsFromGroup;
    }

    public boolean removeAllStudentsFromGroup(Group group) {
        if (!groups.contains(group)) throw new IllegalArgumentException("This group doesn't exist!");
        ArrayList<Student> remainingStudents = new ArrayList<Student>();

        for (Student student : students) {
            if (student.getGroupId() != group.getId()) {
                remainingStudents.add(student);
            }
        }

        students = remainingStudents;
        return true;
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

    public boolean removeEmptyGroup(Group group) {
        //if (!group.isEmpty()) throw new IllegalArgumentException("The group is not empty!");
        if (!groups.contains(group)) {
            throw new IllegalArgumentException("This group doesn't exist!");
        }
        for (Student student : students) {
            if (student.getGroupId() == group.getId()) {
                throw new IllegalArgumentException("The group is not empty!");
            }
        }
        return groups.remove(group);
    }

    public boolean enrollNewStudent(Student student, Date enrollmentDate, Group group) {
        if (student == null) throw new IllegalArgumentException("The student is null!");
        if (!groups.contains(group)) throw new IllegalArgumentException("The group is null!");
        if (enrollmentDate == null) throw new IllegalArgumentException("Enrollment date is null!");
        student.setEnrollmentDate(enrollmentDate);
        student.setGroupId(group.getId());
        return students.add(student);
    }

    public boolean expelStudent(Student student) {
        if (student == null) throw new IllegalArgumentException("The student is null");
        return students.remove(student);
    }

    public boolean changeStudentGroup(Student student, Group newGroup) {
        if (!groups.contains(newGroup)) {
            throw new IllegalArgumentException("This group doesn't exist yet!");
        }
        student.setGroupId(newGroup.getId());
        return true;
    }

    public Group getGroupById(Integer id) {
        for (Group currGroup : groups) {
            if (currGroup.getId() != null  && currGroup.getId().equals(id)) {
                return currGroup;
            }
        }
        return null;
    }


    public String getGroupNameById(Integer id) {
        if (id == null) {return "";}
        for (Group currGroup : groups) {
            if (currGroup.getId() != null  &&  currGroup.getId().equals(id)) {
                return currGroup.getName();
            }
        }
        return "";
    }


    public Group getGroupByName(String groupName) {
        for (Group currGroup : groups) {
            String st = currGroup.getName();
            if (currGroup.getName().equals(groupName)) {
                return currGroup;
            }
        }
        return null;
    }


    public Student getStudentById(Integer id) {
        for (Student currStudent : students) {
            if (currStudent.getId().equals(id)) {return currStudent;}
        }
        return null;
    }
}
