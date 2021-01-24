package com.contagion.shop;

import com.contagion.map.Position;
import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableType;

public class RetailShop extends Shop implements Drawable {
    public RetailShop(Position position) {
        super(position);
    }

    @Override
    public DrawableType getObjectType() {
        return DrawableType.RetailShop;
    }
}
