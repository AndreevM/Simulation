package com.company;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by Mikhail on 24.12.2015.
 */
public class Portfolio {
    protected
    Map<AssetType, Asset> assets;
    Map<AssetType, Double> allocation;
    double cash;
    double invested_amount;

    Portfolio(RiskProfile profile)
    {
        createPortfolioAllocation(profile);
    }

    void createPortfolioAllocation(RiskProfile profile)
    {
        allocation = new HashMap<AssetType, Double>(profile.allocation);
    }

    // Given amount of initial investment, portfolio allocation and market price, return number of shares of each ETF to order. This routine will be ran for each new client
    List<TradingOrder> createInitialPortfolioOrder(double initialInvestment)
    {
        AddCash(initialInvestment);
        List<TradingOrder> tradingOrders = new ArrayList<TradingOrder>();

        assets = new HashMap<AssetType, Asset>();

        for (Map.Entry<AssetType, Double> entry : allocation.entrySet())
        {
                double _quote = Quotes.GetQuote(entry.getKey(), Simulator.currentDate);
                double _q = Math.floor(initialInvestment * entry.getValue().doubleValue() / _quote);
                tradingOrders.add(new TradingOrder(entry.getKey(),_q, Simulator.currentDate));
                assets.put(entry.getKey(), new Asset(entry.getKey(), _q, _quote));
                cash = cash - _quote * _q;
        }

        return tradingOrders;
    }

    List<TradingOrder> buyAssets(RiskProfile profile)
    {
        List<TradingOrder> tradingOrders = new ArrayList<TradingOrder>();
        double used_cash = 0.0;

        for (Map.Entry<AssetType, Double> entry : profile.allocation.entrySet())
        {
            double _quote = Quotes.GetQuote(entry.getKey(), Simulator.currentDate);
            double _q = Math.floor(cash * entry.getValue().doubleValue() / _quote);
            tradingOrders.add(new TradingOrder(entry.getKey(),_q, Simulator.currentDate));
            assets.get(entry.getKey()).quantity += _q;
            used_cash += _q * _quote;
        }

        cash -= used_cash;

        return tradingOrders;
    }

    Map<AssetType, Double> getPortfolioAllocation()
    {
        for (Map.Entry<AssetType, Double> entry : allocation.entrySet())
        {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        return allocation;
    }

    void rebalancePortfolio(RiskProfile profile) {}

    void getInvestmentProjection() {}

    void update()
    {
        // update allocation
        for (Map.Entry<AssetType, Double> entry : allocation.entrySet())
        {
            if(entry.getValue().doubleValue()>0)
            {
                double _quote = Quotes.GetQuote(entry.getKey(), Simulator.currentDate);
                double _q = assets.get(entry.getKey()).quantity;
                entry.setValue(_quote * _q / getCurrentValue());
            }
        }
        //getPortfolioAllocation();
    }

    void AddCash(double _cash)
    {
        cash += _cash;
        invested_amount += _cash;
    }

    double getCurrentValue()
    {
        double _v = cash;

        for (HashMap.Entry<AssetType, Asset> entry : assets.entrySet())
        {
            _v += entry.getValue().quantity * entry.getValue().getCurrentPrice();
        }
        return _v;
    }

    // returns maximum drift among assets
    double getPortfolioDrift(RiskProfile profile)
    {
        double drift = 0.;

        for (Map.Entry<AssetType, Double> entry : profile.allocation.entrySet())
        {
            if(entry.getValue().doubleValue()>0)
            {
                double _quote = Quotes.GetQuote(entry.getKey(), Simulator.currentDate);
                Asset _a = assets.get(entry.getKey());
                double _q = _a.quantity;

                double __drift = Math.abs(_quote * _q / getCurrentValue() - entry.getValue());
                if(__drift > drift)
                    drift = __drift;
            }
        }

        return drift;
    }

    double getROI()
    {
        if(invested_amount > 0)
            return getCurrentValue() / invested_amount - 1.0;
        else
            return 0.0;
    }


    double getPnlAmount()
    {return getCurrentValue() - invested_amount;}
}
