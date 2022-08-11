package main;

import objects.Account;

import java.util.Arrays;
import java.util.Scanner;

public class MainMenuMethods {

    public static void systemExit() {
        Main.loggedOut = true;
        System.out.println("Thank You for using this software! See You next time");
        return;
    }

    public static void createAccount() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("What is the customer's country of tax residence?");
        String country = scanner.nextLine();
        askForCurrencies(scanner);

        //String[] properCurrencies = new String[]{"EUR", "SEK", "GBP", "USD"};



        // System.out.println(Arrays.toString(currenciesList));

        Account account = new Account(country);
        System.out.println("");
        System.out.println("New account has been created!");
        System.out.println("");
        System.out.println("AccountID: " + account.customerID);
        System.out.println("CustomerID: " + account.accountID);
        System.out.println("Your account balance is:");
        System.out.println("0 EUR");
        System.out.println("0 SEK");
        System.out.println("0 GBP");
        System.out.println("0 USD");

        System.out.println("");
        MainMenuUi.mainMenu();
    }

    private static void askForCurrencies(Scanner scanner) {

        // Process the currencies as Array

        while(!currenciesCorrect) {
            System.out.println("Choose the accepted currencies for the account. Available currencies: EUR, SEK, GBP, USD:");
            String currencies = scanner.nextLine();
            String[] currenciesList;
            currenciesList = currencies.split(",");

            for (int i = 0; i < currenciesList.length; i++) {
                currenciesList[i] = currenciesList[i].trim();
                if (!(currenciesList[i].toLowerCase().equals("eur") || currenciesList[i].toLowerCase().contains("sek") || currenciesList[i].toLowerCase().equals("gbp") || currenciesList[i].toLowerCase().equals("usd"))) {
                    System.out.println("You have entered an incorrect currency.");
                    askForCurrencies(scanner);
                } else {
                    continue;
                }
            }
            currenciesCorrect = true;
        }

    }

    public static boolean currenciesCorrect = false;

    public static String eur = "EUR";
    public static String sek = "SEK";
    public static String gbp = "GBP";
}
