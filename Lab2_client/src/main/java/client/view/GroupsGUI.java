package client.view;

import client.model.GroupsTableModel;
import common_model.Group;
import common_model.Util_msg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Dmitry Khoruzhenko on 27.02.2016.
 * GUI groups/ Visual form for show data
 */
public class GroupsGUI extends TemplateGUI {
    private GroupsTableModel gtm; // = new GroupsTableModel();
    protected JTable table; // = new JTable(gtm);
    private JScrollPane scrollPaneGroups; // = new JScrollPane(table);
    private JLabel lblGrName = new JLabel("Название группы: ");
    private JTextField edGrName = new JTextField(10);
    private JLabel lblFaculty = new JLabel("Факультет: ");
    private JTextField edFaculty = new JTextField(10);
    private JButton btOk = new JButton("Сохранить");
    private JButton btCancel = new JButton("Отменить");
    private Integer editingGroupId = null;


    /**
     * Конструктор
     * @param gtm  - ссылка на объект класса GroupsTableModel (для управления данными)
     */
    public GroupsGUI(GroupsTableModel gtm) {
        super();
        this.gtm = gtm;
        initGroups();
    }


    /**
     * Инициализация.
     * Расставляем на форме визуальные компоненты
     */
    private void initGroups() {
        frame.setTitle("Группы");
        frame.setLocation(10, 100);
        frame.setSize(440, 300);
        table = new JTable(gtm);
        scrollPaneGroups = new JScrollPane(table);
        panelEdit.setLayout(new GridLayout(3, 2));
        frame.add(scrollPaneGroups, new GridBagConstraints(0, 0, 3, 1, 1, 1,  // добавляем панель с областью прокрутки ( с таблицей внутри) на форму
                GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1), 0, 0));

        panelEdit.add(lblGrName);
        panelEdit.add(edGrName);
        panelEdit.add(lblFaculty);
        panelEdit.add(edFaculty);
        panelEdit.add(btOk);
        panelEdit.add(btCancel);
        panelEdit.setBackground(Color.CYAN);
        panelEdit.setVisible(false);

        btOk.addActionListener(new OkButton_ActionListener());
        btCancel.addActionListener(new CancelButton_ActionListener());

        frame.setVisible(true);
        if (gtm.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }  // выделить 1-ю строку грида
    }


    /**
     * Обработка нажатия кнопки "Добавить"
     */
    protected void onAddClick() {
        setDataState(DS_ADD);
        editingGroupId = null;
        System.out.println("AddClick " + gtm.getRowCount());
    }


    /**
     * Обработка нажатия кнопки "Редактировать"
     */
    protected void onEditClick() {
        int row = table.getSelectedRow();
        if (row == -1) {
            Util_msg.showMessage("Не выбрана строка для редактирования.");
            return;
        }
        setDataState(DS_EDIT);
        edGrName.setText((String) gtm.getValueAt(row, 1));
        edFaculty.setText((String) gtm.getValueAt(row, 2));
        editingGroupId = (Integer) gtm.getValueAt(row, 0);
    }


    /**
     * Обработка нажатия кнопки "Удалить"
     */
    protected void onDelClick() {
        int row = table.getSelectedRow();
        if (row == -1) {
            Util_msg.showMessage("Не выбрана строка для удаления.");
            return;
        }
        boolean answYes = Util_msg.showYesNoMessage("Точно удалить группу?");
        if (answYes) {
            int id4del = (Integer) gtm.getValueAt(row, 0);
            gtm.deleteGroup(id4del);
            gtm.selectGroups(new Group(null, null, null));
        }
    }


    /**
     * Обработка нажатия кнопки "Показать"
     */
    protected void onSelectClick() {
        System.out.println("SelectClick");
        gtm.selectGroups(new Group(null, null, null));
    }


    /**
     * Слушатель и обработчик нажатия кнопки "Сохранить" на панели редактирования
     */
    private class OkButton_ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            Group editedGroup = gtm.checkSndSaveGroup(editingGroupId, edGrName.getText(), edFaculty.getText());
            gtm.selectGroups(new Group(null, null, null));
            if (editedGroup != null) {
                setDataState(DS_BROWSE);
            }
        }
    }


    /**
     * Слушатель и обработчик нажатия кнопки "Отменить" на панели редактирования
     */
    private class CancelButton_ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            setDataState(DS_BROWSE);
        }
    }

}
