package Demo;

import client.model.ISClient;
import client.model.XmlClientOperations;

/**
 * Created by Oleksandr Dudkin on 16.03.2016.
 */
public class DemoFind {

    public static void main(String[] args) {

        //закидываем в клиент
        ISClient client = new ISClient(XmlClientOperations.findGroups(null, "E-91", "Econom"));

        ISClient client2 = new ISClient(XmlClientOperations.findStudents(null, "Ivanov", 1, null)); //todo разобраться с датой, как ее обработать

    }
}

