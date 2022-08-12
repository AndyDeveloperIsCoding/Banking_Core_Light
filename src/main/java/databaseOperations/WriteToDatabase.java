package databaseOperations;

import main.Main;
import main.MainMenuMethods;
import main.MainMenuUi;
import objects.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class WriteToDatabase {

    public static void saveNewAccountToDatabase(){

        SQLiteDataSource dataSource = new SQLiteDataSource();
        String url = "jdbc:sqlite:" + Main.databaseFileName;
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatementEur = null;
        PreparedStatement preparedStatementSek = null;
        PreparedStatement preparedStatementGbp = null;
        PreparedStatement preparedStatementUsd = null;

        try {
            con = dataSource.getConnection();

            String makeAnEntryWithproperCUrrency = "INSERT INTO accounts (customerID, accountID, currencyName, currencyBalance) VALUES (?, ?, ?, ?)";

            if(MainMenuMethods.eur){
                // Statement creation
                preparedStatementEur = con.prepareStatement(makeAnEntryWithproperCUrrency);
                preparedStatementEur.setInt(1, Account.customerId);
                preparedStatementEur.setInt(2, Account.accountId);
                preparedStatementEur.setString(3, MainMenuMethods.eurString);
                preparedStatementEur.setInt(4, 0);
                // Statement execution
                preparedStatementEur.executeUpdate();
            }
            if(MainMenuMethods.sek){
                // Statement creation
                preparedStatementSek = con.prepareStatement(makeAnEntryWithproperCUrrency);
                preparedStatementSek.setInt(1, Account.customerId);
                preparedStatementSek.setInt(2, Account.accountId);
                preparedStatementSek.setString(3, MainMenuMethods.sekString);
                preparedStatementSek.setInt(4, 0);
                // Statement execution
                preparedStatementSek.executeUpdate();
            }
            if(MainMenuMethods.gbp){
                // Statement creation
                preparedStatementGbp = con.prepareStatement(makeAnEntryWithproperCUrrency);
                preparedStatementGbp.setInt(1, Account.customerId);
                preparedStatementGbp.setInt(2, Account.accountId);
                preparedStatementGbp.setString(3, MainMenuMethods.gbpString);
                preparedStatementGbp.setInt(4, 0);
                // Statement execution
                preparedStatementGbp.executeUpdate();
            }
            if(MainMenuMethods.usd){
                // Statement creation
                preparedStatementUsd = con.prepareStatement(makeAnEntryWithproperCUrrency);
                preparedStatementUsd.setInt(1, Account.customerId);
                preparedStatementUsd.setInt(2, Account.accountId);
                preparedStatementUsd.setString(3, MainMenuMethods.usdString);
                preparedStatementUsd.setInt(4, 0);
                // Statement execution
                preparedStatementUsd.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatementEur != null) {
                try {
                    preparedStatementEur.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatementSek != null) {
                try {
                    preparedStatementSek.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatementGbp != null) {
                try {
                    preparedStatementGbp.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatementUsd != null) {
                try {
                    preparedStatementUsd.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public static void createTransaction() {

        Scanner scanner = new Scanner(System.in);

        // User input 1
        System.out.println("Please provide the AccountID:");
        int providedAccountID =Integer.valueOf(scanner.nextLine());
        // Check 1/6: Check if the account with provided ID exists
        if(!ReadFromDatabase.accountIdCheck(providedAccountID)){
            System.out.println("Account with such AccountID is not found.");
            MainMenuUi.mainMenu();
        }

        // User input 2
        System.out.println("Please provide the transaction amount:");
        int transferAmount =Integer.valueOf(scanner.nextLine());
        // Check 2/6: Check if the provided amount is positive
        if (transferAmount < 0){
            System.out.println("The provided amount is negative. it is not possible to make a transaction with a negative amount.");
            MainMenuUi.mainMenu();
        }

        // User input 3
        System.out.println("Please provide the transaction currency:");
        String transferCurrency = scanner.nextLine();
        // Check 3/6: Check the currency is allowed on the account
        if(!ReadFromDatabase.checkIfCurrencyAllowed(providedAccountID, transferCurrency)){
            System.out.println(transferCurrency.toUpperCase() + " currency is not allowed on this account.");
            MainMenuUi.mainMenu();
        }

        // User input 4
        System.out.println("Please provide the transaction direction: receipt (in) or transfer (out):");
        String transactionDirection = scanner.nextLine();
        // Check 4/6: Check if the direction is provided properly
        if(transactionDirection.toLowerCase().equals("in") || transactionDirection.toLowerCase().equals("receipt")){
            transactionDirection = "in";
        } else if (transactionDirection.toLowerCase().equals("out") || transactionDirection.toLowerCase().equals("transfer")){
            transactionDirection = "out";
        } else {
            System.out.println("Unknown direction provided. The direction can be whether receipt or transfer");
            MainMenuUi.mainMenu();
        }
        // Check 5/6: In case of funds transfer- check if the provided amount of funds is available
        if(transactionDirection.equals("out")){
            if(!ReadFromDatabase.checkIfAmountAvailable(providedAccountID, transferCurrency, transferAmount)){
                System.out.println("There is not enough funds on the account to make this transfer.");
                MainMenuUi.mainMenu();
            }
        }


        // User input 5
        System.out.println("Please provide a short transaction's description:");
        String transactionDescription = scanner.nextLine();

        MainMenuUi.mainMenu();
    }
}
