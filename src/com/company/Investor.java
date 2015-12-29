package com.company;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by myandreev on 23/12/2015.
 */
public class Investor {
    public Investor(Date  _date)
    {
        onboarding_date = _date;
        lastinvestment_date = onboarding_date;
        profile = new RiskProfile();
        portfolio = new Portfolio(profile);
    }

    void update(Date _d, FeeModel _fees)
    {
        if(_d.before(onboarding_date))
            return;

        createEmptyDailyBlockOrder();

        if(_d.equals(onboarding_date))
            Onboarding();

        if(getDifferenceDays(lastinvestment_date, _d)>=30)
        {
            AddAdditionalInvestment();
            _fees.processPortfolio(portfolio);

            // trigger model
            if(portfolio.getPortfolioDrift(profile) >= triggerThreshold)
                portfolio.rebalanceProtfolio(profile, daily_block_order);
            else
                portfolio.buyAssets(profile, daily_block_order);

           }
        else
        {
            // trigger model
            if(portfolio.getPortfolioDrift(profile) >= triggerThreshold)
                portfolio.rebalanceProtfolio(profile, daily_block_order);

            // tax loss harvesting
           // portfolio.tryTaxLossHarvesting(daily_block_order);
        }

        portfolio.update();
        //printCurrentState();
        DBG_PrintDailyBlockOrder();
        // DBG

    }

    protected void Onboarding()
    {
        portfolio.createInitialPortfolioOrder(initialInvestment, daily_block_order);
    }

    protected void AddAdditionalInvestment()
    {
        lastinvestment_date = Simulator.currentDate;
        portfolio.AddCash(additionalInvestment);
    }

    public int getDifferenceDays(Date d1, Date d2) {
        int daysdiff=0;
        long diff = d2.getTime() - d1.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000)+1;
        daysdiff = (int) diffDays;
        return daysdiff;
    }

    public void createEmptyDailyBlockOrder()
    {
        daily_block_order = new HashMap<AssetType, Double>();

        for (AssetType dir : AssetType.values()) {
            daily_block_order.put(dir, 0.);
        }
    }


    public void DBG_PrintDailyBlockOrder()
    {
        System.out.format("Order:");
        for (AssetType dir : AssetType.values()) {
            if(daily_block_order.get(dir).doubleValue() != 0.)
                System.out.format(" %s %.0f ", dir, daily_block_order.get(dir).doubleValue());
        }
        System.out.format("\n");
    }

    public void printCurrentState()
    {
        System.out.format("Portfolio at %s: %.0f %.3f %.0f \n",Simulator.currentDate.toString(),
                portfolio.getPnlAmount(),
                portfolio.getPortfolioDrift(profile),
                portfolio.realized_loss);
    }

    protected
        Date onboarding_date;
        Date lastinvestment_date;
        Portfolio portfolio;
        RiskProfile profile;
        Map<AssetType, Double> daily_block_order;

    private double initialInvestment = 10000.;
    private double additionalInvestment = 500.;
    private double triggerThreshold = 0.05;
}
