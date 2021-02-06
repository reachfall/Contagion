package com.contagion.control;

import com.contagion.tiles.Movable;

public enum PandemicControl {
    INSTANCE;
    private int numberOfInfected = 0;
    private int numberOfPeople = 0;
    private double lockdownFactor = 0.25;
    private boolean lockdown = false;
    private final Object pandemicControlMonitor = new Object();

    //probability of getting sick even when there are no sick people around
    private double initialSicknessProbability = 0.005;

    //probability of getting sick from sick person
    private double sicknessSpreadProbability = 0.05;

    //probability of vaccine preventing getting sick
    private double vaccineProtectionProbability = 0.15;

    //suppliers have lowered infection probability by given ratio
    private double supplierViralSecurityRatio = 2;

    public double getInitialSicknessProbability() {
        return initialSicknessProbability;
    }

    public double getSicknessSpreadProbability() {
        return sicknessSpreadProbability;
    }

    public double getVaccineProtectionProbability() {
        return vaccineProtectionProbability;
    }

    public double getSupplierViralSecurityRatio() {
        return supplierViralSecurityRatio;
    }

    public void newRecovered() {
        numberOfInfected--;
    }

    public void setLockdown(int infected) {
        synchronized (pandemicControlMonitor) {
            numberOfInfected += infected;
            if (numberOfInfected >= numberOfPeople * lockdownFactor) {
                lockdown = true;
            } else {
                lockdown = false;
            }
            Storage.INSTANCE.setAllShopsCapacity(lockdown);
        }
    }

    public void addPerson() {
        synchronized (pandemicControlMonitor) {
            numberOfPeople++;
        }
    }

    public void removePerson(Movable entity) {
        synchronized (pandemicControlMonitor) {
            numberOfPeople--;
            if (entity.isSick()) {
                numberOfInfected--;
            }
        }

    }
}
