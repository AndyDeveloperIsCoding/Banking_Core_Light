package databaseOperations;

import main.Main;
import main.MainMenuMethods;
import objects.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WriteToDatabase {

    public static void saveNewAccountToDatabase(String country) {

        SQLiteDataSource dataSource = new SQLiteDataSource();
        String url = "jdbc:sqlite:" + Main.databaseFileName;
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatementCustomers;
        PreparedStatement preparedStatementAccounts;

        PreparedStatement preparedStatementEur = null;
        PreparedStatement preparedStatementSek = null;
        PreparedStatement preparedStatementGbp = null;
        PreparedStatement preparedStatementUsd = null;

        try {
            con = dataSource.getConnection();

            // Entry into Customers table
            String makeAnEntryToCustomers = "INSERT INTO customers (country) VALUES (?)";
            preparedStatementCustomers = con.prepareStatement(makeAnEntryToCustomers);
            preparedStatementCustomers.setString(1, country);
            preparedStatementCustomers.executeUpdate();

            // Entry to Accounts table
            ReadFromDatabase.readCustomerIdFromDatabase();
            String makeAnEntryToAccounts = "INSERT INTO accounts (customerID) VALUES (?)"; //Previously: (customerID, accountID, currencyName, currencyBalance) VALUES (?, ?, ?, ?)
            preparedStatementAccounts = con.prepareStatement(makeAnEntryToAccounts);
            preparedStatementAccounts.setInt(1, Account.customerId);
            preparedStatementAccounts.executeUpdate();

            //Update Customers with accountID:
            ReadFromDatabase.readAccountIdFromDatabase();
            String makeAnEntryToCustomers2 = "UPDATE customers SET accountID = ? WHERE customerID = ?;";
            preparedStatementAccounts = con.prepareStatement(makeAnEntryToCustomers2);
            preparedStatementAccounts.setInt(1, Account.accountId);
            preparedStatementAccounts.setInt(2, Account.customerId);
            preparedStatementAccounts.executeUpdate();

            // Entry to Currency tables- for the currencies, that customer had chosen
            ReadFromDatabase.readAccountIdFromDatabase();

            if (MainMenuMethods.eur) {

                String makeAnEntryToEur = "INSERT INTO eur (accountID, currencyName, currencyBalance) VALUES (?, ?, ?)";
                preparedStatementEur = con.prepareStatement(makeAnEntryToEur);
                preparedStatementEur.setInt(1, Account.accountId);
                preparedStatementEur.setString(2, "eur");
                preparedStatementEur.setInt(3, 0);
                preparedStatementEur.executeUpdate();
            }

            if (MainMenuMethods.sek) {
                String makeAnEntryToSek = "INSERT INTO sek (accountID, currencyName, currencyBalance) VALUES (?, ?, ?)";
                preparedStatementSek = con.prepareStatement(makeAnEntryToSek);
                preparedStatementSek.setInt(1, Account.accountId);
                preparedStatementSek.setString(2, "sek");
                preparedStatementSek.setInt(3, 0);
                preparedStatementSek.executeUpdate();
            }
            if (MainMenuMethods.gbp) {
                String makeAnEntryToGbp = "INSERT INTO gbp (accountID, currencyName, currencyBalance) VALUES (?, ?, ?)";
                preparedStatementGbp = con.prepareStatement(makeAnEntryToGbp);
                preparedStatementGbp.setInt(1, Account.accountId);
                preparedStatementGbp.setString(2, "gbp");
                preparedStatementGbp.setInt(3, 0);
                preparedStatementGbp.executeUpdate();
            }
            if (MainMenuMethods.usd) {
                String makeAnEntryToUsd = "INSERT INTO usd (accountID, currencyName, currencyBalance) VALUES (?, ?, ?)";
                preparedStatementUsd = con.prepareStatement(makeAnEntryToUsd);
                preparedStatementUsd.setInt(1, Account.accountId);
                preparedStatementUsd.setString(2, "usd");
                preparedStatementUsd.setInt(3, 0);
                preparedStatementUsd.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void saveTransactionToDatabase(int providedAccountID, int transactionAmount, String transactionCurrency, String transactionDirection, String transactionDescription) {

        SQLiteDataSource dataSource = new SQLiteDataSource();
        String url = "jdbc:sqlite:" + Main.databaseFileName;
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatementTransaction = null;

        try {
            con = dataSource.getConnection();

            String makeTransactionEntry = "INSERT INTO statements (accountID, amount, currencyName, direction, description, currencyBalance) VALUES (?, ?, ?, ?, ?, ?)";

            // Statement creation
            preparedStatementTransaction = con.prepareStatement(makeTransactionEntry);
            preparedStatementTransaction.setInt(1, providedAccountID);
            // preparedStatementTransaction.setInt(2, XYZ);
            preparedStatementTransaction.setInt(2, transactionAmount);
            preparedStatementTransaction.setString(3, transactionCurrency);
            preparedStatementTransaction.setString(4, transactionDirection);
            preparedStatementTransaction.setString(5, transactionDescription);
            if (transactionDirection.equals("in")) {
                preparedStatementTransaction.setInt(6, (Main.specificCurrencyBalance + transactionAmount));
            } else if (transactionDirection.equals("out")) {
                preparedStatementTransaction.setInt(6, (Main.specificCurrencyBalance - transactionAmount));
            }
            // Statement execution
            preparedStatementTransaction.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatementTransaction != null) {
                try {
                    preparedStatementTransaction.close();
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

    public static String updateCurrencyBalanceSql;
    public static void updateCurrencyTableAfterMakingTransaction(int providedAccountID, int transactionAmount, String transactionCurrency, String transactionDirection) {

        SQLiteDataSource dataSource = new SQLiteDataSource();
        String url = "jdbc:sqlite:" + Main.databaseFileName;
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatementCurrencyBalanceUpdate = null;

        try {
            con = dataSource.getConnection();

            // Different SQLs for different transactions:
            if (transactionCurrency.equals("eur")) {
                updateCurrencyBalanceSql = "UPDATE eur SET currencyBalance = ? WHERE accountID = ?;";
            } else if(transactionCurrency.equals("sek")) {
                updateCurrencyBalanceSql = "UPDATE sek SET currencyBalance = ? WHERE accountID = ?;";
            } else if(transactionCurrency.equals("gbp")) {
                updateCurrencyBalanceSql = "UPDATE gbp SET currencyBalance = ? WHERE accountID = ?;";
            } else if(transactionCurrency.equals("usd")) {
                updateCurrencyBalanceSql = "UPDATE usd SET currencyBalance = ? WHERE accountID = ?;";
            }

            // Previous: String updateCurrencyBalance = "UPDATE accounts SET currencyBalance = ? WHERE accountID = ? AND currencyName = ?";
            // Statement creation

            preparedStatementCurrencyBalanceUpdate = con.prepareStatement(updateCurrencyBalanceSql);
            if (transactionDirection.equals("in")) {
                preparedStatementCurrencyBalanceUpdate.setInt(1, (Main.specificCurrencyBalance + transactionAmount));
            } else if (transactionDirection.equals("out")) {
                preparedStatementCurrencyBalanceUpdate.setInt(1, (Main.specificCurrencyBalance - transactionAmount));
            }
            preparedStatementCurrencyBalanceUpdate.setInt(2, providedAccountID);
            preparedStatementCurrencyBalanceUpdate.setString(3, transactionCurrency);

            // Statement execution
            ReadFromDatabase.execution = preparedStatementCurrencyBalanceUpdate.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatementCurrencyBalanceUpdate != null) {
                try {
                    preparedStatementCurrencyBalanceUpdate.close();
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
