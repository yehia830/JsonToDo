package sample;

import java.util.ArrayList;

/**
 * Created by Yehia830 on 9/1/16.
 */
public class Person {
    private String name;
    public ArrayList<ToDoItem> arrayListofToDos = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Person() {

    }
}
