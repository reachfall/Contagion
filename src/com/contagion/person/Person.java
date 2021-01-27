package com.contagion.person;

import com.contagion.map.Position;
import com.contagion.control.ScheduledExecution;
import com.contagion.tiles.Movable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Person implements Runnable, Movable {

    protected String name;
    protected String surname;
    protected String id;
    protected boolean isSick;
    protected boolean isVaccinated;
    protected boolean isMasked;
    protected Position position;
    protected Position lastPosition;
    protected int visitedShopsCounter;
    protected ArrayList<String> instructions = new ArrayList<>();

    public Person(String name, String surname, String id, Position position) {
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.position = position;
        this.lastPosition = position;
        this.instructions.addAll(new ArrayList<String>(List.of("wait")));
        ScheduledExecution.getInstance().scheduleAtFixedRate(this::run, 0, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public void setLastPosition(Position lastPosition) {
        this.lastPosition = lastPosition;
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

    @Override
    public Position getLastPosition() {
        return lastPosition;
    }

    public boolean comparePositions(){
        return lastPosition != position;
    }
}
