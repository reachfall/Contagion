package com.contagion.control;

import com.contagion.map.Position;
import com.contagion.person.Client;
import com.contagion.person.Supplier;
import com.contagion.shop.RetailShop;
import com.contagion.shop.Shop;
import com.contagion.shop.Wholesale;

import java.util.ArrayList;
import java.util.HashMap;

public enum Storage {
    INSTANCE;
    private final ArrayList<Client> clients = new ArrayList<>();
    private final ArrayList<Supplier> suppliers = new ArrayList<>();
    private final ArrayList<RetailShop> retailShops = new ArrayList<>();
    private final ArrayList<Wholesale> wholesales = new ArrayList<>();
    private final ArrayList<Shop> allShops = new ArrayList<>();
    private final HashMap<Position, Shop> locationToShop = new HashMap<>();

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

    public Shop getShopOnPosition(Position position) {
        return locationToShop.get(position);
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void addSupplier(Supplier supplier) {
        suppliers.add(supplier);
    }

    public void addRetailShop(RetailShop retailShop) {
        retailShops.add(retailShop);
    }

    public void addWholesale(Wholesale wholesale) {
        wholesales.add(wholesale);
    }

    public void addShop(Shop shop) {
        allShops.add(shop);
    }

    public void addLocationToShop(Position position, Shop shop) {
        locationToShop.put(position, shop);
    }
}
