package com.company;

/**
 * Created by Mikhail on 24.12.2015.
 */
enum AssetType { SP, DOW, VOL, UsEq, DevEq,	EMMkt, US_REIT, US_TIPS, US_Bond}


public class FinancialInstrument {
    public AssetType assetType;
    public String assetTicker;
}
