package com.contagion.control;

import com.contagion.shop.Shop;

public enum PandemicControl {
    INSTANCE;
    private int numberOfInfected = 0;
    private int numberOfPeople = 0;
    private double lockdownFactor = 0.25;
    private double initialSicknessProbability = 0.1;
    private double sicknessSpreadProbability = 0.1;

    public void infected() {
        numberOfInfected++;
        if (numberOfInfected >= lockdownFactor * numberOfPeople) {
            for (Shop shop : Storage.INSTANCE.getAllShops()) {
                shop.lockdown();
            }
        }
    }

    public void recovered() {
        numberOfInfected--;
        if (numberOfInfected < lockdownFactor * numberOfPeople) {
            for (Shop shop : Storage.INSTANCE.getAllShops()) {
                shop.endLockdown();
            }
        }
    }
}
