package com.contagion.person;

import com.contagion.control.PhaserExecution;
import com.contagion.control.ScheduledExecution;
import com.contagion.control.Storage;
import com.contagion.map.Map;
import com.contagion.pathfinding.Pathfinder;
import com.contagion.shop.*;
import com.contagion.shop.Package;
import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableType;
import com.contagion.map.Position;
import com.contagion.tiles.Movable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Supplier extends Person {
    private final List<Shop> shopsToVisit;
    private final HashMap<UUID, List<Package>> packageList;
    private final List<Package> cachedPackages;
    private final List<Shop> cachedRetailShops;
    private final int trunkCapacity;
    private final String companyName;
    private int trunkFilled;
    private final DoubleProperty trunkOccupancy;
    private final int tankCapacity;
    private int fuelLevel;
    private final DoubleProperty tankOccupancy;
    private RetailShop deliveryShop;
    private Wholesale pickUpShop;
    private int deliveryAttempts;

    public Supplier(String name, String surname, Position position, int noShopToVisitToGetCured, List<Shop> shopsToVisit, String companyName) {
        super(name, surname, position, noShopToVisitToGetCured);
        this.shopsToVisit = new ArrayList<>(shopsToVisit);
        this.companyName = companyName;
        this.packageList = new HashMap<>();
        this.cachedPackages = new ArrayList<>();
        this.cachedRetailShops = new ArrayList<>();
        this.trunkCapacity = Storage.INSTANCE.randomNumberGenerator(10, 20);
        this.trunkFilled = 0;
        this.trunkOccupancy = new SimpleDoubleProperty(0);
        this.tankCapacity = (int) (Storage.INSTANCE.getLongestPath() * 1.1);
        this.fuelLevel = tankCapacity;
        this.tankOccupancy = new SimpleDoubleProperty(this.fuelLevel);
        Storage.INSTANCE.addPersonToFutureMap(this, ScheduledExecution.getInstance().scheduleAtFixedRate(this, 0, 10, TimeUnit.MILLISECONDS));
        PhaserExecution.getInstance().register();
    }

    @Override
    public void run() {
        try {
            nextShop();
            interpretInstructions();
            tankOccupancy.set((double) fuelLevel / tankCapacity);
        } catch (Exception e) {
            System.err.println("Exception in supplier " + this.toString());
            System.err.println(Arrays.toString(e.getStackTrace()));
        } finally {
            PhaserExecution.getInstance().arriveAndAwaitAdvance();
        }
    }

    public void nextShop() {
        if (instructions.isEmpty()) {
            if (shopsToVisit.isEmpty()) {
                instructions.add("stop");
            } else {
                findWayToShop();
            }
        }
    }

    public void findWayToShop() {
        Shop shop = shopsToVisit.get(0);
        Collections.rotate(shopsToVisit, -1);
        ArrayList<String> newInstructionsQueue = Pathfinder.findPath(position, shop.getPosition(), this.getObjectType());
        if (newInstructionsQueue == null) {
            throw new NullPointerException("Can't find path");
        }
        instructions.addAll(newInstructionsQueue);
        if (shop.getObjectType() == DrawableType.RetailShop) {
            instructions.add("deliverPackagesLogistics");
        } else {
            instructions.add("pickUpPackagesLogistics");
        }
    }


    public void pickUpPackagesLogistics() {
        pickUpShop = (Wholesale) Storage.INSTANCE.getShopOnPosition(position);
        for (Shop shop : shopsToVisit) {
            if (shop.getObjectType() == DrawableType.RetailShop) {
                cachedRetailShops.add(shop);
            }
        }

        if (!cachedRetailShops.isEmpty()) {
            for (int i = 0; i < cachedRetailShops.size(); i++) {
                instructions.add("pickUpPackage");
            }
        }
        instructions.remove(0);
    }

    public void pickUpPackage() {
        if (cachedRetailShops.isEmpty()) {
            instructions.clear();
            cachedRetailShops.clear();
            instructions.add("wait");
        } else {
            Shop destinationShop = cachedRetailShops.remove(0);
            Package pack = pickUpShop.createPackage(destinationShop);
            for (Product product : pack.getProductList()) {
                product.setDeliveredBy(id.toString());
            }

            if (trunkFilled + pack.getPackageSize() <= trunkCapacity) {
                trunkFilled += pack.getPackageSize();
                List<Package> packageList = this.packageList.get(destinationShop.getId());
                if (packageList == null) {
                    this.packageList.put(destinationShop.getId(), new ArrayList<>(Arrays.asList(pack)));
                } else {
                    packageList.add(pack);
                }
            } else {
                for (Product product : pack.getProductList()) {
                    product.setDisposed(true);
                    product.setExists(false);
                }
                instructions.clear();
                cachedRetailShops.clear();
            }
        }
    }


    public void deliverPackagesLogistics() {
        deliveryShop = (RetailShop) Storage.INSTANCE.getShopOnPosition(position);
        List<Package> combinedShopPackage = this.packageList.remove(deliveryShop.getId());
        if (combinedShopPackage != null) {
            for (int i = 0; i < combinedShopPackage.size(); i++) {
                instructions.add("deliverPackage");
            }
            cachedPackages.addAll(combinedShopPackage);
        }
        //max 10 failed rounds before moving on
        deliveryAttempts = 10;
        instructions.remove(0);
    }

    public void deliverPackage() {
        if (deliveryAttempts > 0) {
            if (cachedPackages.isEmpty()) {
                if (!instructions.isEmpty()) {
                    instructions.clear();
                }
            } else {
                Package pack = cachedPackages.get(0);
                if (deliveryShop.receivePackage(pack)) {
                    trunkFilled -= pack.getPackageSize();
                    cachedPackages.remove(0);
                } else {
                    deliveryAttempts--;
                }
            }
        } else {
            for (Package pack : cachedPackages) {
                trunkFilled -= pack.getPackageSize();
            }
            for (Package pack : cachedPackages) {
                for (Product product : pack.getProductList()) {
                    product.setDisposed(true);
                    product.setExists(false);
                }
            }
            cachedPackages.clear();
            instructions.clear();
        }
    }

    public void fillTank() {
        fuelLevel = tankCapacity;
    }

    public void interpretInstructions() {
        switch (instructions.get(0)) {
            case "pickUpPackagesLogistics":
                fillTank();
                shopCuration();
                pickUpPackagesLogistics();
                trunkOccupancy.set((double) trunkFilled / trunkCapacity);
                break;
            case "pickUpPackage":
                pickUpPackage();
                trunkOccupancy.set((double) trunkFilled / trunkCapacity);
                break;
            case "deliverPackagesLogistics":
                fillTank();
                shopCuration();
                deliverPackagesLogistics();
                trunkOccupancy.set((double) trunkFilled / trunkCapacity);
                break;
            case "deliverPackage":
                deliverPackage();
                trunkOccupancy.set((double) trunkFilled / trunkCapacity);
                break;
            case "stop":
                if (!shopsToVisit.isEmpty()) {
                    instructions.remove(0);
                }
                break;
            case "up":
                if (Map.getInstance().moveToPosition(this, new Position(position.getX(), position.getY() - 1))) {
                    instructions.remove(0);
                    fuelLevel--;
                }
                break;
            case "down":
                if (Map.getInstance().moveToPosition(this, new Position(position.getX(), position.getY() + 1))) {
                    instructions.remove(0);
                    fuelLevel--;
                }
                break;
            case "left":
                if (Map.getInstance().moveToPosition(this, new Position(position.getX() - 1, position.getY()))) {
                    instructions.remove(0);
                    fuelLevel--;

                }
                break;
            case "right":
                if (Map.getInstance().moveToPosition(this, new Position(position.getX() + 1, position.getY()))) {
                    instructions.remove(0);
                    fuelLevel--;

                }
                break;
            default:
                lastPosition = position;
                instructions.remove(0);
        }
    }

    @Override
    public DrawableType getObjectType() {
        return DrawableType.Supplier;
    }

    @Override
    public boolean isSpecialPositionOccupied(DrawableType stationaryObjectInNewPosition, List<Movable> entitiesOnNextPosition, Position position) {
        if (stationaryObjectInNewPosition == DrawableType.Intersection ||
                stationaryObjectInNewPosition == DrawableType.RetailShop ||
                stationaryObjectInNewPosition == DrawableType.Wholesale) {
            for (Drawable e : entitiesOnNextPosition) {
                if (e.getObjectType() == DrawableType.Supplier) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void _destroy() {
        Storage.INSTANCE.removeSupplier(this);
    }

    public DoubleProperty trunkOccupancyProperty() {
        return trunkOccupancy;
    }

    public DoubleProperty tankOccupancyProperty() {
        return tankOccupancy;
    }

    public String getCompanyName() {
        return companyName;
    }
}
