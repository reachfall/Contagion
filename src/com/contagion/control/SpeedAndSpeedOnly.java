package com.contagion.control;

import java.util.concurrent.atomic.AtomicBoolean;

public enum SpeedAndSpeedOnly {
    INSTANCE;
    private int speed;
    private AtomicBoolean timeControl = new AtomicBoolean(true);

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public AtomicBoolean getTimeControl() {
        return timeControl;
    }

    public void setTimeControl(AtomicBoolean timeControl) {
        this.timeControl = timeControl;
    }
}
