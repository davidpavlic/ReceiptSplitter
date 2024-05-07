package ch.zhaw.it.pm2.receiptsplitter.enums;

public enum Currencies {
    CHF("CHF");
    // More currencies can be added here

    private final String currency;

    Currencies(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}
