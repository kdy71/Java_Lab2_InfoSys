package common_model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Dmitry Khoruzhenko, Oleksandr Dudkin on 21.02.2016.
 * Administration of university.
 * Работа с набором групп и набором студентов.
 */
public class Administration implements AdminInterface {

    private List<Group> groups;
    private List<Student> students;


    /**
     * Конструктор
     */
    public Administration() {
        this.groups = new ArrayList();
        this.students = new ArrayList<Student>();
    }


    /**
     * @return  список групп
     */
    public List<Group> getGroups() {
        return groups;
    }


    /**
     * @return список студентов
     */
    public List<Student> getAllStudents() {
        return students;
    }


    /**
     * Возвращает список всех студентов указанной группы
     * @param group  - группа, состав которой ищем
     * @return  - список студентов группы
     */
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


    /**
     * Убирает из группы всех студентов
     * @param group - обрабатываемая группа
     * @return - true/false - успешно или нет
     */
    public boolean removeAllStudentsFromGroup(Group group) {
        if (!groups.contains(group)) throw new IllegalArgumentException("This group doesn't exist!");
        ArrayList<Student> remainingStudents = new ArrayList<Student>();
        for (Student student : students) {
            if (!student.getGroupId().equals(group.getId())) {
                remainingStudents.add(student);
            }
        }
        students = remainingStudents;
        return true;
    }


    /**
     * Создаёт новую группу
     * @param groupName  - название новой группы
     * @param facultyName  - факультет
     * @return  - созданная группа
     */
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


    /**
     * Добавляет группу в список групп
     * @param group  - добавляемая группа
     * @return  - успешно или нет прошло добавление
     */
    public boolean addGroup(Group group) {
        if (group == null) {
            throw new IllegalArgumentException("The added group is null!");
        }
        return groups.add(group);
    }


    /**
     * Добавляет студента в список студентов
     * @param student - добавляемый студент
     * @return  - успешно или нет прошло добавление
     */
    public boolean addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("The added student is null!");
        }
        return students.add(student);
    }


    /**
     * Удаляет группу из списка групп. Перед удалением группы из неё надо убрать всех студентов
     * @param group  - удаляемая группа
     * @return  - успешно или нет прошло удаление
     */
    public boolean removeEmptyGroup(Group group) {
        if (!groups.contains(group)) {
            throw new IllegalArgumentException("This group doesn't exist!");
        }
        for (Student student : students) {
            if (student.getGroupId().equals(group.getId())) {
                throw new IllegalArgumentException("The group is not empty!");
            }
        }
        return groups.remove(group);
    }


    /**
     * Добавляет студента в спикос студентов
     * @param student  - добавляемый студент
     * @param enrollmentDate  - дата зачисление (атрибут класса Студент)
     * @param group  - группа (атрибут класса Студент)
     * @return  -успешно или нет прошло добавление
     */
    public boolean enrollNewStudent(Student student, Date enrollmentDate, Group group) {
        if (student == null) throw new IllegalArgumentException("The student is null!");
        if (!groups.contains(group)) throw new IllegalArgumentException("The group is null!");
        if (enrollmentDate == null) throw new IllegalArgumentException("Enrollment date is null!");
        student.setEnrollmentDate(enrollmentDate);
        student.setGroupId(group.getId());
        return students.add(student);
    }


    /**
     * Удаление студента из списка
     * @param student  - удаляемый студент
     * @return  - успешно или нет прошло удаление
     */
    public boolean expelStudent(Student student) {
        if (student == null) throw new IllegalArgumentException("The student is null");
        return students.remove(student);
    }


    /**
     * Перевод студента в другую группу
     * @param student  - студент, которого надо перевести
     * @param newGroup  - новая группа
     * @return  - успешно или нет прошёл перевод
     */
    public boolean changeStudentGroup(Student student, Group newGroup) {
        if (!groups.contains(newGroup)) {
            throw new IllegalArgumentException("This group doesn't exist yet!");
        }
        student.setGroupId(newGroup.getId());
        return true;
    }


    /**
     * Ищет группу по заданному id группы
     * @param id  -  id искомой группы
     * @return   - найденая группа
     */
    public Group getGroupById(Integer id) {
        for (Group currGroup : groups) {
            if (currGroup.getId() != null && currGroup.getId().equals(id)) {
                return currGroup;
            }
        }
        return null;
    }


    /**
     * Возвращает название группы с заданным id
     * @param id  -  id искомой группы
     * @return   - название найденной группы
     */
    public String getGroupNameById(Integer id) {
        if (id == null) {
            return "";
        }
        for (Group currGroup : groups) {
            if (currGroup.getId() != null && currGroup.getId().equals(id)) {
                return currGroup.getName();
            }
        }
        return "";
    }


    /**
     * Ищет группу по её названию
     * @param groupName  - название искомой группы
     * @return  - найденная группа
     */
    public Group getGroupByName(String groupName) {
        for (Group currGroup : groups) {
            String st = currGroup.getName();
            if (currGroup.getName().equals(groupName)) {
                return currGroup;
            }
        }
        return null;
    }


    /**
     * Ищет студента по заданному id
     * @param id  - id искомого студента
     * @return  - найденный студент
     */
    public Student getStudentById(Integer id) {
        for (Student currStudent : students) {
            if (currStudent.getId().equals(id)) {
                return currStudent;
            }
        }
        return null;
    }


    /**
     * Ищет студента по его индексу в списке студентов
     * @param index  - индекс искомого студента
     * @return   - найденный студент
     */
    public Student getStudentByIndex(int index) {
        return students.get(index);
    }


    /**
     * Ищет группу по её индексу в списка групп
     * @param index - индекс искомой группы
     * @return  - найденная группа
     */
    public Group getGroupByIndex(int index) {
        return groups.get(index);
    }


    /**
     * Возвращает количество студентов в списке
      * @return - количество студентов в списке
     */
    public int getStudentsCount() {
        return students.size();
    }


    /**
     * Возвращает количество групп в списке
     * @return - количество групп
     */
    public int getGroupsCount() {
        return groups.size();
    }


    /**
     * Подменяет список студентов новым
     * @param newStudents  - новый список студентов
     */
    public void replaceAllStudents(List<Student> newStudents) {
        students = newStudents;
    }


    /**
     * Подменяет список групп новым
     * @param newGroups - новый список групп
     */
    public void replaceAllGroups(List<Group> newGroups) {
        groups = newGroups;
    }


    /**
     * Сортировка списка студентов по их id
     */
    public void sortStudentsById() {
        getAllStudents().sort(new ComparatorStudentsById());
    }


    /**
     * Сортировка списка студентов по ФИО
     */
    public void sortStudentsByName() {
          students.sort(new ComparatorStudentsByFio());
    }


    /**
     * Копмаратор для сравнения студентов по id
     */
    public class ComparatorStudentsById implements Comparator<Student> {
        public int compare(Student o1, Student o2) {
            Integer id1 = o1.getId();
            Integer id2 = o2.getId();
            return (id1.compareTo(id2));
        }
    }


    /**
     * Компаратор для сравнения студентов по ФИО
     */
    public class ComparatorStudentsByFio implements Comparator<Student> {
        public int compare(Student o1, Student o2) {
            String fio1 = o1.getName();
            String fio2 = o2.getName();
            return (fio1.compareTo(fio2));
        }
    }

}
