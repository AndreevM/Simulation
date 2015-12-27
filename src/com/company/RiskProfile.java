package com.company;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mikhail on 24.12.2015.
 */
public class RiskProfile {
    Map<AssetType, Double> allocation;

    public RiskProfile()
    {
        allocation = new HashMap<AssetType, Double>();

        allocation.put(AssetType.SP, 0.0);
        allocation.put(AssetType.DOW, 0.0);
        allocation.put(AssetType.VOL, 0.0);
        allocation.put(AssetType.UsEq, 0.40);
        allocation.put(AssetType.DevEq, 0.25);
        allocation.put(AssetType.EMMkt, 0.15);
        allocation.put(AssetType.US_REIT, 0.10);
        allocation.put(AssetType.US_TIPS, 0.05);
        allocation.put(AssetType.US_Bond, 0.05);
    }
}
