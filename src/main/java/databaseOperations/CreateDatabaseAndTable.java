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


        try {
            con = dataSource.getConnection();

            // Table for Customers:
            String createCustomerTable = "CREATE TABLE IF NOT EXISTS customers(" +
                    "customerID INTEGER PRIMARY KEY," +
                    "accountID INTEGER DEFAULT NULL," +
                    "country VARCHAR NOT NULL);";
            // Statement creation
            preparedStatementAccount = con.prepareStatement(createCustomerTable);
            // Statement execution
            preparedStatementAccount.executeUpdate();

            // Table for Accounts:
            String createAccountTable = "CREATE TABLE IF NOT EXISTS accounts(" +
                    "accountID INTEGER PRIMARY KEY," +
                    "customerID INTEGER);";

            // Statement creation
            preparedStatementAccount = con.prepareStatement(createAccountTable);
            // Statement execution
            preparedStatementAccount.executeUpdate();

            // Table for transactions/ account statements:
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


            // Table for EUR currency:
            String createEurTable = "CREATE TABLE IF NOT EXISTS eur(" +
                    "accountID INTEGER," +
                    "currencyName VARCHAR(3) DEFAULT eur," +
                    "currencyBalance INTEGER DEFAULT 0);";
            // Statement creation
            preparedStatementAccount = con.prepareStatement(createEurTable);
            // Statement execution
            preparedStatementAccount.executeUpdate();

            // Table for SEK currency:
            String createSekTable = "CREATE TABLE IF NOT EXISTS sek(" +
                    "accountID INTEGER," +
                    "currencyName VARCHAR(3) DEFAULT sek," +
                    "currencyBalance INTEGER DEFAULT 0);";
            // Statement creation
            preparedStatementAccount = con.prepareStatement(createSekTable);
            // Statement execution
            preparedStatementAccount.executeUpdate();

            // Table for GBP currency:
            String createGbpTable = "CREATE TABLE IF NOT EXISTS gbp(" +
                    "accountID INTEGER," +
                    "currencyName VARCHAR(3) DEFAULT gbp," +
                    "currencyBalance INTEGER DEFAULT 0);";
            // Statement creation
            preparedStatementAccount = con.prepareStatement(createGbpTable);
            // Statement execution
            preparedStatementAccount.executeUpdate();

            // Table for USD currency:
            String createUsdTable = "CREATE TABLE IF NOT EXISTS usd(" +
                    "accountID INTEGER," +
                    "currencyName VARCHAR(3) DEFAULT usd," +
                    "currencyBalance INTEGER DEFAULT 0);";
            // Statement creation
            preparedStatementAccount = con.prepareStatement(createUsdTable);
            // Statement execution
            preparedStatementAccount.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
