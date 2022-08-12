package databaseOperations;

import main.Main;
import main.MainMenuMethods;
import objects.Account;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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


}
