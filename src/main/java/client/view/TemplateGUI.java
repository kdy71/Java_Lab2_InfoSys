package client.view;

import common_model.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Dmitry Khoruzhenko on 27.02.2016.
 * Visual form for show data
 */
public class TemplateGUI {
    public final int DS_BROWSE = 1;
    public final int DS_EDIT = 2;
    public final int DS_ADD = 3;
    private int dataState = DS_BROWSE;

    protected JFrame frame = new JFrame("Template"); ;
    protected JScrollPane scrollPane = new JScrollPane();

    protected JPanel panelButtons  = new JPanel(new FlowLayout());  // FlowLayout - всё в одном ряду, слева направо. Не хватит места - перенос. Выравн. по центру.
    protected JPanel panelEdit = new JPanel(new GridLayout(4,2) ); //(new FlowLayout());
    protected JPanel panelStatusBar = new JPanel(new FlowLayout());

    private JButton addButton = new JButton("Добавить");
    private JButton selectButton = new JButton("Показать");
    private JButton editButton = new JButton("Редактировать");
    private JButton deleteButton = new JButton("Удалить");


    public TemplateGUI() {
        super();
        System.out.println("constructor TemplateGUI_ "); // debug
        init();
    }


    /**
     * Управляет видимостью панели редактирования и панели с кнопками
     * @param state (true/false) - редактирование или нет
     */
    protected void setDataState(int state) {
        dataState = state;
        panelEdit.setVisible(dataState != DS_BROWSE);
        panelButtons.setVisible(dataState == DS_BROWSE);
    }

    public int getDataState() {
        return dataState;
    }


/*
    protected JTable getTable(){
        if (this.table != null) {return this.table;}
        return new JTable();
    }

    protected  void setTable (JTable tbl) {
        this.table = tbl;
    }
*/

    private void init(){
        frame.setSize(500,300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLocation(100,100);
        frame.setLayout(new GridBagLayout());  // GridBagLayout  - указывать координаты элементов c.gridx, c.gridy
        addButton.setToolTipText("Добавить запись");
        editButton.setToolTipText("Редактировать запись");
        deleteButton.setToolTipText("Удалить запись");
        selectButton.setToolTipText("Обновить данные на экране");

        panelButtons.add(addButton);
        panelButtons.add(selectButton);
        panelButtons.add(editButton);
        panelButtons.add(deleteButton);
//        panelButtons.setBackground(Color.GREEN);

//        panelEdit.add(lblName);
//        panelEdit.add(edName);
/*
        frame.add(scrollPane, new GridBagConstraints(0, 0, 3, 1, 1, 1,  // добавляем панель с областью прокрутки ( с таблицей внутри) на форму
                GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1), 0, 0));
*/
        frame.add(panelButtons, new GridBagConstraints(0, 1, 1, 1, 0, 0,  // добавляем панель с кнопками на форму
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0));

        frame.add(panelEdit, new GridBagConstraints(0, 2, 1, 1, 0, 0,  // добавляем редактирования на форму
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                new Insets(1, 1, 1, 1), 0, 0));

        addButton.addActionListener(new AddButton_ActionListener());
        editButton.addActionListener(new EditButton_ActionListener());
        deleteButton.addActionListener(new DelButton_ActionListener());
        selectButton.addActionListener(new SelectButton_ActionListener());

        setDataState(DS_BROWSE);

    }


    protected void onAddClick(){
//        System.out.println("AddClick");
        setDataState(DS_ADD);
    }

    protected void onEditClick(){
//        System.out.println("EditClick");
        setDataState(DS_EDIT);
    }

    protected void onDelClick(){
//        System.out.println("DelClick");
    }

    protected void onSelectClick(){
//        System.out.println("SelectClick");
    }


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


}
