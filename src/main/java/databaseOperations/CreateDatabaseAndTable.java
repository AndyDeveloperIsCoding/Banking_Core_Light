package databaseOperations;

import main.Main;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateDatabaseAndTable {

    public static void createDatabaseAndTable() {

        SQLiteDataSource dataSource = new SQLiteDataSource();
        String url = "jdbc:sqlite:" + Main.databaseFileName;
        dataSource.setUrl(url);

        Connection con = null;
        PreparedStatement preparedStatementAccount = null;
        PreparedStatement preparedStatementAccountStatement = null;

        // Table for storing account data
        try {
            con = dataSource.getConnection();

            String createAccountTable = "CREATE TABLE IF NOT EXISTS accounts(" +
                    //"customerID INTEGER," +
                    "accountID INTEGER PRIMARY KEY," +
                    "currencyName VARCHAR(3) NOT NULL ," +
                    "currencyBalance INTEGER NOT NULL DEFAULT 0 );";

            // Statement creation
            preparedStatementAccount = con.prepareStatement(createAccountTable);
            // Statement execution
            preparedStatementAccount.executeUpdate();

            // Table for storing account statement data
            String createStatementTable = "CREATE TABLE IF NOT EXISTS statements(" +
                    "accountID INTEGER," +
                    "transactionID INTEGER PRIMARY KEY," +
                    "amount INTEGER," +
                    "currencyName VARCHAR(3)," +
                    "direction VARCHAR(3)," +
                    "description VARCHAR(20)," +
                    "currencyBalance INTEGER );";
            // Statement creation
            preparedStatementAccountStatement = con.prepareStatement(createStatementTable);
            // Statement execution
            preparedStatementAccountStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatementAccount != null) {
                try {
                    preparedStatementAccount.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatementAccountStatement != null) {
                try {
                    preparedStatementAccountStatement.close();
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
