package com.contagion.shop;

import com.contagion.control.Storage;
import com.contagion.map.Position;
import com.contagion.tiles.DrawableShop;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Shop implements DrawableShop {
    protected final String name;
    protected final UUID id;
    protected final Position position;
    protected final int maxClientCapacity;
    protected int actualCapacity;
    protected final List<Product> currentSupply;
    protected DoubleProperty supplyOccupancy;
    protected final Object currentSupplyMonitor;
    protected final int storageCapacity;

    public Shop(String name, Position position, int maxClientCapacity, int storageCapacity) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.position = position;
        this.maxClientCapacity = maxClientCapacity;
        this.actualCapacity = maxClientCapacity;
        this.storageCapacity = storageCapacity;
        this.currentSupply = new ArrayList<>();
        this.supplyOccupancy = new SimpleDoubleProperty(0);
        this.currentSupplyMonitor = new Object();
    }

    @Override
    public String toString() {
        return "Shop{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", maxClientCapacity=" + maxClientCapacity +
                ", currentClientCapacity=" + actualCapacity +
                ", currentSupply=" + currentSupply +
                ", currentSupplyMonitor=" + currentSupplyMonitor +
                '}';
    }

    public Product getRandomProduct() {
        synchronized (currentSupplyMonitor) {
            if (currentSupply.isEmpty()) {
                return null;
            } else {
                supplyOccupancy.setValue((double)(currentSupply.size() - 1) / storageCapacity);
                return currentSupply.remove(Storage.INSTANCE.randomNumberGenerator(0, currentSupply.size() - 1));
            }
        }
    }

    public Position getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public int getActualCapacity() {
        return actualCapacity;
    }

    public UUID getId() {
        return id;
    }

    //allow at minimum 1 client
    public void lockdown() {
        actualCapacity = Math.max(1, (int) (maxClientCapacity * 0.25));
    }

    public void endLockdown() {
        actualCapacity = maxClientCapacity;
    }

    public DoubleProperty supplyOccupancyProperty() {
        return supplyOccupancy;
    }
}
