package Demo;

import common_model.Group;

/**
 * Created by Oleksandr Dudkin on 21.02.2016.
 */
public class DemoGroup {

    public static void main(String[] args) {
        Group group = new Group("E-91", "Econom");

        System.out.println(group.getId());
        System.out.println(group.getName());
        System.out.println(group.getFacultyName());

        System.out.println("");
        group.setId(8);
        group.setName("Z-91");
        group.setFacultyName("Gumfak");
        System.out.println(group);

    }
}
