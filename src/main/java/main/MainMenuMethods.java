package main;

import databaseOperations.WriteToDatabase;
import objects.Account;

import java.util.Scanner;

public class MainMenuMethods {

    public static void systemExit() {
        Main.loggedOut = true;
        System.out.println("Thank You for using this software! See You next time");
        return;
    }

    public static void createAccount() {

        Scanner scanner = new Scanner(System.in);
        /* TEMPORARY SWITCH OFF as this info is not saved to database
        System.out.println("What is the customer's country of tax residence?");
        String country = scanner.nextLine();
         */

        eur = false;
        sek = false;
        gbp = false;
        usd = false;

        askForCurrencies(scanner);

        //These two lines should be deleted later
        //String[] properCurrencies = new String[]{"EUR", "SEK", "GBP", "USD"};
        // System.out.println(Arrays.toString(currenciesList));

        Account account = new Account(); // Previously: new Account(country);
        System.out.println("");
        System.out.println("New account has been created!");
        System.out.println("");
        System.out.println("AccountID: " + account.accountId);
        System.out.println("CustomerID: " + account.customerId);
        System.out.println("Your account balance is:");
        if (eur) {
            System.out.println("0 EUR");
        }
        if (sek) {
            System.out.println("0 SEK");
        }
        if (gbp) {
            System.out.println("0 GBP");
        }
        if (usd) {
            System.out.println("0 USD");
        }

        WriteToDatabase.saveNewAccountToDatabase();
        MainMenuUi.mainMenu();
    }

    private static void askForCurrencies(Scanner scanner) {

        // Process the currencies as Array
        currenciesCorrect = false;

        while (!currenciesCorrect) {
            System.out.println("Choose the accepted currencies for the account. Available currencies: EUR, SEK, GBP, USD:");
            String currencies = scanner.nextLine();
            String[] currenciesList;
            currenciesList = currencies.split(",");

            for (int i = 0; i < currenciesList.length; i++) {
                currenciesList[i] = currenciesList[i].trim();
                if (currenciesList[i].toLowerCase().equals("eur")) {
                    eur = true;
                } else if (currenciesList[i].toLowerCase().equals("sek")) {
                    sek = true;
                } else if (currenciesList[i].toLowerCase().equals("gbp")) {
                    gbp = true;
                } else if (currenciesList[i].toLowerCase().equals("usd")) {
                    usd = true;
                } else {
                    System.out.println("You have entered an incorrect currency.");
                    eur = false;
                    sek = false;
                    gbp = false;
                    usd = false;
                    askForCurrencies(scanner);
                }
                /* Old not nice code
                else if (!(currenciesList[i].toLowerCase().equals("eur") || currenciesList[i].toLowerCase().contains("sek") || currenciesList[i].toLowerCase().equals("gbp") || currenciesList[i].toLowerCase().equals("usd"))) {
                    System.out.println("You have entered an incorrect currency.");
                    askForCurrencies(scanner);
                } else {
                    continue;
                }
                 */
            }
            currenciesCorrect = true;
        }
    }

    public static boolean currenciesCorrect = false;

    public static boolean eur = false;
    public static boolean sek = false;
    public static boolean gbp = false;
    public static boolean usd = false;

    public static String eurString = "EUR";
    public static String sekString = "SEK";
    public static String gbpString = "GBP";
    public static String usdString = "USD";


}
