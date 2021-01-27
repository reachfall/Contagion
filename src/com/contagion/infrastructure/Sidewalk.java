package com.contagion.infrastructure;

import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableType;

public class Sidewalk implements Drawable {
    @Override
    public DrawableType getObjectType() {
        return DrawableType.Sidewalk;
    }
}
