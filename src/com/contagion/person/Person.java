package com.contagion.person;

import com.contagion.map.Map;
import com.contagion.map.Position;
import com.contagion.control.ScheduledExecution;
import com.contagion.tiles.Movable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public abstract class Person implements Runnable, Movable {

    protected String name;
    protected String surname;
    protected String id;
    protected boolean isSick;
    protected boolean isVaccinated;
    protected Position position;
    protected Position lastPosition;
    protected int visitedShopsCounter;
    protected final Phaser phaser;
    protected ArrayList<String> instructions = new ArrayList<>();

    public Person(String name, String surname, String id, Position position, Phaser phaser) {
        this.name = name;
        this.surname = surname;
        this.id = id;
        this.position = position;
        this.lastPosition = position;
        this.instructions.add("wait");
        this.phaser = phaser;
        phaser.register();
        ScheduledExecution.getInstance().scheduleAtFixedRate(this::run, 0, 10, TimeUnit.MILLISECONDS);
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public void setLastPosition(Position lastPosition) {
        this.lastPosition = lastPosition;
    }

//    @Override
//    public String toString() {
//        return "Person{" +
//                "name='" + name + '\'' +
//                ", surname='" + surname + '\'' +
//                ", id='" + id + '\'' +
//                ", isSick=" + isSick +
//                ", isVaccinated=" + isVaccinated +
//                ", isMasked=" + isMasked +
//                ", position=" + position +
//                ", lastPosition=" + lastPosition +
//                ", visitedShopsCounter=" + visitedShopsCounter +
//                ", instructions=" + getFirstInstruction() +
//                '}';
//    }

    @Override
    public String toString() {
        return "Person{" +
                ", position=" + position +
                ", lastPosition=" + lastPosition +
                ", position type=" + Map.getInstance().getPostionType(position) +
                ", instructions=" + getFirstInstruction() +
                ", id=" + id +
                '}';
    }

    public String getFirstInstruction(){
        if(instructions.isEmpty()){
            return null;
        } else {
            return instructions.get(0);
        }
    }

    public void destroy() {
        try {
            _destroy();
        } catch (Exception e) {
            ;
        } finally {
            phaser.arriveAndDeregister();
        }

    }
    abstract protected void _destroy();

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

    @Override
    public boolean isSick(){
        return isSick;
    }
}
