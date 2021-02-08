package com.contagion.tiles;

import com.contagion.map.Position;

import java.util.List;
import java.util.UUID;

public interface Movable extends Drawable {
    Position getPosition();
    void setPosition(Position position);
    boolean isSick();
    void setSick();
    boolean isVaccinated();
    boolean isMasked();
    UUID getId();

    boolean isSpecialPositionOccupied(DrawableType stationaryObjectInNewPosition, List<Movable> entitiesOnNextPosition, Position position);
}
