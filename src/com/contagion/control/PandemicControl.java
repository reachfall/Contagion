package com.contagion.control;

import com.contagion.tiles.Movable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;

import java.util.List;

public enum PandemicControl {
    INSTANCE;
    private int numberOfInfected = 0;
    private int numberOfPeople = 0;
    private final BooleanProperty lockdown = new SimpleBooleanProperty(false);

    private final Object pandemicControlMonitor = new Object();

    public static final double LOCKDOWN_FACTOR = 0.4;
    private double lockdownFactor = LOCKDOWN_FACTOR;

    public static final double PASSIVE_INFECTION_RATE_DEFAULT = 0.005;
    private double passiveInfectionRate = PASSIVE_INFECTION_RATE_DEFAULT;

    public static final double TRANSMISSION_RATE_DEFAULT = 0.05;
    private double transmissionRate = TRANSMISSION_RATE_DEFAULT;

    public static final double MASK_TRANSMISSION_FACTOR = 0.2;
    private double maskTransmissionFactor = MASK_TRANSMISSION_FACTOR;

    public static final double VACCINE_EFFICACY_DEFAULT = 0.15;
    private double vaccineEfficacy = VACCINE_EFFICACY_DEFAULT;


    public void setLockdownFactor(double lockdownFactor) {
        synchronized (pandemicControlMonitor) {
            this.lockdownFactor = lockdownFactor;
        }
    }

    public void setPassiveInfectionRate(double passiveInfectionRate) {
        synchronized (pandemicControlMonitor) {
            this.passiveInfectionRate = passiveInfectionRate;
        }
    }

    public void setTransmissionRate(double transmissionRate) {
        synchronized (pandemicControlMonitor) {
            this.transmissionRate = transmissionRate;
        }
    }

    public void setMaskTransmissionFactor(double maskTransmissionFactor) {
        synchronized (pandemicControlMonitor) {
            this.maskTransmissionFactor = maskTransmissionFactor;
        }
    }

    public void setVaccineEfficacy(double vaccineEfficacy) {
        synchronized (pandemicControlMonitor) {
            this.vaccineEfficacy = vaccineEfficacy;
        }
    }


    public void resetCoefficients() {
        synchronized (pandemicControlMonitor) {
            lockdownFactor = LOCKDOWN_FACTOR;
            passiveInfectionRate = PASSIVE_INFECTION_RATE_DEFAULT;
            transmissionRate = TRANSMISSION_RATE_DEFAULT;
            maskTransmissionFactor = MASK_TRANSMISSION_FACTOR;
            vaccineEfficacy = VACCINE_EFFICACY_DEFAULT;
        }
    }

    public void newRecovered() {
        synchronized (pandemicControlMonitor) {
            numberOfInfected--;
        }
    }

    public void setLockdown(int infected) {
        synchronized (pandemicControlMonitor) {
            numberOfInfected += infected;
            lockdown.set(numberOfInfected > numberOfPeople * (1 - lockdownFactor));
            Storage.INSTANCE.setAllShopsCapacity(lockdown.getValue());
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

    public int evaluateEntitiesForDisease(List<Movable> entitiesOnPosition, int infected) {
        synchronized (pandemicControlMonitor) {
            double infectionProbability = passiveInfectionRate;

            for (Movable entity : entitiesOnPosition) {
                if (entity.isSick()) {
                    infectionProbability += entity.isMasked() ? transmissionRate * (1 - maskTransmissionFactor) : transmissionRate;
                }
            }

            for (Movable entity : entitiesOnPosition) {

                if (!entity.isSick()) {
                    double probability = entity.isVaccinated() ? infectionProbability - vaccineEfficacy : infectionProbability;
                    if (probability > Math.random()) {
                        entity.setSick();
                        infected++;
                    }
                }
            }
            return infected;
        }
    }

    //TODO
    public BooleanProperty lockdownProperty() {
        return lockdown;
    }
}
