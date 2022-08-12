package databaseOperations;

import main.Main;
import main.MainMenuMethods;
import objects.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WriteToDatabase {

    public static void saveNewAccountToDatabase() {

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

            if (MainMenuMethods.eur) {
                // Statement creation
                preparedStatementEur = con.prepareStatement(makeAnEntryWithproperCUrrency);
                preparedStatementEur.setInt(1, Account.customerId);
                preparedStatementEur.setInt(2, Account.accountId);
                preparedStatementEur.setString(3, MainMenuMethods.eurString);
                preparedStatementEur.setInt(4, 0);
                // Statement execution
                preparedStatementEur.executeUpdate();
            }
            if (MainMenuMethods.sek) {
                // Statement creation
                preparedStatementSek = con.prepareStatement(makeAnEntryWithproperCUrrency);
                preparedStatementSek.setInt(1, Account.customerId);
                preparedStatementSek.setInt(2, Account.accountId);
                preparedStatementSek.setString(3, MainMenuMethods.sekString);
                preparedStatementSek.setInt(4, 0);
                // Statement execution
                preparedStatementSek.executeUpdate();
            }
            if (MainMenuMethods.gbp) {
                // Statement creation
                preparedStatementGbp = con.prepareStatement(makeAnEntryWithproperCUrrency);
                preparedStatementGbp.setInt(1, Account.customerId);
                preparedStatementGbp.setInt(2, Account.accountId);
                preparedStatementGbp.setString(3, MainMenuMethods.gbpString);
                preparedStatementGbp.setInt(4, 0);
                // Statement execution
                preparedStatementGbp.executeUpdate();
            }
            if (MainMenuMethods.usd) {
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

            /*
            // Update table
            String updateCurrencyBalance = "UPDATE accounts SET currencyBalance = " + Main.specificCurrencyBalance + " WHERE accountID = ? AND currencyName = ?";
            preparedStatementTransaction = con.prepareStatement(updateCurrencyBalance);
            preparedStatementTransaction.setInt(1, providedAccountID);
            preparedStatementTransaction.setString(2, transactionCurrency);
            preparedStatementTransaction.executeUpdate();
             */

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

    public static void updateAccountsTable(int providedAccountID, int transactionAmount, String transactionCurrency) {

        SQLiteDataSource dataSource = new SQLiteDataSource();
        String url = "jdbc:sqlite:" + Main.databaseFileName;
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatementCurrencyBalanceUpdate = null;

        try {
            con = dataSource.getConnection();

            //TEMP System.out.println("Specific currency balance before making the change to table is (should be atleast 15 EUR) " + Main.specificCurrencyBalance);
            String updateCurrencyBalance = "UPDATE accounts SET currencyBalance = " + (Main.specificCurrencyBalance + transactionAmount) + " WHERE accountID = ? AND currencyName = ?";

            // Statement creation
            preparedStatementCurrencyBalanceUpdate = con.prepareStatement(updateCurrencyBalance);
            preparedStatementCurrencyBalanceUpdate.setInt(1, providedAccountID);
            preparedStatementCurrencyBalanceUpdate.setString(2, transactionCurrency);

            // Statement execution
            ReadFromDatabase.execution = preparedStatementCurrencyBalanceUpdate.executeUpdate();
            //TEMP System.out.println("Execution at the moment of updating the table is " + ReadFromDatabase.execution);

            /* THIS SHOULD WORK
            String addBalanceSql = "UPDATE accounts SET currencyBalance = currencyBalance + 0 WHERE accountID = ? AND currencyName = ?";

            preparedStatement = con.prepareStatement(addBalanceSql);
            preparedStatement.setInt(1, accountId);
            preparedStatement.setString(2, transferCurrency);

            // Statement execution
            execution = preparedStatement.executeUpdate();

             */

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
