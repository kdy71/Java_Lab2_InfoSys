package client.model;

import common_model.AdminInterface;
import common_model.Group;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Created by Dmitry Khoruzhenko on 02.03.2016.
 * TableModel for Groups
 */
public class GroupsTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 2000L;
    private final int columnCount = 3;
//    private List<Group> groupsList; // = new ArrayList<Group>();  // хранилище данных
    private AdminInterface admin;
    private IoInterface io = new IoXML(admin);

    public GroupsTableModel(AdminInterface admin) {  // constructor
        super();
        this.admin = admin;
//        groupsList = admin.getGroups();
    }

    public GroupsTableModel() {  // constructor
        super();
//        this.groups = new Groups();
    }

    public AdminInterface getAdminInterface(){ return admin;}


    //    @Override
    public int getRowCount() {
        return admin.getGroupsCount();
    }

    //    @Override
    public int getColumnCount() {
        return columnCount;
    }

    //    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Group currGroup = admin.getGroupByIndex(rowIndex);
        if (columnIndex == 0) return  currGroup.getId();
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
        admin.addGroup(group);
    }

    public Group checkSndSaveGroup(Integer id, String grName, String facultyName){

        Group newGroup = new Group(id, grName, facultyName);
        io.saveGroup(newGroup);
        return  newGroup;
    }

}
