package com.contagion.shop;

import com.contagion.map.Position;
import com.contagion.tiles.Drawable;

import java.util.ArrayList;

public abstract class Shop implements Drawable {
    private Position position;
    private String name;
    private ArrayList<Product> supply;
    private Object supplyMonitor = new Object();
    private int actualCapacity;

    public Shop(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public int getActualCapacity() {
        return actualCapacity;
    }

    public Product getRandomProduct() {
        Product product;
        synchronized (supplyMonitor) {
            if(supply.size() == 0){
                return null;
            }
            int index = (int) (Math.random() * (supply.size()));
            product = supply.get(index);
            supply.remove(index);
        }
        return product;
    }
}
