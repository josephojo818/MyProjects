package bmt;

import java.util.Scanner;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class GothamBank {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> cartCollection;
    private static Scanner sc;

    public static void main(String[] args){
        connectToMongoDB();
        sc = new Scanner(System.in);

        while (true) { 
            System.out.println("-----Welcome To Gotham Bank-----");
            System.out.println("1. Login");
            System.out.println("2. Make an account");
            System.out.println("3. Remove Account");
            System.out.println("4. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1:
                    Login();
                    break;
                case 2:
                    SignUp();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    closeMongoConnection();
                    System.exit(0);
                    break;
                case 3:
                    removeAccount(); // Optional feature
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void connectToMongoDB(){
        try {
            String uri = "mongodb+srv://cobrasiddharthgawai:vaishnawi25092008@mong.q6olbb5.mongodb.net/cartDB?retryWrites=true&w=majority";
            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase("cartDB");
            cartCollection = database.getCollection("cart");
            System.out.println("Connected to server.");
        }
        catch (Exception e) {
            System.out.println("Error connecting to server." + e.getMessage());
        }
    }

    private static void Login(){
        System.out.println("Enter your gmail: ");
        String gmail = sc.nextLine();

        System.out.println("Enter your number: ");
        Long number = sc.nextLong();
        sc.nextLine();

        System.out.println("Enter your password: ");
        String password = sc.nextLine();

        Document query = new Document("gmail", gmail)
            .append("number", number)
            .append("password", password);
        Document user = cartCollection.find(query).first();

        if(user != null){
            System.out.println("Login successful! Welcome " + user.getString("name"));
        } else {
            System.out.println("Invalid credentials. Try again!");
        }
    }

    private static void SignUp(){
        System.out.println("Enter your first name: ");
        String name = sc.nextLine();

        System.out.println("Enter your last name: ");
        String lastName = sc.nextLine();

        System.out.println("Enter your mobile number: ");
        Long number = sc.nextLong();
        sc.nextLine(); // consume the newline character

        System.out.println("Enter your Gmail Id: ");
        String gmail = sc.nextLine();

        System.out.println("Enter your password: ");
        String password = sc.nextLine();

        Document user = new Document("name", name)
            .append("Last_name", lastName)
            .append("number", number)
            .append("gmail", gmail)
            .append("password", password)
            .append("date", java.time.Instant.now().toString());

        cartCollection.insertOne(user);
        System.out.println("Account created successfully!");
    }

    private static void removeAccount(){
        System.out.println("Enter your gmail: ");
        String gmail = sc.nextLine();

        System.out.println("Enter your number: ");
        Long number = sc.nextLong();
        sc.nextLine(); // consume the newline character

        Document query = new Document("gmail", gmail).append("number", number);
        Document user = cartCollection.find(query).first();

        if(user != null){
            try{
                System.out.println("Enter your password: ");
                String password = sc.nextLine();
                String user_password = user.getString("password");

                if(user_password != null && user_password.equals(password)){
                    System.out.println("Password matched.");
                    System.out.println("Account removed successfully.");
                    cartCollection.deleteOne(user);
                } else {
                    System.out.println("Wrong Password. Try again!");
                }
            } catch(Exception e){
                e.printStackTrace(); // better to print stack trace
                System.out.println("Error while handling user data: " + e.getMessage());
            }
        }
    }

    private static void closeMongoConnection(){
        if(mongoClient != null){
            mongoClient.close();
            System.out.println("MongoDB closing....");
        }
    }
}
