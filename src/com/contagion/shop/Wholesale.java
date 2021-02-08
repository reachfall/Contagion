package com.contagion.shop;

import com.contagion.control.ScheduledExecution;
import com.contagion.control.Storage;
import com.contagion.map.Position;
import com.contagion.tiles.DrawableShop;
import com.contagion.tiles.DrawableType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Wholesale extends Shop implements Runnable, DrawableShop {

    public Wholesale(String name, Position position, int maxClientCapacity, int storageCapacity) {
        super("(W) " + name, position, maxClientCapacity, storageCapacity);
        ScheduledExecution.getInstance().scheduleAtFixedRate(this, 0, 100, TimeUnit.MILLISECONDS);
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
                supplyOccupancy.setValue((double) (currentSupply.size() + 1) / storageCapacity);
                currentSupply.add(Storage.INSTANCE.createProduct(name));
            }
        }
    }

    public Package createPackage(Shop targetShop) {
        List<Product> listOfProducts = new ArrayList<>();
        synchronized (currentSupplyMonitor) {
            if (currentSupply.isEmpty()) {
                Product product = Storage.INSTANCE.createProduct(name);
                product.setTo(targetShop.getName());
                listOfProducts.add(product);
            } else {
                int nPackages = Storage.INSTANCE.randomNumberGenerator(1, Math.min(5, currentSupply.size()));
                for (int i = 0; i < nPackages; i++) {
                    Product product = getRandomProduct();
                    product.setTo(targetShop.getName());
                    listOfProducts.add(product);
                }
            }
        }
        return new Package(listOfProducts, id, targetShop.getId());
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
