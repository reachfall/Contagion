package com.contagion.map;


import java.util.HashMap;
import java.util.List;

public class Map {
    private static Map instance;
    private final TileMapGenerator tileMapGenerator;


    private Map() {
        System.out.println("Tworzenie mapy...");
        tileMapGenerator = new TileMapGenerator();
    }

    HashMap<Position, List<Drawable>> locationToDrawable = new HashMap<>(50);
    HashMap<Drawable, Position> lastDrawablePosition = new HashMap<>(50);

    Object graphics = new Object();

    public void drawOnMap(Drawable drawable) {
        System.out.println(drawable.getObjectType());
        //        Position lastPosition = lastDrawablePosition.get(drawable);
//        if (lastPosition != null) {
//
//        }
//        //for each junction
//        synchronized (junction1) {
//
//        }
//
//        synchronized (graphics) {
//
//        }
//        int layer;
//        switch (drawable.getObjectType()) {
//            case Client -> {
//            }
//            case Supplier -> {
//            }
//            case None -> {
//                layer = 5;
//            }
//        }
//        tileMapGenerator.drawLayer(drawable, layer);
    }


    synchronized public static Map getInstance() {
        if (instance == null) {
            instance = new Map();
        }
        return instance;
    }
}
