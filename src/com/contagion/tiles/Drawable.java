package com.contagion.tiles;

import com.contagion.map.Position;

public interface Drawable {
    public Position getPosition();
    public DrawableType getObjectType();
}