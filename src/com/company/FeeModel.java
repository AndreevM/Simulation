package com.company;

/**
 * Created by myandreev on 29/12/2015.
 */
public class FeeModel {

    void processPortfolio(Portfolio _p)
    {
        double _fee = _p.getCurrentValue() * monthly_rate;
        fee_accumulated += _fee;
        //System.out.format("Fee collected %.1f\n", fee_accumulated);
        _p.WithdrawCash(_fee);
    }

    double monthly_rate = 0.15/12./100.;
    double fee_accumulated = 0.0;


}
