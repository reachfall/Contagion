package com.contagion.control;

import com.contagion.map.Position;
import com.contagion.pathfinding.Pathfinder;
import com.contagion.person.Client;
import com.contagion.person.Person;
import com.contagion.person.Supplier;
import com.contagion.shop.RetailShop;
import com.contagion.shop.Shop;
import com.contagion.shop.Wholesale;
import com.contagion.tiles.DrawableType;
import com.contagion.tiles.Movable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;

public enum Storage {
    INSTANCE;
    private final ObservableList clients = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private final ObservableList suppliers = FXCollections.synchronizedObservableList(FXCollections.observableArrayList());
    private final HashMap<Person, ScheduledFuture<?>> personToFuture = new HashMap<>();
    private final ObservableList retailShops = FXCollections.observableArrayList();
    private final ObservableList wholesales = FXCollections.observableArrayList();
    private final ArrayList<Shop> allShops = new ArrayList<>();
    private final HashMap<Position, Shop> locationToShop = new HashMap<>();
    private int longestPath;

    public ObservableList<Client> getClients() {
        return clients;
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void removeClient(Movable entity) {
        clients.remove(entity);
    }


    public ObservableList<Supplier> getSuppliers() {
        return suppliers;
    }

    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
    }

    public void removeSupplier(Movable entity) {
        suppliers.remove(entity);
    }


    public ObservableList getRetailShops() {
        return retailShops;
    }

    public void addRetailShop(RetailShop retailShop) {
        retailShops.add(retailShop);
    }


    public ObservableList getWholesales() {
        return wholesales;
    }

    public void addWholesale(Wholesale wholesale) {
        wholesales.add(wholesale);
    }


    public ArrayList<Shop> getAllShops() {
        return allShops;
    }

    public void addShop(Shop shop) {
        allShops.add(shop);
    }


    public Shop getShopOnPosition(Position position) {
        return locationToShop.get(position);
    }

    public void addLocationToShop(Position position, Shop shop) {
        locationToShop.put(position, shop);
    }


    public void addPersonToFutureMap(Person person, ScheduledFuture<?> future) {
        personToFuture.put(person, future);
    }

    public void removePersonToFutureMap(Person person) {
        personToFuture.remove(person).cancel(false);
    }

    /**
     * Additional setup, pathfinding needs Map so it has to be invoked outside Map constructor
     * find longest path to set supplier max fuel level
     */
    public void findLongestPath() {
        int longestPath = 0;

        for (int i = 0; i < allShops.size(); i++) {
            for (int j = i + 1; j < allShops.size(); j++) {
                int currentPath = Pathfinder.findPath(allShops.get(i).getPosition(), allShops.get(j).getPosition(), DrawableType.Supplier).size();
                if (currentPath > longestPath) {
                    longestPath = currentPath;
                }
            }
        }
        this.longestPath = longestPath;
    }

    public int getLongestPath() {
        return longestPath;
    }

    public void setAllShopsCapacity(boolean isLockdown) {
        if (isLockdown) {
            for (Shop shop : allShops) {
                shop.lockdown();
            }
        } else {
            for (Shop shop : allShops) {
                shop.endLockdown();
            }
        }
    }
}
