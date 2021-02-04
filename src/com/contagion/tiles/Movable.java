package com.contagion.tiles;

import com.contagion.map.Position;

import java.util.ArrayList;

public interface Movable extends Drawable {
    public Position getPosition();
    public void setPosition(Position position);
    public void setLastPosition(Position lastPosition);
    public Position getLastPosition();
    public boolean isSick();

    public boolean isSpecialPositionOccupied(Drawable stationaryObjectInNewPosition, ArrayList<Drawable> entitiesOnNextPosition, Position position);
}
