package com.contagion.tiles;

import javafx.scene.paint.Color;

import java.util.EnumMap;

public enum MapColors {
    INSTANCE;
    private EnumMap<DrawableType, Color> colorMap;
    private Color defaultColor;

    private MapColors() {
        defaultColor = Color.web("000000");
        colorMap = new EnumMap<DrawableType, Color>(DrawableType.class);
        colorMap.put(DrawableType.Road, Color.web("fbf236"));
        colorMap.put(DrawableType.Intersection, Color.web("99e550"));
        colorMap.put(DrawableType.Sidewalk, Color.web("639bff"));
        colorMap.put(DrawableType.SidewalkIntersection, Color.web("5fcde4"));
        colorMap.put(DrawableType.Underpass, Color.web("76428a"));
        colorMap.put(DrawableType.RetailShop, Color.web("d95763"));
        colorMap.put(DrawableType.Wholesale, Color.web("d77bba"));
    }

    public Color getColor(DrawableType type){
        return colorMap.getOrDefault(type, defaultColor);
    }
}
