package com.contagion.person;

import com.contagion.map.DrawableType;
import com.contagion.map.Position;

public class Supplier extends Person{

    public Supplier(String name, String surname, Position position) {
        super(name, surname, position);
    }

    @Override
    public void run() {

    }

    @Override
    public DrawableType getObjectType() {
        return DrawableType.Supplier;
    }
}
