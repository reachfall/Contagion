package com.contagion.shop;

import com.contagion.map.Position;
import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableType;

public class RetailShop extends Shop implements Drawable {
    public RetailShop(String name, Position position, int maxClientCapacity, int storageCapacity) {
        super(name, position, maxClientCapacity, storageCapacity);
    }

    public void receivePackage(Package receivedPackage) {
        synchronized (currentSupplyMonitor) {
            for (int i = 0; i < receivedPackage.getProductList().size(); i++) {
                if (currentSupply.size() < storageCapacity) {
                    currentSupply.addAll(receivedPackage.getProductList());
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public DrawableType getObjectType() {
        return DrawableType.RetailShop;
    }
}
