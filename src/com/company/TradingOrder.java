package com.company;

import java.util.Date;

/**
 * Created by Mikhail on 24.12.2015.
 */
public class TradingOrder {
    public AssetType assetType;
    public double quantity;
    public String ticker;
    public Date orderDate;

    public TradingOrder(AssetType _type, double q, Date _date)
    {
        _type = assetType;
        quantity = q;
        orderDate = _date;
    }
}
