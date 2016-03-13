package client.model;

import common_model.AdminInterface;
import common_model.Group;
import common_model.Student;
import common_model.Util;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Dmitry Khoruzhenko on 01.03.2016.
 */
public class StudentsTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1000L;
    private int columnCount = 4;
    private List<Student> studentsList = new ArrayList<Student>();  // хранилище данных
//    private List<Group> groups;  // список групп
    private AdminInterface admin;
    private IoInterface io = new IoXML();

    public StudentsTableModel() {  // constructor
        super();
    }

    public StudentsTableModel(AdminInterface admin) {  // constructor
        super();
        this.admin = admin;
    }


    //    @Override
    public int getRowCount() {
        return studentsList.size();
    }


    //    @Override
    public int getColumnCount() {
        return columnCount;
    }


    //    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student currStudent = studentsList.get(rowIndex);
        if (columnIndex == 0) {
            return currStudent.getId();
//            if    (currStudent.getId() == null)    return "";
//            else                                   return  Integer.valueOf (currStudent.getId());
        }
        if (columnIndex==1) return  currStudent.getName();
        if (columnIndex==2) return Util.dat2Str(currStudent.getEnrollmentDate());
        if (columnIndex==3) return admin.getGroupNameById(currStudent.getGroupId()); // currStudent.getGroup();
        return "???";
    }

    /**
     * Дополнительно переопределим метод, возвращающий имена столбцов.
     */
    @Override
    public  String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0: return "id";
            case 1: return " ФИО ";
            case 2: return "Дата приёма";
            case 3: return "Группа";
        }
        return "";
    }


    public void addData (Student student) {
        studentsList.add(student);
    }


    /**
     *
     * @param fio      - entered student's FIO
     * @param stDateIn - entered student's DateIn
     * @param stGroupName - entered student's GroupName
     * @return  True - if all data are correct, new student created and saved
     */
    public Student checkAndSaveStudent(Integer id, String fio, String stDateIn, String stGroupName) {
//    public boolean checkAndSaveStudent(String fio, String stDateIn, String stGroupName) {
        Date dateIn;
        Student result = null;
        try {
            dateIn = Util.str2Date(stDateIn);
        } catch (ParseException e) {
            Util.showError(e.getMessage());
            return null;
//            e.printStackTrace();
        }
        Group group = admin.getGroupByName(stGroupName);
        if (group == null) {
            Util.showError("Нет такой группы - "+stGroupName);
            return null;
        }

        try {
            Student newStudent = new Student(id, fio, dateIn, group.getId());
            io.saveStudent(newStudent);
            return newStudent;
        }
        catch (Exception e) {
            Util.showError("Упс...  Что-то пошло не так.  "+e.getMessage());
            // Log4j .....
            return null;
        }
    }





    public void sortById() {
/*
        Collections.sort(studentsList, new Comparator<Student>() {
            int compare(Student o1, Student o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        */
    };

 }
