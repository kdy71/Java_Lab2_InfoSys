package client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Dmitry Khoruzhenko on 27.02.2016.
 * Visual form template for show data
 */
public abstract class TemplateGUI {
    public final int DS_BROWSE = 1;
    public final int DS_EDIT = 2;
    public final int DS_ADD = 3;
    private int dataState = DS_BROWSE;

    protected JFrame frame = new JFrame("Template");

    protected JPanel panelButtons = new JPanel(new FlowLayout());  // FlowLayout - всё в одном ряду, слева направо. Не хватит места - перенос. Выравн. по центру.
    protected JPanel panelEdit = new JPanel(new GridLayout(4, 2)); //(new FlowLayout());

    private JButton addButton = new JButton("Добавить");
    private JButton selectButton = new JButton("Показать");
    private JButton editButton = new JButton("Редактировать");
    private JButton deleteButton = new JButton("Удалить");


    /**
     * Конструктор
     */
    public TemplateGUI() {
        super();
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


    /**
     * Возвращает состояние дабора данных
     * @return - состояние дабора данных
     */
    public int getDataState() {
        return dataState;
    }


    /**
     * Инициализация GUI
     */
    private void init() {
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());  // GridBagLayout  - указывать координаты элементов c.gridx, c.gridy
        addButton.setToolTipText("Добавить запись");
        editButton.setToolTipText("Редактировать запись");
        deleteButton.setToolTipText("Удалить запись");
        selectButton.setToolTipText("Обновить данные на экране");

        panelButtons.add(addButton);
        panelButtons.add(selectButton);
        panelButtons.add(editButton);
        panelButtons.add(deleteButton);

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


    /**
     * Обработка нажатия кнопки "Добавить"
     */
    protected void onAddClick() {
        setDataState(DS_ADD);
    }


    /**
     * Обработка нажатия кнопки "Редактировать"
     */
    protected void onEditClick() {
        setDataState(DS_EDIT);
    }


    /**
     * Обработка нажатия кнопки "Удалить"
     */
    protected abstract void onDelClick();


    /**
     * Обработка нажатия кнопки "Показать"
     */
    protected abstract void onSelectClick();


    /**
     * слушатель нажатия кнопки "Добавить"
     */
    private class AddButton_ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            onAddClick();
        }
    }


    /**
     * слушатель нажатия кнопки "Редактировать"
     */
    private class EditButton_ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            onEditClick();
        }
    }


    /**
     * слушатель нажатия кнопки "Удалить"
     */
    private class DelButton_ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            onDelClick();
        }
    }


    /**
     * слушатель нажатия кнопки "Показать"
     */
    private class SelectButton_ActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            onSelectClick();
        }
    }

}
