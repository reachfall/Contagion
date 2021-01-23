package com.contagion.person;

import com.contagion.map.DrawableType;
import com.contagion.map.Map;
import com.contagion.map.Position;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client extends Person {

    private int cartCapacity;
    private ArrayList<String> productsInCart;
    private String nextShop;

    public Client(String name, String surname, Position position, int cartCapacity) {
        super(name, surname, position);
        System.out.println("Created a client");
        this.cartCapacity = cartCapacity;

    }

    @Override
    public void run() {
        System.out.println("moving a client");
        position.setX(12);

        Map.getInstance().drawOnMap(this);
    }

    @Override
    public DrawableType getObjectType() {
        return DrawableType.Client;
    }
}
