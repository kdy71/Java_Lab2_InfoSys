package client.view;

import common_model.Group;
import client.model.GroupsTableModel;
import common_model.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by Dmitry Khoruzhenko on 27.02.2016.
 * Visual form for show data
 */
//public class GroupsGUI {
public class GroupsGUI extends TemplateGUI {
//    protected JFrame frame = new JFrame("Группы"); ;
    private GroupsTableModel gtm; // = new GroupsTableModel();
    protected JTable table; // = new JTable(gtm);
    private JScrollPane scrollPaneGroups; // = new JScrollPane(table);

//    private JPanel panelButtons  = new JPanel(new FlowLayout());  // FlowLayout - всё в одном ряду, слева направо. Не хватит места - перенос. Выравн. по центру.
//    protected JPanel panelEdit = new JPanel(new GridLayout(3,3));
//    private JPanel panelStatusBar = new JPanel(new FlowLayout());

//    private JButton addButton = new JButton("Добавить");
//    private JButton selectButton = new JButton("Показать");
//    private JButton editButton = new JButton("Редактировать");
//    private JButton deleteButton = new JButton("Удалить");

    private JLabel lblGrName = new JLabel("Название группы: ");
    private JTextField edGrName = new JTextField(10);
    private JLabel lblFaculty = new JLabel("Факультет: ");
    private JTextField edFaculty = new JTextField(10);
    private JButton btOk = new JButton("Сохранить");
    private JButton btCancel = new JButton("Отменить");

//    private JPanel panelGrid = new JPanel();
//    private JScrollPane scrollPaneGroups = new JScrollPane(panelGrid);
//    private boolean editState = false;

    public GroupsGUI(GroupsTableModel gtm) {
        super();
//        System.out.println("constructor GroupsGUI() "); // debug
        this.gtm = gtm;
        initGroups();
//        frame.setVisible(true);
    }

/*
    public java.util.List<Group> getGroupList(){
        return gtm.getGroupList();
    }
*/

    private void initGroups(){
        frame.setTitle("Группы");
        frame.setLocation(10, 100);
        frame.setSize(440, 300);
        table = new JTable(gtm);
        scrollPaneGroups = new JScrollPane(table);
        panelEdit.setLayout(new GridLayout(3,2));

//        frame.add(panelDataGrid, new GridBagConstraints(0, 0, 3, 1, 1, 1,  // добавляем панель с областью прокрутки ( с таблицей внутри) на форму
        frame.add(scrollPaneGroups, new GridBagConstraints(0, 0, 3, 1, 1, 1,  // добавляем панель с областью прокрутки ( с таблицей внутри) на форму
                GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1), 0, 0));

//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLocationRelativeTo(null);   // окно будет в центре экрана
//        frame.setLayout(new GridBagLayout());  // GridBagLayout  - указывать координаты элементов c.gridx, c.gridy

//        panelButtons.add(addButton);
//        panelButtons.add(selectButton);
//        panelButtons.add(editButton);
//        panelButtons.add(deleteButton);
//        panelButtons.setBackground(Color.GREEN);

        panelEdit.add(lblGrName);
        panelEdit.add(edGrName);
        panelEdit.add(lblFaculty);
        panelEdit.add(edFaculty);
        panelEdit.add(btOk);
        panelEdit.add(btCancel);
        panelEdit.setBackground(Color.CYAN);
        panelEdit.setVisible(false);

//        frame.add(panelButtons, new GridBagConstraints(0, 1, 1, 1, 0, 0,  // добавляем панель с кнопками на форму
//                GridBagConstraints.SOUTH, GridBagConstraints.SOUTH,
//                new Insets(1, 1, 1, 1), 0, 0));

//        frame.add(panelEdit, new GridBagConstraints(0, 2, 1, 1, 0, 0,  // добавляем панель с кнопками на форму
//                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
//                new Insets(1, 1, 1, 1), 0, 0));

//        addButton.addActionListener(new AddButton_ActionListener());
//        editButton.addActionListener(new EditButton_ActionListener());
//        deleteButton.addActionListener(new DelButton_ActionListener());
//        selectButton.addActionListener(new SelectButton_ActionListener());
        btOk.addActionListener(new OkButton_ActionListener());
        btCancel.addActionListener(new CancelButton_ActionListener());


//        Group group1 = new Group(1, "ПЭ - 81", "факультет №1");
//        Group group1 = new Group( "ПЭ - 81", "факультет №1");
        gtm.addData(new Group( "ПЭ - 81", "факультет №1"));
        gtm.addData(new Group( "ПЭ - 81", "факультет №1"));
        gtm.addData(new Group( "ПЭ - 81", "факультет №1"));
//        gtm.addData(group1);

        frame.setVisible(true);
        if(gtm.getRowCount()>0) {table.setRowSelectionInterval(0,0);}  // выделить 1-ю строку грида
    }


    protected void onAddClick(){
//        Group group1 = new Group(2, "ПЭ - 82", "факультет №2");
        Group group1 = new Group( "aaa", "факультет №2");
        gtm.addData(group1);
//        table.setVisible(false);
//        table.setVisible(true);
        gtm.fireTableDataChanged();
        System.out.println("AddClick " + gtm.getRowCount());
    }

    protected void onEditClick(){
//        System.out.println("EditClick");
        if (table.getSelectedRow() == -1) {
            Util.showMessage("Не выбрана строка для редактирования.");
            return;
        }
        if (table.getSelectedRow() == -1) {return;}
        panelButtons.setVisible(false);
        panelEdit.setVisible(true);
        int row = table.getSelectedRow();
        System.out.println("table.getSelectedRow() " + table.getSelectedRow());
        edGrName.setText((String)gtm.getValueAt(row, 1));
        edFaculty.setText((String)gtm.getValueAt(row, 2));
    }

    protected void onDelClick(){
        System.out.println("DelClick");
    }

    protected void onSelectClick(){
        System.out.println("SelectClick");
    }

/*
    private class AddButton_ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            onAddClick();
        }
    }

    private class EditButton_ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            onEditClick();
        }
    }

    private class DelButton_ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            onDelClick();
        }
    }

    private class SelectButton_ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            onSelectClick();
        }
    }
*/

    private class OkButton_ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            panelEdit.setVisible(false);
            panelButtons.setVisible(true);
        }
    }
    private class CancelButton_ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            panelEdit.setVisible(false);
            panelButtons.setVisible(true);
        }
    }


}
