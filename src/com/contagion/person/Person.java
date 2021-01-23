package com.contagion.person;

import com.contagion.map.Drawable;
import com.contagion.map.Position;
import com.contagion.sample.ScheduledExecution;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Person implements Runnable, Drawable {

    private String name;
    private String surname;
    private UUID id;
    private boolean isSick;
    private boolean isVaccinated;
    private boolean isMasked;
    protected Position position;
    private int visitedShopsCounter;

    public Person(String name, String surname, Position position) {
        this.name = name;
        this.surname = surname;
        this.position = position;
        ScheduledExecution.getInstance().scheduleAtFixedRate(this::run, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", id=" + id +
                ", isSick=" + isSick +
                ", isVaccinated=" + isVaccinated +
                ", isMasked=" + isMasked +
                ", position=" + position +
                ", visitedShopsCounter=" + visitedShopsCounter +
                '}';
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
