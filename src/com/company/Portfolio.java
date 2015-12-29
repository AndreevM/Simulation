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
    double realized_profit;
    double realized_loss;

    Portfolio(RiskProfile profile)
    {
        createPortfolioAllocation(profile);
    }

    void createPortfolioAllocation(RiskProfile profile)
    {
        allocation = new HashMap<AssetType, Double>(profile.allocation);
    }

    // Given amount of initial investment, portfolio allocation and market price, return number of shares of each ETF to order. This routine will be ran for each new client
   void createInitialPortfolioOrder(double initialInvestment, Map<AssetType, Double> _daily_order)
    {
        AddCash(initialInvestment);

        assets = new HashMap<AssetType, Asset>();
        for (Map.Entry<AssetType, Double> entry : allocation.entrySet())
        {
                double _quote = Quotes.GetQuote(entry.getKey(), Simulator.currentDate);
                double _q = Math.floor(initialInvestment * entry.getValue().doubleValue() / _quote);
                _daily_order.put(entry.getKey(), _daily_order.get(entry.getKey()) + _q);
                assets.put(entry.getKey(), new Asset(entry.getKey(), _q, _quote));
                cash = cash - _quote * _q;
        }
    }

    void buyAssets(RiskProfile profile, Map<AssetType, Double> _daily_order)
    {
        double used_cash = 0.0;

        for (Map.Entry<AssetType, Double> entry : profile.allocation.entrySet())
        {
            double _quote = Quotes.GetQuote(entry.getKey(), Simulator.currentDate);
            double _q = Math.floor(cash * entry.getValue().doubleValue() / _quote);
            assets.get(entry.getKey()).averageOpenPrice = (assets.get(entry.getKey()).quantity * assets.get(entry.getKey()).averageOpenPrice + _q * _quote) / (assets.get(entry.getKey()).quantity + _q);
            assets.get(entry.getKey()).quantity += _q;
            _daily_order.put(entry.getKey(), _daily_order.get(entry.getKey()) + _q);
            used_cash += _q * _quote;
        }

        cash -= used_cash;
    }

    void rebalanceProtfolio(RiskProfile profile, Map<AssetType, Double> _daily_order)
    {
        double value = getCurrentValue();
        double asset_value = 0;

        for (Map.Entry<AssetType, Double> entry : profile.allocation.entrySet())
        {
            double _quote = Quotes.GetQuote(entry.getKey(), Simulator.currentDate);
            double target_q = Math.floor(value * entry.getValue().doubleValue() / _quote);

            if(target_q > assets.get(entry.getKey()).quantity)
            {
                // buy additional stocks
                double _q = target_q - assets.get(entry.getKey()).quantity;
                _daily_order.put(entry.getKey(), _daily_order.get(entry.getKey()) + _q);
                assets.get(entry.getKey()).averageOpenPrice = (assets.get(entry.getKey()).quantity * assets.get(entry.getKey()).averageOpenPrice + _q * _quote) / (assets.get(entry.getKey()).quantity + _q);
                assets.get(entry.getKey()).quantity += _q;
            }
            else
            if(target_q < assets.get(entry.getKey()).quantity)
            {
                // sell assets
                double _q = assets.get(entry.getKey()).quantity - target_q;
                _daily_order.put(entry.getKey(), _daily_order.get(entry.getKey()) - _q);
                assets.get(entry.getKey()).quantity -= _q;

                double pnl = (_quote - assets.get(entry.getKey()).averageOpenPrice) * _q;

                if(pnl < 0)
                    realized_loss += pnl;
                else
                    realized_profit += pnl;
            }

            asset_value += assets.get(entry.getKey()).quantity * _quote;
        }

        cash = value - asset_value;
    }


    Map<AssetType, Double> getPortfolioAllocation()
    {
        for (Map.Entry<AssetType, Double> entry : allocation.entrySet())
        {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        return allocation;
    }

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

    void WithdrawCash(double _cash)
    {
        cash -= _cash;
    }

    void tryTaxLossHarvesting(Map<AssetType, Double> _daily_order)
    {
        for (Map.Entry<AssetType, Asset> entry : assets.entrySet())
        {
            if(entry.getValue().getPnL() < -0.05)
            {
                // wash
                realized_loss += entry.getValue().getPnlAmount();
                entry.getValue().averageOpenPrice = entry.getValue().getCurrentPrice();
                _daily_order.put(entry.getKey(), _daily_order.get(entry.getKey()) - entry.getValue().quantity);
                _daily_order.put(AssetType.Alt, entry.getValue().quantity);
            }
        }
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

    double getReailizedPnL()
    {return realized_loss + realized_profit;}

}
