package client.model;

import common_model.AdministrationInterface;
import common_model.Group;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.table.AbstractTableModel;

/**
 * Created by Dmitry Khoruzhenko on 02.03.2016.
 * TableModel for Groups
 */
public class GroupsTableModel extends AbstractTableModel {

    public static final Logger log = LogManager.getLogger(GroupsTableModel.class);

    private static final long serialVersionUID = 2000L;
    private final int columnCount = 3;
    private AdministrationInterface admin;
    private IoInterface io; // = new IoXML(admin);
    private Group searchTemplate = new Group(null, null, null);  // шаблон поиска


    /**
     * Конструктор
     * @param admin  - ссылка на объект класса AdminInterface
     * @param io     - ссылка на объект класса IoInterface
     */
    public GroupsTableModel(AdministrationInterface admin, IoInterface io) {  // constructor
        super();
        this.admin = admin;
        this.io = io;
        io.selectGroups(searchTemplate); // // запрос серверу на получение списка  групп
        log.info("GroupsTableModel constructor.");
    }


    /**
     * Возвращает число строк в списке групп
     * @return  -количество групп
     */
    @Override
    public int getRowCount() {
        return admin.getGroupsCount();
    }


    /**
     * Возвращает количество столбцов грида групп
     * @return  - количество столбцов грида групп
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
        Group currGroup = admin.getGroupByIndex(rowIndex);
        if (columnIndex == 0) return  currGroup.getId();
        if (columnIndex == 1) return  currGroup.getName();
        if (columnIndex == 2) return  currGroup.getFacultyName();
        return "???";
    }


    /**
     * Дополнительно переопределим метод, возвращающий имена столбцов.
     */
    @Override
    public  String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0: return "id";
            case 1: return "Название группы";
            case 2: return " Факультет ";
        }
        return "";
    }


    /**
     * Добавляет группу в список групп
     * @param group  - добавляемая группа
     */
    public void addData (Group group) {
        admin.addGroup(group);
    }


    /**
     * Сохраняет изменения в группе (т.е. отправляет их на сервер)
     * @param id  - id сохраняемой группы. (Если id==null - будет добавление новой группы и генерация id на сервере)
     * @param grName  - название сохраняемой группы
     * @param facultyName  - название факультета сохраняемой группы
     * @return   - новая группа, созданная по заданным атрибутам
     */
    public Group checkSndSaveGroup(Integer id, String grName, String facultyName){
        Group newGroup = new Group(id, grName, facultyName);
        io.saveGroup(newGroup);
        log.info("New group was added in the form.");
        return  newGroup;
    }


    /**
     * @return  шаблон поиска групп
     */
    public Group getSearchTemplate() {
        return searchTemplate;
    }


    /**
     * Установка шаблона поиска + запрос серверу на select
     * @param searchTemplate - шаблон поиска
     */
    public void selectGroups(Group searchTemplate) {
        this.searchTemplate = searchTemplate;
        io.selectGroups(searchTemplate);
        log.info("Group was selected in the form.");
    }


    /**
     * Посылает команду на сервер "Удалить группу"
     * @param id4del - id удаляемой группі
     */
    public void deleteGroup (Integer id4del) {
        log.info("Group was chosen to delete in the form.");
        io.deleteGroup(id4del);
    }


    /**
     * Перерисовывает грид. Вызывается при изменении данных.
     */
    public void refreshGrid(){
        fireTableDataChanged();
    }
}
