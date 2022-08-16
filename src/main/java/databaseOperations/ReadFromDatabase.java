package databaseOperations;

import main.Main;
import main.MainMenuUi;
import objects.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Scanner;

public class ReadFromDatabase {
    public static void getAccount() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please provide the AccountID:");
        int requestedAccountId = Integer.valueOf(scanner.nextLine());

        if (!ReadFromDatabase.accountIdCheck(requestedAccountId)) {
            System.out.println("Account with such AccountID is not found.");
            MainMenuUi.mainMenu();
        } else {
            ReadFromDatabase.findCustomerIdByAccountID(requestedAccountId);
        }

        System.out.println("AccountID: " + requestedAccountId);
        System.out.println("CustomerID: " + Account.customerId);
        Account.customerId = -1;
        System.out.println("Your account balance is:");

        //Display currencies:
        if (ReadFromDatabase.checkIfCurrencyAllowed(requestedAccountId, "eur")) {
            ReadFromDatabase.readCurrencyBalance(requestedAccountId, "eur");
            System.out.println(Main.specificCurrencyBalance + " EUR");
        }
        if (ReadFromDatabase.checkIfCurrencyAllowed(requestedAccountId, "sek")) {
            ReadFromDatabase.readCurrencyBalance(requestedAccountId, "sek");
            System.out.println(Main.specificCurrencyBalance + " SEK");
        }
        if (ReadFromDatabase.checkIfCurrencyAllowed(requestedAccountId, "gbp")) {
            ReadFromDatabase.readCurrencyBalance(requestedAccountId, "gbp");
            System.out.println(Main.specificCurrencyBalance + " GBP");
        }
        if (ReadFromDatabase.checkIfCurrencyAllowed(requestedAccountId, "usd")) {
            ReadFromDatabase.readCurrencyBalance(requestedAccountId, "usd");
            System.out.println(Main.specificCurrencyBalance + " USD");
        }

    }

    public static boolean accountIdCheck(int requestedAccountID) {

        boolean accountIdExists = false;

        String url = "jdbc:sqlite:" + Main.databaseFileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {
            con = dataSource.getConnection();

            // Statement creation
            String addBalanceSql = "UPDATE accounts SET accountID = accountID + 0 WHERE accountID = ?";

            preparedStatement = con.prepareStatement(addBalanceSql);
            preparedStatement.setInt(1, requestedAccountID);

            // Statement execution
            execution = preparedStatement.executeUpdate();

        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        if (execution > 0) { // Test values
            accountIdExists = true;
            execution = 0;
        }

        return accountIdExists;
    }

    public static int execution = 0;

    public static boolean checkIfCurrencyAllowed(int accountId, String transactionCurrency) {

        boolean currencyAllowed = false;

        String url = "jdbc:sqlite:" + Main.databaseFileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatement = null;

        PreparedStatement preparedStatementEur;
        PreparedStatement preparedStatementSek;
        PreparedStatement preparedStatementGbp;
        PreparedStatement preparedStatementUsd;

        try {
            con = dataSource.getConnection();

            execution = 0;
            //System.out.println("At the beginning the execution is " + execution);

            if (transactionCurrency.equals("eur")) {
                String makeAnEntryToEur = "UPDATE eur SET currencyBalance = currencyBalance + 0 WHERE accountID = ?;";
                preparedStatementEur = con.prepareStatement(makeAnEntryToEur);
                preparedStatementEur.setInt(1, accountId);
                execution = preparedStatementEur.executeUpdate();
            }
            if (transactionCurrency.equals("sek")) {
                String makeAnEntryToSek = "UPDATE sek SET currencyBalance = currencyBalance + 0 WHERE accountID = ?;";
                preparedStatementSek = con.prepareStatement(makeAnEntryToSek);
                preparedStatementSek.setInt(1, accountId);
                execution = preparedStatementSek.executeUpdate();
            }
            if (transactionCurrency.equals("gbp")) {
                String makeAnEntryToGbp = "UPDATE gbp SET currencyBalance = currencyBalance + 0 WHERE accountID = ?;";
                preparedStatementGbp = con.prepareStatement(makeAnEntryToGbp);
                preparedStatementGbp.setInt(1, accountId);
                execution = preparedStatementGbp.executeUpdate();
            }
            if (transactionCurrency.equals("usd")) {
                String makeAnEntryToUsd = "UPDATE usd SET currencyBalance = currencyBalance + 0 WHERE accountID = ?;";
                preparedStatementUsd = con.prepareStatement(makeAnEntryToUsd);
                preparedStatementUsd.setInt(1, accountId);
                execution = preparedStatementUsd.executeUpdate();
            }

        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        if (execution > 0) { // Test values
            currencyAllowed = true;
            execution = 0;
        }

        return currencyAllowed;
    }

    public static void readCurrencyBalance(int providedAccountID, String transactionCurrency) {

        String url = "jdbc:sqlite:" + Main.databaseFileName;
        
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);



        Connection con;
        PreparedStatement currencyBalanceRequest;
        ResultSet currencyBalanceDisplay;

        try {
            con = dataSource.getConnection();

            //System.out.println("Transaction currency is: " + transactionCurrency);

            //Options for different currencies
            if (transactionCurrency.equals("eur")) {
                accountBalanceRequestSql = "SELECT currencyBalance FROM eur WHERE accountID = ?";
            } else if (transactionCurrency.equals("sek")) {
                accountBalanceRequestSql = "SELECT currencyBalance FROM sek WHERE accountID = ?";
            } else if (transactionCurrency.equals("gbp")) {
                accountBalanceRequestSql = "SELECT currencyBalance FROM gbp WHERE accountID = ?";
            } else if (transactionCurrency.equals("usd")) {
                accountBalanceRequestSql = "SELECT currencyBalance FROM usd WHERE accountID = ?";
            }

            currencyBalanceRequest = con.prepareStatement(accountBalanceRequestSql);
            currencyBalanceRequest.setInt(1, providedAccountID);

            currencyBalanceDisplay = currencyBalanceRequest.executeQuery();

            while (currencyBalanceDisplay.next()) {
                Main.specificCurrencyBalance = currencyBalanceDisplay.getInt("currencyBalance");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        accountBalanceRequestSql = "";
    }

    public static String accountBalanceRequestSql;

    public static void readTransactionFromDatabase(int providedAccountID) {

        if (!ReadFromDatabase.accountIdCheck(providedAccountID)) {
            System.out.println("Account with such AccountID is not found.");
            MainMenuUi.mainMenu();
        }

        String url = "jdbc:sqlite:" + Main.databaseFileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        // Table header:
        System.out.println("AccountID\tTransactionID\tAmount\tCurrency\tDirection\tDescription");

        Connection con = null;
        Statement accountDataRequest = null;
        ResultSet accountDataDisplay = null;
        Statement accountCurrenciesRequest = null;
        ResultSet accountCurrenciesDisplay = null;

        try {
            con = dataSource.getConnection();

            // accountId request
            accountDataRequest = con.createStatement();
            String accountDataRequestSql = "SELECT accountID, transactionID, amount, currencyName, direction, description FROM statements WHERE accountID = " + providedAccountID + ";"; // Earlier:
            accountDataDisplay = accountDataRequest.executeQuery(accountDataRequestSql);
            while (accountDataDisplay.next()) {

                int accountId = accountDataDisplay.getInt("accountID");
                int transactionId = accountDataDisplay.getInt("transactionID");
                int amount = accountDataDisplay.getInt("amount");
                String currencyNameLowerCase = accountDataDisplay.getString("currencyName");
                String currencyName = currencyNameLowerCase.toUpperCase();
                String direction = accountDataDisplay.getString("direction");
                String description = accountDataDisplay.getString("description");

                System.out.println(accountId + "\t\t\t" + +transactionId + "\t\t\t\t" + amount + "\t\t" + currencyName + "\t\t\t" + direction + "\t\t\t" + description);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void readAccountIdFromDatabase() {

        String url = "jdbc:sqlite:" + Main.databaseFileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement accountIdRequest = null;
        ResultSet accountIdDisplay = null;

        try {
            con = dataSource.getConnection();

            String accountIdRequestSql = "SELECT accountID FROM accounts ORDER BY accountID ASC";
            // Also works: "SELECT * FROM accounts WHERE accountID = (SELECT MAX(accountID) FROM accounts);"
            accountIdRequest = con.prepareStatement(accountIdRequestSql);

            accountIdDisplay = accountIdRequest.executeQuery();

            while (accountIdDisplay.next()) {
                Account.accountId = accountIdDisplay.getInt("accountID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void readCustomerIdFromDatabase() {

        String url = "jdbc:sqlite:" + Main.databaseFileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement customerIdRequest = null;
        ResultSet customerIdDisplay = null;

        try {
            con = dataSource.getConnection();

            String customerIdRequestSql = "SELECT customerID FROM customers ORDER BY customerID ASC";
            customerIdRequest = con.prepareStatement(customerIdRequestSql);

            customerIdDisplay = customerIdRequest.executeQuery();

            while (customerIdDisplay.next()) {
                Account.customerId = customerIdDisplay.getInt("customerID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void findCustomerIdByAccountID(int requestedAccountId) {

        String url = "jdbc:sqlite:" + Main.databaseFileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement customerIdRequest = null;
        ResultSet customerIdDisplay = null;

        try {
            con = dataSource.getConnection();

            String customerIdRequestSql = "SELECT customerID FROM customers WHERE accountID = ?;";
            customerIdRequest = con.prepareStatement(customerIdRequestSql);
            customerIdRequest.setInt(1, requestedAccountId);

            customerIdDisplay = customerIdRequest.executeQuery();

            while (customerIdDisplay.next()) {
                Account.customerId = customerIdDisplay.getInt("customerID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

