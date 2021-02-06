package com.contagion.tiles;

import com.contagion.map.Position;

import java.util.List;

public interface Movable extends Drawable {
    Position getPosition();
    void setPosition(Position position);
    boolean isSick();
    void setSick();
    boolean isVaccinated();

    boolean isSpecialPositionOccupied(Drawable stationaryObjectInNewPosition, List<Movable> entitiesOnNextPosition, Position position);
}
