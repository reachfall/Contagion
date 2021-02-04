package com.contagion.shop;

import com.contagion.control.Randomize;
import com.contagion.control.ScheduledExecution;
import com.contagion.map.Position;
import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Wholesale extends Shop implements Runnable, Drawable {

    public Wholesale(String name, Position position, int maxClientCapacity, int storageCapacity) {
        super(name, position, maxClientCapacity, storageCapacity);
        ScheduledExecution.getInstance().scheduleAtFixedRate(this::run, 0, 10, TimeUnit.MILLISECONDS);
    }

    @Override
    public String toString() {
        return "Wholesale{" +
                "id=" + id +
                ", currentSupply=" + currentSupply.size() +
                ", storageCapacity=" + storageCapacity +
                '}';
    }

    public void createProduct() {
        synchronized (currentSupplyMonitor) {
            if (currentSupply.size() < storageCapacity) {
                currentSupply.add(Randomize.INSTANCE.createProduct());
            }
        }
    }

    public Package createPackage(UUID targetShop) {
        List<Product> listOfProducts = new ArrayList<>();
        synchronized (currentSupplyMonitor) {
            if (currentSupply.isEmpty()) {
                listOfProducts.add(Randomize.INSTANCE.createProduct());
            } else {
                int nPackages = Randomize.INSTANCE.randomNumberGenerator(1, Math.min(5, currentSupply.size()));
                for (int i = 0; i < nPackages; i++) {
                    listOfProducts.add(getRandomProduct());
                }
            }
        }
        return new Package(listOfProducts, id, targetShop);
    }

    @Override
    public DrawableType getObjectType() {
        return DrawableType.Wholesale;
    }

    @Override
    public void run() {
        createProduct();
    }
}
