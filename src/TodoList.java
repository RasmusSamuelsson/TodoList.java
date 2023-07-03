import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class TodoItem implements Serializable {
    private String name;
    private boolean completed;

    public TodoItem(String name) {
        this.name = name;
        this.completed = false;
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        completed = true;
    }
}

public class TodoList {
    private List<TodoItem> todoItems;

    public TodoList() {
        todoItems = new ArrayList<>();
    }

    public void addTodoItem(String name) {
        TodoItem todoItem = new TodoItem(name);
        todoItems.add(todoItem);
        System.out.println("Uppgift tillagd: " + name);
    }

    public void markTodoItemCompleted(int index) {
        if (index >= 0 && index < todoItems.size()) {
            TodoItem todoItem = todoItems.get(index);
            todoItem.markCompleted();
            System.out.println("Uppgift markerad som färdig: " + todoItem.getName());
        } else {
            System.out.println("Ogiltigt index.");
        }
    }

    public void removeTodoItem(int index) {
        if (index >= 0 && index < todoItems.size()) {
            TodoItem removedItem = todoItems.remove(index);
            System.out.println("Uppgift borttagen: " + removedItem.getName());
        } else {
            System.out.println("Ogiltigt index.");
        }
    }

    public void showTodoList() {
        if (todoItems.isEmpty()) {
            System.out.println("ToDo-listan är tom.");
        } else {
            System.out.println("ToDo-lista:");
            for (int i = 0; i < todoItems.size(); i++) {
                TodoItem todoItem = todoItems.get(i);
                String status = todoItem.isCompleted() ? "[X]" : "[ ]";
                System.out.println(i + ". " + status + " " + todoItem.getName());
            }
        }
    }

    public void saveTodoList(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(todoItems);
            System.out.println("ToDo-listan sparades till filen: " + fileName);
        } catch (IOException e) {
            System.out.println("Fel vid sparande av ToDo-listan: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadTodoList(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            todoItems = (List<TodoItem>) ois.readObject();
            System.out.println("ToDo-listan lästes in från filen: " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Fel vid inläsning av ToDo-listan: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TodoList todoList = new TodoList();
        String fileName = "todolist.ser"; // Filnamnet för att spara/läsa in ToDo-listan

        // Läs in ToDo-listan från filen (om den finns)
        todoList.loadTodoList(fileName);

        while (true) {
            System.out.println("Välkommen till ToDo-listan!");
            System.out.println("Välj en åtgärd:");
            System.out.println("1. Lägg till uppgift");
            System.out.println("2. Markera uppgift som färdig");
            System.out.println("3. Ta bort uppgift");
            System.out.println("4. Visa ToDo-lista");
            System.out.println("5. Spara ToDo-listan och avsluta");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Rensa input-buffer

            switch (choice) {
                case 1:
                    System.out.print("Ange namn på uppgiften: ");
                    String taskName = scanner.nextLine();
                    todoList.addTodoItem(taskName);
                    break;
                case 2:
                    System.out.print("Ange index för uppgiften att markera som färdig: ");
                    int completedIndex = scanner.nextInt();
                    todoList.markTodoItemCompleted(completedIndex);
                    break;
                case 3:
                    System.out.print("Ange index för uppgiften att ta bort: ");
                    int removeIndex = scanner.nextInt();
                    todoList.removeTodoItem(removeIndex);
                    break;
                case 4:
                    todoList.showTodoList();
                    break;
                case 5:
                    todoList.saveTodoList(fileName);
                    System.out.println("Programmet avslutas.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Ogiltigt val.");
                    break;
            }
        }
    }
}