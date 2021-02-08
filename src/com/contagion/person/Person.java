package com.contagion.person;

import com.contagion.control.*;
import com.contagion.map.Map;
import com.contagion.map.Position;
import com.contagion.tiles.Movable;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Person implements Runnable, Movable {

    protected final String name;
    protected final String surname;
    protected final UUID id;
    protected BooleanProperty isSick = new SimpleBooleanProperty();
    protected BooleanProperty isVaccinated = new SimpleBooleanProperty(false);
    protected BooleanProperty isMasked = new SimpleBooleanProperty(false);
    protected Position position;
    protected Position lastPosition;
    protected final int noShopToVisitToGetCured;
    protected int visitedShopsCounter;
    protected List<String> instructions = new ArrayList<>();

    public Person(String name, String surname, Position position, int noShopToVisitToGetCured) {
        this.name = name;
        this.surname = surname;
        this.id = UUID.randomUUID();
        this.position = position;
        this.instructions.add("wait");
        this.noShopToVisitToGetCured = noShopToVisitToGetCured;
        this.visitedShopsCounter = 0;
        PandemicControl.INSTANCE.addPerson();
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    public void shopCuration() {
        if (isSick.get()) {
            visitedShopsCounter++;
            if (visitedShopsCounter >= noShopToVisitToGetCured) {
                isSick.set(false);
                PandemicControl.INSTANCE.newRecovered();
                visitedShopsCounter = 0;
            }
        }
    }

    public void destroy() {
        try {
            PandemicControl.INSTANCE.removePerson(this);
            Map.getInstance().removeFromLocationToDrawable(this);
            Storage.INSTANCE.removePersonToFutureMap(this);
            _destroy();
        } catch (Exception e) {
            ;
        } finally {
            PhaserExecution.getInstance().arriveAndDeregister();
        }

    }

    abstract protected void _destroy();

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public boolean isSick() {
        return isSick.get();
    }

    @Override
    public void setSick() {
        isSick.set(true);
    }

    @Override
    public boolean isVaccinated() {
        return isVaccinated.get();
    }

    @Override
    public boolean isMasked() {
        return isMasked.get();
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public BooleanProperty isSickProperty() {
        return isSick;
    }

    public BooleanProperty isVaccinatedProperty() {
        return isVaccinated;
    }

    public BooleanProperty isMaskedProperty() {
        return isMasked;
    }

    public UUID getId() {
        return id;
    }
}
