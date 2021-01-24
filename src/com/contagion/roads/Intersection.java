package com.contagion.roads;

import com.contagion.map.Position;
import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableType;

public class Intersection extends TransportInfrastructure implements Drawable {
    @Override
    public DrawableType getObjectType() {
        return DrawableType.Intersection;
    }
}