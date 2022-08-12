package databaseOperations;

import main.Main;
import main.MainMenuUi;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Scanner;

public class ReadFromDatabase {
    public static void getAccount() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please provide the AccountID:");
        int requestedAccountID = Integer.valueOf(scanner.nextLine());

        if(!ReadFromDatabase.accountIdCheck(requestedAccountID)){
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
            String accountDataRequestSql = "SELECT customerID, currencyName, currencyBalance FROM accounts WHERE accountID = " + requestedAccountID + " GROUP BY accountID;"; // Earlier:
            accountDataDisplay = accountDataRequest.executeQuery(accountDataRequestSql);
            while (accountDataDisplay.next()) {
                System.out.println("AccountID: " + requestedAccountID);
                int customerId = accountDataDisplay.getInt("customerID");
                System.out.println("CustomerID: " + customerId);
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

                /*
            while(accountDataDisplay.next()) {
                int currencyBalance = accountDataDisplay.getInt("currencyBalance");
                System.out.print(currencyBalance + " ");
                String currencyName = accountDataDisplay.getString("currencyName");
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

    public static boolean checkIfCurrencyAllowed(int accountId, String transferCurrency){

        boolean currencyAllowed = false;

        String url = "jdbc:sqlite:" + Main.databaseFileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatement = null;

        try {
            con = dataSource.getConnection();

            // Statement creation
            String addBalanceSql = "UPDATE accounts SET currencyBalance = currencyBalance + 0 WHERE accountID = ? AND currencyName = ?";

            preparedStatement = con.prepareStatement(addBalanceSql);
            preparedStatement.setInt(1, accountId);
            preparedStatement.setString(2, transferCurrency);

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
            currencyAllowed = true;
            execution = 0;
        }

        return currencyAllowed;
    }

    // From here
    /*
    public static boolean checkIfAmountAvailable(int providedAccountID, String transferCurrency, int transferAmount){

        boolean amountIsAvailable = false;

        String url = "jdbc:sqlite:" + Main.databaseFileName;

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        Connection con = null;
        Statement accountDataRequest = null;
        ResultSet accountDataDisplay = null;
        // Statement accountCurrenciesRequest = null;
        // ResultSet accountCurrenciesDisplay = null;

        try {
            con = dataSource.getConnection();

            // accountId request
            accountDataRequest = con.createStatement();
            String accountDataRequestSql = "SELECT currencyBalance FROM accounts WHERE accountID = " + providedAccountID + ";"; // Earlier: "&& currencyName = " + transferCurrency +
            accountDataDisplay = accountDataRequest.executeQuery(accountDataRequestSql);
            while (accountDataDisplay.next()) {
                int availableBalance = accountDataDisplay.getInt("currencyBalance");
                if(availableBalance >= transferAmount){
                    amountIsAvailable = true;
                };
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
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return amountIsAvailable;
    }
    */
    // To here


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
                //System.out.println("Specific currencty balance before the query: " + Main.specificCurrencyBalance);
                Main.specificCurrencyBalance = currencyBalanceDisplay.getInt("currencyBalance");
                // int availableBalance = currencyBalanceDisplay.getInt("currencyBalance");
                //System.out.println("Specific currencty balance after the query: " + Main.specificCurrencyBalance);
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
}

