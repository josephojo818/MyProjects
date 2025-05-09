package bmt;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;


class MongoConnection{
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static MongoDatabase connect(){
        if(database == null){
            String uri = "";
            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase("Chores");
            System.out.println("Connected to MongoDB.");
        }
        return database;
    }
    public static void close(){
        if(mongoClient != null){
            mongoClient.close();
            System.out.println("Disconnected to MongoDB.");
        }
    }
}