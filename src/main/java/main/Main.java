package main;

import databaseOperations.CreateDatabaseAndTable;
import databaseOperations.ReadFromDatabase;

public class Main {
    public static void main(String[] args) {


        // Read database name from Main.Main class arguments
        databaseFileName = args[1];
        // Create database at the time of running the program
        CreateDatabaseAndTable.createDatabaseAndTable();

        //TEMP ReadFromDatabase.readCurrencyBalance(2306, "SEK");
        //TEMP System.out.println("SEK balance for account 2306 is " + specificCurrencyBalance);

        //Launch User Interface
        MainMenuUi.mainMenu();
    }

    public static String databaseFileName = "";
    public static int specificCurrencyBalance = -1;
    public static boolean loggedOut = false;
}