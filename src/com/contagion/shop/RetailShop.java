package com.contagion.shop;

import com.contagion.map.Position;
import com.contagion.tiles.DrawableShop;
import com.contagion.tiles.DrawableType;

public class RetailShop extends Shop implements DrawableShop {
    public RetailShop(String name, Position position, int maxClientCapacity, int storageCapacity) {
        super("(R) " + name, position, maxClientCapacity, storageCapacity);
    }

    public boolean receivePackage(Package receivedPackage) {
        synchronized (currentSupplyMonitor) {
            if (receivedPackage.getPackageSize() + currentSupply.size() <= storageCapacity) {
                supplyOccupancy.setValue((double)(currentSupply.size() + receivedPackage.getPackageSize()) / storageCapacity);
                currentSupply.addAll(receivedPackage.getProductList());
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public DrawableType getObjectType() {
        return DrawableType.RetailShop;
    }
}
