package com.company;

/**
 * Created by Mikhail on 24.12.2015.
 */
public class Asset extends FinancialInstrument {

    protected double quantity;
    protected double currentPrice;
    protected double averageOpenPrice;

    public Asset(AssetType _type, double _q, double _avOP)
    {
        assetType = _type;
        quantity = _q;
        averageOpenPrice = _avOP;
    }

    double getCurrentPrice()
    {
        return Quotes.GetQuote(assetType, Simulator.currentDate);
    }

    double getPnL()
    {
        if(averageOpenPrice > 0.)
            return (getCurrentPrice() - averageOpenPrice) / averageOpenPrice;
        else
            return 0.0;
    }

    double getPnlAmount()
    {
        if(averageOpenPrice > 0.)
            return (getCurrentPrice() - averageOpenPrice) / averageOpenPrice * quantity;
        else
            return 0.0;
    }
}
