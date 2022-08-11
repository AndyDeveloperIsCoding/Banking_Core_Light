package main;

import databaseOperations.CreateDatabaseAndTable;

public class Main {
    public static void main(String[] args) {

        // Read database name from Main.Main class arguments
        databaseFileName = args[1];
        // Create database at the time of running the program
        CreateDatabaseAndTable.createDatabaseAndTable();

        //Launch User Interface
        MainMenuUi.mainMenu();
    }

    public static String databaseFileName = "";
    public static double accountBalance;
    public static boolean loggedOut = false;
}