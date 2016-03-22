package client.view;

import client.model.StudentsTableModel;
import common_model.Student;
import common_model.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * Created by Dmitry Khoruzhenko on 01.03.2016.
 * GUI students
 */
public class StudentsGUI extends TemplateGUI {

    //    private  List<Group> groups;  // список групп
    private StudentsTableModel stm; // = new StudentsTableModel(); // = new StudentsTableModel();
    private JScrollPane scrollPaneStudents = null;
    private JTable table; //=  new JTable(stm);

    private JLabel lblFIO = new JLabel("Ф.И.О.: ");
    private JTextField edFIO = new JTextField(30);
    private JLabel lblGroup = new JLabel("Группа: ");
    private JTextField edGroup = new JTextField(10);
    private JLabel lblEnrollmentDate = new JLabel("Дата поступления: ");
    private JTextField edEnrollmentDate = new JTextField(10);
    private JButton btOk = new JButton("Сохранить");
    private JButton btCancel = new JButton("Отменить");
    private Integer editingStudentId = null;
//    private IoInterface io ;

/*
    public StudentsGUI() {
        super();
        this.stm = new StudentsTableModel();
        initStudents();
    }
*/

    public StudentsGUI(StudentsTableModel stm) {
        this.stm = stm;
//        io = new IoXML(stm.getAdminInterface());
        initStudents();
    }


    private void initStudents() {
        super.frame.setTitle("Студенты");

        table = new JTable(stm);
        scrollPaneStudents = new JScrollPane(table);

        frame.add(scrollPaneStudents, new GridBagConstraints(0, 0, 3, 1, 1, 1,  // добавляем панель с областью прокрутки ( с таблицей внутри) на форму
                GridBagConstraints.NORTH, GridBagConstraints.BOTH,
                new Insets(1, 1, 1, 1), 0, 0));

        frame.setLocation(450, 100);
        panelEdit.add(lblFIO);
        panelEdit.add(edFIO);
        panelEdit.add(lblGroup);
        panelEdit.add(edGroup);
        panelEdit.add(lblEnrollmentDate);
        panelEdit.add(edEnrollmentDate);
        panelEdit.add(btOk);
        panelEdit.add(btCancel);
        panelEdit.setBackground(Color.CYAN);
        panelEdit.setVisible(false);

        btOk.addActionListener(new OkButton_ActionListener());
        btCancel.addActionListener(new CancelButton_ActionListener());

//        stm.addData(new Student(111, "Иванов И.И.", new Date(), 0));
//        stm.addData(new Student(2, "Арбузов А.А.", new Date(), 1));
//        stm.addData(new Student(3, "Барабанов Б.Б.", new Date(), 1));
//        stm.addData(new Student(1, "Виноградов В.В.", new Date(), 2));
        stm.sortStudentsByName();
        setDataState(DS_BROWSE);
        frame.setVisible(true);

        if (stm.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }  // выделить 1-ю строку грида
    }

    @Override
    protected void onAddClick() {
//        System.out.println("AddClick ");
        setDataState(DS_ADD);
        super.onAddClick();
//        Student student1 = new Student("Петров П.П.", new Date(), 1);
//        stm.addData(student1);
//        stm.fireTableDataChanged();  // перерисовать таблицу
        System.out.println("AddClick " + stm.getRowCount());

        edEnrollmentDate.setText(Util.dat2Str(new Date()));
        editingStudentId = null;
    }

    @Override
    protected void onEditClick() {
//        System.out.println("EditStudent_Click");
        int row = table.getSelectedRow();
        if (row == -1) {
            Util.showMessage("Не выбрана строка для редактирования.");
            return;
        }
        setDataState(DS_EDIT);
        System.out.println("table.getSelectedRow() " + table.getSelectedRow());
        edFIO.setText((String) stm.getValueAt(row, 1));
        edEnrollmentDate.setText((String) stm.getValueAt(row, 2));
        edGroup.setText((String) stm.getValueAt(row, 3));
        editingStudentId = (Integer) stm.getValueAt(row, 0);
    }

    @Override
    protected void onDelClick() {
//        System.out.println("DelClick");
        int row = table.getSelectedRow();
        if (row == -1) {
            Util.showMessage("Не выбрана строка для удаления.");
            return;
        }
        boolean answYes = Util.showYesNoMessage("Точно удалить студента?");
        if (answYes) {
            int id4del = (Integer) stm.getValueAt(row, 0);
            stm.deleteStudent(id4del);
            stm.selectStudents(new Student(null,null,null,null));
        }
    }

    @Override
    protected void onSelectClick() {
        System.out.println("SelectClick " + stm.getRowCount());
        stm.selectStudents(new Student(null,null,null,null));
    }


    private class OkButton_ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
//            Boolean checkOk = stm.checkAndSaveStudent(edFIO.getText(), edEnrollmentDate.getText(), edGroup.getText());
            Student editedStudent = stm.checkAndSaveStudent(editingStudentId, edFIO.getText(), edEnrollmentDate.getText(), edGroup.getText());
            stm.selectStudents(new Student(null,null,null,null));
            if (editedStudent != null) {
                setDataState(DS_BROWSE);
            }
        }
    }

    private class CancelButton_ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            setDataState(DS_BROWSE);
        }
    }

}
