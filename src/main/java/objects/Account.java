package objects;

public class Account {

    public static int customerID = 1550;
    public static int accountID = 2305;
    private String country;
    private String[] currencyName;
    private int[] currencyBalance;

    public Account(String country){
        this.customerID++;
        this.accountID++;
        this.country = country;
        this.currencyName = new String[]{"EUR", "SEK", "GBP", "USD"};
        this.currencyBalance = new int[]{0, 0, 0, 0};
    }

    public String[] getCurrencyName(){
        return this.currencyName;
    }

    public int[] getCurrencyBalance(){
        return this.currencyBalance;
    }
}
