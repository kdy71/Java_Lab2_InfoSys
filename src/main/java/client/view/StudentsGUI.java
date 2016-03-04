package client.view;

import client.model.StudentsTableModel;
import common_model.Student;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * Created by Dmitry Khoruzhenko on 01.03.2016.
 * GUI students
 */
public class StudentsGUI extends TemplateGUI {

    private JLabel lblGroup = new JLabel("Группа:");
    private JTextField edGroup = new JTextField(" группа ");
    private StudentsTableModel stm = new StudentsTableModel(); // = new StudentsTableModel();
    private JScrollPane scrollPaneStudents = null;
//  JTable table =  new JTable(stm);
    JTable table =  new JTable(stm);


    public StudentsGUI() {
        super();
//        table = new JTable(stm);
        System.out.println("constructor StudentsGUI_ "); // debug
        initStudents();
    }

    private void initStudents() {
        super.frame.setTitle("Студенты");

        scrollPaneStudents = new JScrollPane(table);

        frame.add(scrollPaneStudents, new GridBagConstraints(0, 0, 3, 1, 1, 1,  // добавляем панель с областью прокрутки ( с таблицей внутри) на форму
                GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1), 0, 0));

        frame.setLocation(450, 100);
        super.panelEdit.add(lblGroup);
        super.panelEdit.add(edGroup);

        Student student = new Student(111, "Иванов", new Date(), "ПЭ - 81");
        stm.addData(student);
        stm.addData(student);
        frame.setVisible(true);
    }

    @Override
    protected void onAddClick(){
//        System.out.println("AddClick");
//        setEditState(true);
        super.onAddClick();
        Student student1 = new Student(2,"Петров П.П.", new Date(), 1);
        stm.addData(student1);
        stm.fireTableDataChanged();  // перерисовать таблицу
        System.out.println("AddClick " + stm.getRowCount());
    }

    @Override
    protected void onEditClick(){
//        System.out.println("EditClick");
//        setEditState(true);
        super.onEditClick();
    }

    @Override
    protected void onDelClick(){
//        System.out.println("DelClick");
        super.onDelClick();
    }

    @Override
    protected void onSelectClick(){
//        System.out.println("SelectClick");
//        super.onSelectClick();
        System.out.println("SelectClick " + stm.getRowCount());
    }


}
