package com.contagion.infrastructure;

import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableType;

public class Intersection implements Drawable {
    @Override
    public DrawableType getObjectType() {
        return DrawableType.Intersection;
    }
}
