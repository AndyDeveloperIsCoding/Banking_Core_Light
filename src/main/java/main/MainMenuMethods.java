package main;

import databaseOperations.ReadFromDatabase;
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
        ReadFromDatabase.readAccountIdFromDatabase();
        System.out.println("AccountID: " + Account.accountId);
        Account.accountId = -1;
        //System.out.println("CustomerID: " + account.customerId);
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

    public static void createTransaction() {

        Scanner scanner = new Scanner(System.in);

        // User input 1
        System.out.println("Please provide the AccountID:");
        int providedAccountID = Integer.valueOf(scanner.nextLine());
        // Check 1/6: Check if the account with provided ID exists
        if (!ReadFromDatabase.accountIdCheck(providedAccountID)) {
            System.out.println("Account with such AccountID is not found.");
            MainMenuUi.mainMenu();
        }

        // User input 2
        System.out.println("Please provide the transaction amount:");
        int transactionAmount = Integer.valueOf(scanner.nextLine());
        // Check 2/6: Check if the provided amount is positive
        if (transactionAmount < 0) {
            System.out.println("The provided amount is negative. it is not possible to make a transaction with a negative amount.");
            MainMenuUi.mainMenu();
        }

        // User input 3
        System.out.println("Please provide the transaction currency:");
        String transactionCurrency = scanner.nextLine();
        // Check 3/6: Check the currency is allowed on the account
        if (!ReadFromDatabase.checkIfCurrencyAllowed(providedAccountID, transactionCurrency)) {
            System.out.println(transactionCurrency.toUpperCase() + " currency is not allowed on this account.");
            MainMenuUi.mainMenu();
        }

        // User input 4
        System.out.println("Please provide the transaction direction- receipt or transfer:");
        String transactionDirection = scanner.nextLine();
        // Check 4/6: Check if the direction is provided properly
        if (transactionDirection.toLowerCase().equals("in") || transactionDirection.toLowerCase().equals("receipt")) {
            transactionDirection = "in";
        } else if (transactionDirection.toLowerCase().equals("out") || transactionDirection.toLowerCase().equals("transfer")) {
            transactionDirection = "out";
        } else {
            System.out.println("Unknown direction provided. The direction can be whether receipt or transfer");
            MainMenuUi.mainMenu();
        }
        // Check 5/6: In case of funds transfer- check if the provided amount of funds is available
        ReadFromDatabase.readCurrencyBalance(providedAccountID, transactionCurrency);
        if (transactionDirection.equals("out")) {
            if (Main.specificCurrencyBalance < transactionAmount) {
                System.out.println("There is not enough funds on the account to make this transfer.");
                MainMenuUi.mainMenu();
            }
        }

        // User input 5
        System.out.println("Please provide a short transaction's description:");
        String transactionDescription = scanner.nextLine();
        // Check 6/6: In case of funds transfer- check if the provided amount of funds is available
        if (transactionDescription.length() <= 0) {
            System.out.println("Transaction's description is missing. Making of the transaction is aborted");
            MainMenuUi.mainMenu();
        }

        System.out.println("Saving the transaction to database..."); // Temp
        WriteToDatabase.saveTransactionToDatabase(providedAccountID, transactionAmount, transactionCurrency, transactionDirection, transactionDescription);
        WriteToDatabase.updateAccountsTable(providedAccountID, transactionAmount, transactionCurrency, transactionDirection);
        System.out.println("Transaction has been successfully executed!");
        MainMenuUi.mainMenu();
    }

    public static void getTransaction() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please provide the AccountID:");
        int providedAccountID = Integer.valueOf(scanner.nextLine());

        if (!ReadFromDatabase.accountIdCheck(providedAccountID)) {
            System.out.println("Account with such AccountID is not found.");
            MainMenuUi.mainMenu();
        } else {
            ReadFromDatabase.readTransactionFromDatabase(providedAccountID);
        }
    }
}