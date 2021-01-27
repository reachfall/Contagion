package com.contagion.control;

import com.contagion.map.Position;
import com.contagion.person.Client;
import com.contagion.person.Supplier;
import com.contagion.shop.RetailShop;
import com.contagion.shop.Shop;
import com.contagion.shop.Wholesale;

import java.util.ArrayList;
import java.util.HashMap;

public class Storage {
    private static Storage instance;
    private final ArrayList<Client> clients = new ArrayList<>();
    private final ArrayList<Supplier> suppliers = new ArrayList<>();
    private final ArrayList<RetailShop> retailShops = new ArrayList<>();
    private final ArrayList<Wholesale> wholesales = new ArrayList<>();
    private final ArrayList<Shop> allShops = new ArrayList<>();
    private final HashMap<Position, Shop> locationToShop = new HashMap<>();

    private Storage() {
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public ArrayList<Supplier> getSuppliers() {
        return suppliers;
    }

    public ArrayList<RetailShop> getRetailShops() {
        return retailShops;
    }

    public ArrayList<Wholesale> getWholesales() {
        return wholesales;
    }

    public ArrayList<Shop> getAllShops() {
        return allShops;
    }

    public HashMap<Position, Shop> getLocationToShop() {
        return locationToShop;
    }
}
