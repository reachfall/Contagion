package com.contagion.roads;

import com.contagion.tiles.DrawableType;

public class SidewalkIntersection extends TransportInfrastructure{

    @Override
    public DrawableType getObjectType() {
        return DrawableType.SidewalkIntersection;
    }
}
