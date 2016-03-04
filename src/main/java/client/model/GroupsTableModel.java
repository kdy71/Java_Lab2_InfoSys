package client.model;

import common_model.Group;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry Khoruzhenko on 02.03.2016.
 * TableModel for Groups
 */
public class GroupsTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 2000L;
    private int columnCount = 3;
    private List<Group> groupsList = new ArrayList<Group>();  // хранилище данных

    public GroupsTableModel() {  // constructor
        super();
//        for (int i=0; i<studentsList.size(); i++) {
//            studentsList.add(new Student(i, "aaa", new Date(), "vvv"));
//        }
    }

    //    @Override
    public int getRowCount() {
        return groupsList.size();
    }

    //    @Override
    public int getColumnCount() {
        return columnCount;
    }

    //    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Group currGroup = groupsList.get(rowIndex);
        if (columnIndex == 0) return  Integer.valueOf (currGroup.getId());
        if (columnIndex==1) return  currGroup.getName();
        if (columnIndex==2) return  currGroup.getFacultyName();
        return "???";
    }


    /**
     * Дополнительно переопределим метод, возвращающий имена столбцов.
     */
//    @Override
    public  String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0: return "id";
            case 1: return "Название группы";
            case 2: return " Факультет ";
        }
        return "";
    }



    public void addData (Group group) {
        groupsList.add(group);
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
