package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import jodd.json.JsonParser;
import jodd.json.JsonSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {

    private String userName;

    @FXML
    ListView todoList;

    @FXML
    TextField todoText;

    ObservableList<ToDoItem> todoItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        todoList.setItems(todoItems);
        Scanner inputScanner = new Scanner(System.in);
        System.out.println("Please enter your name");
        String setName = inputScanner.nextLine();
        setName.substring(0,1).toUpperCase();
        setName.substring(1).toLowerCase();
        setUserName(setName);
        readFromFile();


    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void addOnEnter(KeyEvent e) {
        if(e.getCode().equals(KeyCode.ENTER)) {
            System.out.println("Adding item...");
            todoItems.add(new ToDoItem(todoText.getText()));
            todoText.setText("");
            saveTheList();

        }
    }

    public void addItem() {
        System.out.println("Adding item ...");
        todoItems.add(new ToDoItem(todoText.getText()));
        todoText.setText("");
        saveTheList();
    }

    public void removeItem() {
        System.out.println("Removing the item....");
        ToDoItem todoItem = (ToDoItem)todoList.getSelectionModel().getSelectedItem();
        System.out.println("Getting rid of " + todoItem.text + " ...");
        todoItems.remove(todoItem);
        saveTheList();
    }

    public void toggleItem() {
        System.out.println("Toggling item ...");
        ToDoItem todoItem = (ToDoItem)todoList.getSelectionModel().getSelectedItem();
        if (todoItem != null) {
            todoItem.isDone = !todoItem.isDone;
            todoList.setItems(null);
            todoList.setItems(todoItems);
            saveTheList();
        }
    }

    public void allItemsDone() {
        System.out.println("Everything done...");
        for (ToDoItem item : todoItems) {
            if (item != null) {
                item.isDone = true;
                todoList.setItems(null);
                todoList.setItems(todoItems);
                saveTheList();
            }
        }
    }

    public void nothingDone() {
        System.out.println("Nothing is done!");
        for (ToDoItem item : todoItems) {
            if (item != null) {
                item.isDone = false;
                todoList.setItems(null);
                todoList.setItems(todoItems);
                saveTheList();
            }
        }

    }

    public void toggleAllItems() {
        System.out.println("Toggling...");
        for (ToDoItem item : todoItems) {
            if (item != null) {
                item.isDone = !item.isDone;
                todoList.setItems(null);
                todoList.setItems(todoItems);
                saveTheList();
            }
        }
    }

    public void saveTheList() {
        FileWriter listWriter = null;
        try {
            ListContainer myContainer = new ListContainer();
            File listFile = new File(userName + ".json");
            listWriter = new FileWriter(listFile);
            for (ToDoItem item : todoItems) {
                myContainer.todoArrayList.add(item);
            }
            for (int counter = 0; counter < todoItems.size(); counter++) {
                System.out.println(myContainer.todoArrayList.get(counter));
            }
            listWriter.write(jsonGenerateString(myContainer));

            listWriter.close();
        }
        catch(IOException ioEx){
            ioEx.printStackTrace();
        }
    }

    public void readFromFile() {
        try {
            File listFile = new File(userName + ".json");
            if (listFile.exists()) {
                Scanner listScanner = new Scanner(listFile);

                String list = null;
                list = listScanner.nextLine();

                ListContainer myContainer = jsonRestore(list);
                for (ToDoItem item : myContainer.todoArrayList) {
                    boolean isDone = item.isDone;
                    String text = item.text;
                    todoItems.add(new ToDoItem(text, isDone));
                }
            }

        }
        catch (FileNotFoundException exception) {

        }
    }
    public ListContainer jsonRestore(String jsonTD) {
        JsonParser toDoItemParser = new JsonParser();
        ListContainer item = toDoItemParser.parse(jsonTD, ListContainer.class);

        return item;
    }



    public String jsonGenerateString(ListContainer container) {
        JsonSerializer jsonSerializer = new JsonSerializer().deep(true);
        String jsonString = jsonSerializer.serialize(container);

        return jsonString;
    }




}

