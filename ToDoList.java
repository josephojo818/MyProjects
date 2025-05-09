package bmt;

import java.util.Scanner;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ToDoList {

    private static Scanner sc;
    private static MongoCollection<Document> chores;

    public static void main(String[] args) {
        // Connect to MongoDB
        MongoClient mongoClient = MongoClients.create("uri");
        MongoDatabase db = mongoClient.getDatabase("Task");
        chores = db.getCollection("chores");
        sc = new Scanner(System.in);

        while (true) {
            System.out.println("----Welcome to chores app----");
            System.out.println("1. Add a task.");
            System.out.println("2. Delete a task.");
            System.out.println("3. Mark a task done.");
            System.out.println("4. See available tasks.");
            System.out.println("5. Exit.");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    deleteTask();
                    break;
                case 3:
                    markTask();
                    break;
                case 4:
                    displayTasks();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Enter a valid choice.");
            }
        }
    }

    private static void addTask() {
        System.out.println("Enter your task: ");
        String task = sc.nextLine();
        Document query = new Document("Task", task)
                .append("date", java.time.Instant.now().toString());

        chores.insertOne(query);
        System.out.println("Task added successfully.");
    }

    private static void deleteTask(){
        System.out.println("Enter your task: ");
        String task = sc.nextLine();

        Document query = new Document("Task", task);
        chores.deleteOne(query);
        System.out.println("Task deleted successfully.");
    }

    private static void markTask() {
        System.out.println("Enter the task to mark as done: ");
        String taskName = sc.nextLine();
    
        Document filter = new Document("Task", taskName);
        Document update = new Document("$set", new Document("done", true));
    
        if (chores.updateOne(filter, update).getModifiedCount() > 0) {
            System.out.println("Task marked as done.");
        } else {
            System.out.println("Task not found.");
        }
    }
    private static void displayTasks() {
        System.out.println("----Your tasks are following----");
    
        for (Document doc : chores.find()) {
            String taskName = doc.getString("Task");
            boolean isDone = doc.getBoolean("done", false); // default to false if not set
    
            System.out.println("- " + taskName + (isDone ? " (Done)" : ""));
        }
    }
    
}
