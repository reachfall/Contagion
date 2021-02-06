package com.contagion.person;

import com.contagion.control.PhaserExecution;
import com.contagion.control.Randomize;
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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.*;

public class Supplier extends Person {
    private final List<Shop> shopsToVisit;
    private final HashMap<UUID, List<Package>> packageList;
    private final int trunkCapacity;
    private int currentLoad;
    private IntegerProperty fuelLevel;

    public Supplier(String name, String surname, Position position, List<Shop> shopsToVisit) {
        super(name, surname, position);
        this.shopsToVisit = new ArrayList<>(shopsToVisit);
        this.packageList = new HashMap<>();
        this.trunkCapacity = Randomize.INSTANCE.randomNumberGenerator(5, 10);
        this.currentLoad = 0;
        this.fuelLevel = new SimpleIntegerProperty((int) (Storage.INSTANCE.getLongestPath() * 1.1));
    }

    @Override
    public void run() {
        try {
            nextShop();
            interpretInstructions();
        } catch (Exception e) {
            System.err.println("Exception in supplier " + this.toString());
            System.err.println(e.getStackTrace());
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
            instructions.add("deliverPackages");
        } else {
            instructions.add("pickUpPackages");
        }
    }

    //adding new packages, one for each retail shop in shopsToVisit -- takes one move
    public void pickUpPackages() {
        Wholesale wholesale = (Wholesale) Storage.INSTANCE.getShopOnPosition(position);
        for (Shop shop : shopsToVisit) {
            if (currentLoad < trunkCapacity) {
                if (shop.getObjectType() == DrawableType.RetailShop) {
                    Package newPackage = wholesale.createPackage(shop.getId());
                    List<Package> packageList = this.packageList.get(shop.getId());
                    if (packageList == null) {
                        this.packageList.put(shop.getId(), new ArrayList<>(Arrays.asList(newPackage)));
                    } else {
                        packageList.add(newPackage);
                    }
                }
            } else {
                break;
            }
        }
        instructions.remove(0);
    }

    //deliver package(s) to retail shop
    public void deliverPackages() {
        RetailShop retailShop = (RetailShop) Storage.INSTANCE.getShopOnPosition(position);
        List<Package> packageList = this.packageList.get(retailShop.getId());
        for (int i = 0; i < packageList.size(); i++) {
            retailShop.receivePackage(packageList.remove(i));
        }
        instructions.remove(0);
    }

    public void fillTank() {
        fuelLevel.set((int) (Storage.INSTANCE.getLongestPath() * 1.1));
    }

    public void interpretInstructions() {
        switch (instructions.get(0)) {
            case "pickUpPackages":
                fillTank();
                shopCuration();
                pickUpPackages();
                break;
            case "deliverPackages":
                fillTank();
                shopCuration();
                deliverPackages();
                break;
            case "stop":
                if (!shopsToVisit.isEmpty()) {
                    instructions.remove(0);
                }
                break;
            case "up":
                if (Map.getInstance().moveToPosition(this, new Position(position.getX(), position.getY() - 1))) {
                    instructions.remove(0);
                    fuelLevel.setValue(fuelLevel.getValue() - 1);
                }
                break;
            case "down":
                if (Map.getInstance().moveToPosition(this, new Position(position.getX(), position.getY() + 1))) {
                    instructions.remove(0);
                    fuelLevel.setValue(fuelLevel.getValue() - 1);
                }
                break;
            case "left":
                if (Map.getInstance().moveToPosition(this, new Position(position.getX() - 1, position.getY()))) {
                    instructions.remove(0);
                    fuelLevel.setValue(fuelLevel.getValue() - 1);

                }
                break;
            case "right":
                if (Map.getInstance().moveToPosition(this, new Position(position.getX() + 1, position.getY()))) {
                    instructions.remove(0);
                    fuelLevel.setValue(fuelLevel.getValue() - 1);

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
    public boolean isSpecialPositionOccupied(Drawable stationaryObjectInNewPosition, List<Movable> entitiesOnNextPosition, Position position) {
        if (stationaryObjectInNewPosition.getObjectType() == DrawableType.Intersection ||
                stationaryObjectInNewPosition.getObjectType() == DrawableType.RetailShop ||
                stationaryObjectInNewPosition.getObjectType() == DrawableType.Wholesale) {
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

    public IntegerProperty fuelLevelProperty() {
        return fuelLevel;
    }
}
