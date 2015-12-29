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
    public int direction;


    public TradingOrder(AssetType _type, double q, Date _date, int _direction)
    {
        _type = assetType;
        quantity = q;
        orderDate = _date;
        direction = _direction;
    }
}
