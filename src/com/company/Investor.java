package com.company;
import java.util.Date;

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

    void update(Date _d)
    {
        if(_d.equals(onboarding_date))
            Onboarding();

        if(getDifferenceDays(lastinvestment_date, _d)>30)
            AddAdditionalInvestment();

        portfolio.update();
        printCurrentState();
    }

    protected void Onboarding()
    {
        portfolio.createInitialPortfolioOrder(initialInvestment);
    }

    protected void AddAdditionalInvestment()
    {
        lastinvestment_date = Simulator.currentDate;
        portfolio.AddCash(additionalInvestment);
        portfolio.buyAssets(profile);
    }

    public int getDifferenceDays(Date d1, Date d2) {
        int daysdiff=0;
        long diff = d2.getTime() - d1.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000)+1;
        daysdiff = (int) diffDays;
        return daysdiff;
    }

    public void printCurrentState()
    {
        System.out.println(Simulator.currentDate + "  " + portfolio.getPnlAmount() + " " + portfolio.getPortfolioDrift(profile));
    }

    protected
        Date onboarding_date;
        Date lastinvestment_date;
        Portfolio portfolio;
        RiskProfile profile;

    private double initialInvestment = 10000.;
    private double additionalInvestment = 500.;
}
