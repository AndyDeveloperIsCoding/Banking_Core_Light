package objects;

public class Account {

    //public static int customerId;

    public static int customerId;
    public static int accountId;

    // private String country; // Country is not saved into database
    private String[] currencyName;
    private int[] currencyBalance;

    public Account(){
        //this.customerId++;
        //his.accountId++;
        // this.country = country; // Country is not saved into database
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
