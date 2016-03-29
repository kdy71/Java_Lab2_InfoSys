package client.model;

import common_model.AdministrationInterface;
import common_model.Group;
import common_model.Student;
import common_model.Util_dates;
import common_model.Util_msg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.table.AbstractTableModel;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Dmitry Khoruzhenko on 01.03.2016.
 * TableModel for Students
 */
public class StudentsTableModel extends AbstractTableModel {

    public static final Logger log = LogManager.getLogger(ISClient.class);

    private static final long serialVersionUID = 1000L;
    private int columnCount = 4;
    private AdministrationInterface admin;
    private IoInterface io; // = new IoXML(admin);
    private Student searchTemplate = new Student(null, null, null, null);  // шаблон поиска студентов


    /**
     * Конструктор
     * @param admin  - ссылка на объект класса AdminInterface
     * @param io     - ссылка на объект класса IoInterface
     */
    public StudentsTableModel(AdministrationInterface admin, IoInterface io) {  // constructor
        super();
        this.admin = admin;
        this.io = io;
        io.selectStudents(searchTemplate);   // запрос серверу на получение списка  студентов
        log.info("GroupsTableModel constructor.");
    }


    /**
     * Возвращает число строк в списке студентов
     * @return  -количество студентов
     */
    @Override
    public int getRowCount() {
        return admin.getStudentsCount();
    }


    /**
     * Возвращает количество столбцов грида студентов
     * @return  - количество столбцов грида студентов
     */
    @Override
    public int getColumnCount() {
        return columnCount;
    }


    /**
     * Возвращает значение из ячейки грида с указанными координатами
     * @param rowIndex   - строка
     * @param columnIndex  - стробец
     * @return  - из ячейки грида с указанными координатами
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student currStudent = admin.getStudentByIndex(rowIndex);
        if (columnIndex == 0) {
            return currStudent.getId();
        }
        if (columnIndex == 1) return currStudent.getName();
        if (columnIndex == 2) return Util_dates.dat2Str(currStudent.getEnrollmentDate());
        if (columnIndex == 3) return admin.getGroupNameById(currStudent.getGroupId()); // currStudent.getGroup();
        return "???";
    }


    /**
     * Дополнительно переопределим метод, возвращающий имена столбцов.
     */
    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "id";
            case 1:
                return " ФИО ";
            case 2:
                return "Дата приёма";
            case 3:
                return "Группа";
        }
        return "";
    }


    /**
     * Добавляет студента в список
     * @param student  - добавляемая студент
     */
    public void addData(Student student) {
        admin.addStudent(student);
    }


    /**
     * Сохраняет изменения в студенте (т.е. отправляет их на сервер)
     * @param fio         - entered student's FIO
     * @param stDateIn    - entered student's DateIn
     * @param stGroupName - entered student's GroupName
     * @return True - if all data are correct, new student created and saved
     */
    public Student checkAndSaveStudent(Integer id, String fio, String stDateIn, String stGroupName) {
        Date dateIn;
        try {
            dateIn = Util_dates.str2Date(stDateIn);
        } catch (ParseException e) {
            Util_msg.showError(e.getMessage());
            return null;
        }
        Group group = admin.getGroupByName(stGroupName);
        if (group == null) {
            Util_msg.showError("Нет такой группы - " + stGroupName);
            return null;
        }
        try {
            Student newStudent = new Student(id, fio, dateIn, group.getId());
            io.saveStudent(newStudent);
            log.info("New student was added in the form.");
            return newStudent;
        } catch (Exception e) {
            Util_msg.showError("Упс...  Что-то пошло не так.  " + e.getMessage());
            log.error("Exception while processing checkAndSaveStudent. " + e);
            return null;
        }
    }


    /**
     * Перерисовывает грид. Вызывается при изменении данных.
     */
    public void refreshGrid() {
        fireTableDataChanged();
        sortStudentsByName();
    }


    /**
     * Возвращает шаблон поиска студентов
     * @return - шаблон поиска студентов
     */
    public Student getSearchTemplate() {
        return searchTemplate;
    }


    /**
     * Установка шаблона поиска + запрос серверу на select
     * @param searchTemplate - шаблон поиска
     */
    public void selectStudents(Student searchTemplate) {
        this.searchTemplate = searchTemplate;
        io.selectStudents(searchTemplate);
        log.info("Student was selected in the form.");
    }


    /**
     * Сортирует студентов по ФИО
     */
    public void sortStudentsByName() {
        admin.sortStudentsByName();
    }


    /**
     * Сортирует студентов по id
     */
    public void sortStudentsById() {
        admin.sortStudentsById();
    }


    /**
     * Посылает на сервер команду "Удалить студента"
     * @param id4del  - id удаляемого студента
     */
    public void deleteStudent(Integer id4del) {
        log.info("Student was chosen to delete in the form.");
        io.deleteStudent(id4del);
    }

}

