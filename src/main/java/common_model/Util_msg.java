package common_model;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilites and constants for Lab 1 (Task Manager)
 * Created by Dmitry Khoruzhenko on 08.01.2016.
 */
public class Util_msg {

    public static void showError(String message) {
        JOptionPane.showMessageDialog(new JFrame(), message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(new JFrame(), message, "Внимание",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean showYesNoMessage(String message) {
        Object[] options = {"Да", "Нет!"};
        int n = JOptionPane.showOptionDialog(new JFrame(), message,
                "Подтверждение", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return (n == 0);
    }

}
