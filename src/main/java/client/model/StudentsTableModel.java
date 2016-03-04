package client.model;

import common_model.Student;
import javax.swing.table.AbstractTableModel;
import java.util.*;

/**
 * Created by Dmitry Khoruzhenko on 01.03.2016.
 */
public class StudentsTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1000L;
    private int columnCount = 4;
    private List<Student> studentsList = new ArrayList<Student>();  // хранилище данных

    public StudentsTableModel() {  // constructor
        super();
//        for (int i=0; i<studentsList.size(); i++) {
//            studentsList.add(new Student(i, "aaa", new Date(), "vvv"));
//        }
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
        if (columnIndex == 0) return  Integer.valueOf (currStudent.getId());
        if (columnIndex==1) return  currStudent.getName();
        if (columnIndex==2) return  currStudent.getEnrollmentDate();
        if (columnIndex==3) return "[объект Группа] "; // currStudent.getGroup();
        return "???";
    }

    /**
     * Дополнительно переопределим метод, возвращающий имена столбцов.
     */
    @Override
    public  String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0: return "#id";
            case 1: return " ФИО ";
            case 2: return "Дата приёма";
            case 3: return "Группа";
        }
        return "";
    }


    public void addData (Student student) {
        studentsList.add(student);
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
