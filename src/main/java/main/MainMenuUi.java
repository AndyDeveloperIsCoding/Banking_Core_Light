package main;

import databaseOperations.ReadFromDatabase;
import databaseOperations.WriteToDatabase;

import java.util.Scanner;

public class MainMenuUi {

    public static void mainMenu() {

        Scanner scanner = new Scanner(System.in);
        while (Main.loggedOut == false) {

            System.out.println("");
            System.out.println("Welcome! Please make a choice:");
            System.out.println("");
            System.out.println("1. Create an account");
            System.out.println("2. Display account information");
            System.out.println("3. Make a transaction");
            System.out.println("4. Review a transaction"); // Previously: Generate account statement
            System.out.println("0. Exit");

            String userChoice = scanner.next();

            switch (userChoice) {
                case "1":
                    MainMenuMethods.createAccount();
                    break;
                case "2":
                    ReadFromDatabase.getAccount();
                    break;
                case "3":
                    MainMenuMethods.createTransaction();
                    break;
                case "4":
                    MainMenuMethods.getTransaction();
                    break;
                case "0":
                    MainMenuMethods.systemExit();
                    break;
                default:
                    System.out.println("Unknown command");
                    mainMenu();
                    break;
            }

        }
        scanner.close();
    }

}

