package com.company;

import com.company.Investor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Mikhail on 24.12.2015.
 */
public class InvestmentClub {
    List<Investor> investors;
    protected double aum;

    InvestmentClub()
    {
        investors = new ArrayList<Investor>();
    }

    void OnboardInvestor(Date _d)
    {
        investors.add(new Investor(_d));
    }

    void update(Date _d)
    {
        for(Investor i: investors)
            i.update(_d);
    }

    double GetInvestmentClubAUM()
    {
        double _aum = 0.0;
        for(Investor i: investors)
            _aum += i.portfolio.getCurrentValue();
        return _aum;
    }
}
