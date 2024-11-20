package view;

public enum CurrencyExchange
{
    USD("USD", 0.9178),
    AUD("AUD", 0.6080),
    EGP("EGP", 0.0187),
    KZT("KZT", 0.0019),
    GBP("GBP", 1.19),
    FJD("FJD", 0.4091);


    private String currency_codes;
    private double current_exchange;

    CurrencyExchange(String currency_codes,
                     double current_exchange)
    {
        this.currency_codes = currency_codes;
        this.current_exchange = current_exchange;
    }

    public String getCurrency_codes()
    {
        return currency_codes;
    }

    public double getCurrent_exchange()
    {
        return current_exchange;
    }

    @Override
    public String toString()
    {
        return currency_codes + " - rate: " + current_exchange + " EUR";
    }


} // end of class
