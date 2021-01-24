package com.contagion.person;

import com.contagion.control.Storage;
import com.contagion.pathfinding.Pathfinder;
import com.contagion.shop.Product;
import com.contagion.shop.Shop;
import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableType;
import com.contagion.map.Map;
import com.contagion.map.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Client extends Person {

    private int cartCapacity;
    private ArrayList<Product> productsInCart;
    private String nextShop;

    public Client(String name, String surname, String id, Position position, int cartCapacity) {
        super(name, surname, id, position);
        this.cartCapacity = cartCapacity;
        System.out.println("Created a client");
    }

    @Override
    public void run() {
        try {
            getInstructions();
            if (!instructions.isEmpty()) {
                interpretInstructions();
            }
            Map.getInstance().drawOnMap(this);
        } catch (Exception e) {
            System.err.println("Exception in client" + this.toString());
            System.err.println(e.getStackTrace());
        }
    }

    @Override
    public DrawableType getObjectType() {
        return DrawableType.Client;
    }

    public int randomPick(int max) {
        return (int) (Math.random() * (max +1));
    }

    public void getInstructions() {
        if (instructions.isEmpty()) {
            DrawableType draw = Map.getInstance().getLocationToStationaryDrawable().get(position).getObjectType();
            if (draw == DrawableType.RetailShop || draw == DrawableType.Wholesale) {
                int noProductsToPick = randomPick(cartCapacity);
                int noProductsToEat = productsInCart.size() + noProductsToPick - cartCapacity;
                if (noProductsToEat > 0) {
                    for (int i = 0; i < noProductsToEat; i++) {
                        instructions.add("eat");
                    }
                }
                for (int i = 0; i < noProductsToPick; i++) {
                    instructions.add("buy");
                }
            } else {
                Shop shop = Storage.getInstance().getAllShops().get(randomPick(Storage.getInstance().getAllShops().size()-1));
                nextShop = shop.getName();
                instructions.addAll(Pathfinder.findPath(position, shop.getPosition(), this.getObjectType()));
            }
        }
    }

    public void interpretInstructions() {
        switch (instructions.get(0)) {
            case "eat":
                productsInCart.remove(productsInCart.size() - 1);
                instructions.remove(0);
                break;
            case "buy":
                Product product = Storage.getInstance().getLocationToShop().get(position).getRandomProduct();
                if (product != null) {
                    productsInCart.add(product);
                    instructions.remove(0);
                }
                break;
            case "up":
                Map.getInstance().moveToPosition(this, new Position(position.getX(), position.getY() - 1));
                if (comparePositions()) {
                    instructions.remove(0);
                }
                break;
            case "down":
                Map.getInstance().moveToPosition(this, new Position(position.getX(), position.getY() + 1));
                if (comparePositions()) {
                    instructions.remove(0);
                }
                break;
            case "left":
                Map.getInstance().moveToPosition(this, new Position(position.getX() - 1, position.getY()));
                if (comparePositions()) {
                    instructions.remove(0);
                }
                break;
            case "right":
                Map.getInstance().moveToPosition(this, new Position(position.getX() + 1, position.getY()));
                if (comparePositions()) {
                    instructions.remove(0);
                }
                break;
            default:
                lastPosition = position;
                instructions.remove(0);
        }
    }
}
