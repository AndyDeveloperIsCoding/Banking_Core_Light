package main;

import databaseOperations.CreateDatabaseAndTable;
import databaseOperations.Database;
import databaseOperations.ReadFromDatabase;

public class Main {
    public static void main(String[] args) {



        // Read database name from Main.Main class arguments
        databaseFileName = args[1];

        // Create database at the time of running the program
        CreateDatabaseAndTable.createDatabaseAndTable();

        //Launch User Interface
        new MainMenuUi().mainMenu();
    }

    public static String databaseFileName = "";
    public static int specificCurrencyBalance = -1;
    public static boolean loggedOut = false;
}