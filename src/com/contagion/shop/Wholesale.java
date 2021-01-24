package com.contagion.shop;

import com.contagion.map.Position;
import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableType;

public class Wholesale extends Shop implements Drawable {
    public Wholesale(Position position) {
        super(position);
    }

    @Override
    public DrawableType getObjectType() {
        return DrawableType.Wholesale;
    }
}
