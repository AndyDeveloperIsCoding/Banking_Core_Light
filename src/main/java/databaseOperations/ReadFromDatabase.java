package databaseOperations;

import main.Main;
import main.MainMenuMethods;
import main.MainMenuUi;
import objects.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Scanner;

public class ReadFromDatabase {
    public static void getAccount() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please provide the AccountID:");
        int requestedAccountID = Integer.valueOf(scanner.nextLine());

        if (!ReadFromDatabase.accountIdCheck(requestedAccountID)) {
            System.out.println("Account with such AccountID is not found.");
            MainMenuUi.mainMenu();
        }

        String url = "jdbc:sqlite:" + Main.databaseFileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        Statement accountDataRequest = null;
        ResultSet accountDataDisplay = null;
        Statement accountCurrenciesRequest = null;
        ResultSet accountCurrenciesDisplay = null;

        try {
            con = dataSource.getConnection();

            // accountId request
            accountDataRequest = con.createStatement();
            String accountDataRequestSql = "SELECT currencyName, currencyBalance FROM accounts WHERE accountID = " + requestedAccountID + " GROUP BY accountID;"; // Earlier: "SELECT customerID, currencyName, currencyBalance FROM accounts WHERE accountID = " + requestedAccountID + " GROUP BY accountID;"
            accountDataDisplay = accountDataRequest.executeQuery(accountDataRequestSql);
            while (accountDataDisplay.next()) {
                System.out.println("AccountID: " + requestedAccountID);
                //int customerId = accountDataDisplay.getInt("customerID");
                //System.out.println("CustomerID: " + customerId);
                System.out.println("Your account balance is:");
            }

            // currencies request
            accountCurrenciesRequest = con.createStatement();
            String accountCurrenciesRequestSql = "SELECT currencyName, currencyBalance FROM accounts WHERE accountID = " + requestedAccountID + ";"; // Earlier:
            accountCurrenciesDisplay = accountCurrenciesRequest.executeQuery(accountCurrenciesRequestSql);
            while (accountCurrenciesDisplay.next()) {
                int currencyBalance = accountCurrenciesDisplay.getInt("currencyBalance");
                System.out.print(currencyBalance + " ");
                String currencyName = accountCurrenciesDisplay.getString("currencyName");
                System.out.println(currencyName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (accountDataRequest != null) {
                try {
                    accountDataRequest.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (accountDataDisplay != null) {
                try {
                    accountDataDisplay.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (accountCurrenciesRequest != null) {
                try {
                    accountCurrenciesRequest.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (accountCurrenciesDisplay != null) {
                try {
                    accountCurrenciesDisplay.close();
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
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
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
            System.out.println("At the beginning the execution is " +execution);

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

            /* Old code
            // Statement creation
            String checkCurrencySql = "UPDATE accounts SET currencyBalance = currencyBalance + 0 WHERE accountID = ? AND currencyName = ?";

            preparedStatement = con.prepareStatement(checkCurrencySql);
            preparedStatement.setInt(1, accountId);
            preparedStatement.setString(2, transactionCurrency);


            // Statement execution
            execution = preparedStatement.executeUpdate();
              */


        } catch (
                SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
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
        System.out.println("In the end the execution is " +execution);
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

        Connection con = null;
        PreparedStatement currencyBalanceRequest = null;
        ResultSet currencyBalanceDisplay = null;

        try {
            con = dataSource.getConnection();

            String accountBalanceRequestSql = "SELECT currencyBalance FROM accounts WHERE accountID = ? AND currencyName = ?;";
            currencyBalanceRequest = con.prepareStatement(accountBalanceRequestSql);
            currencyBalanceRequest.setInt(1, providedAccountID);
            currencyBalanceRequest.setString(2, transactionCurrency);

            currencyBalanceDisplay = currencyBalanceRequest.executeQuery(); //Earlier: executeQuery(accountBalanceRequestSql)

            while (currencyBalanceDisplay.next()) {
                Main.specificCurrencyBalance = currencyBalanceDisplay.getInt("currencyBalance");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (currencyBalanceRequest != null) {
                try {
                    currencyBalanceRequest.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (currencyBalanceDisplay != null) {
                try {
                    currencyBalanceDisplay.close();
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

                System.out.println(accountId + "\t\t\t" + + transactionId +  "\t\t\t\t" + amount + "\t\t" + currencyName +  "\t\t\t"  + direction +  "\t\t\t"  + description);
            }

            /*
            // currencies request
            accountCurrenciesRequest = con.createStatement();
            String accountCurrenciesRequestSql = "SELECT currencyName, currencyBalance FROM accounts WHERE accountID = " + requestedAccountID + ";"; // Earlier:
            accountCurrenciesDisplay = accountCurrenciesRequest.executeQuery(accountCurrenciesRequestSql);
            while (accountCurrenciesDisplay.next()) {
                int currencyBalance = accountCurrenciesDisplay.getInt("currencyBalance");
                System.out.print(currencyBalance + " ");
                String currencyName = accountCurrenciesDisplay.getString("currencyName");
                System.out.println(currencyName);
            }
             */

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (accountDataRequest != null) {
                try {
                    accountDataRequest.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (accountDataDisplay != null) {
                try {
                    accountDataDisplay.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (accountCurrenciesRequest != null) {
                try {
                    accountCurrenciesRequest.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (accountCurrenciesDisplay != null) {
                try {
                    accountCurrenciesDisplay.close();
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
}

