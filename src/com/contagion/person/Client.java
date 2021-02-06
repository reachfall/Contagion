package com.contagion.person;

import com.contagion.control.PhaserExecution;
import com.contagion.control.ScheduledExecution;
import com.contagion.control.Storage;
import com.contagion.pathfinding.Pathfinder;
import com.contagion.shop.Product;
import com.contagion.shop.Shop;
import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableType;
import com.contagion.map.Map;
import com.contagion.map.Position;
import com.contagion.tiles.Movable;

import java.util.ArrayList;
import java.util.List;

public class Client extends Person {

    private final int cartCapacity;
    private final ArrayList<Product> productsInCart;
    private String nextShop;

    public Client(String name, String surname, Position position, int cartCapacity) {
        super(name, surname, position);
        this.cartCapacity = cartCapacity;
        this.productsInCart = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            findWayToShop();
            interpretInstructions();
        } catch (Exception e) {
            System.err.println("Exception in client " + this.toString());
            System.err.println(e.getStackTrace());
        } finally {
            PhaserExecution.getInstance().arriveAndAwaitAdvance();
        }
    }

    @Override
    public DrawableType getObjectType() {
        return DrawableType.Client;
    }

    public int randomPick(int max) {
        return (int) (Math.random() * (max + 1));
    }

    public void getShoppingInstructions() {
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
        instructions.add("exit");
    }

    public void findWayToShop() {
        if (instructions.isEmpty()){
            Shop shop = Storage.INSTANCE.getAllShops().get(randomPick(Storage.INSTANCE.getAllShops().size() - 1));
            nextShop = shop.getName();
            ArrayList<String> newInstructionsQueue = Pathfinder.findPath(position, shop.getPosition(), this.getObjectType());
            if (newInstructionsQueue == null) {
                throw new NullPointerException("Can't find path");
            }
            instructions.addAll(newInstructionsQueue);
            instructions.add("shop");
        }
    }

    public void interpretInstructions() {
        switch (instructions.get(0)) {
            case "shop":
                shopCuration();
                getShoppingInstructions();
                instructions.remove(0);
                break;
            case "exit":
                findWayToShop();
                instructions.remove(0);
                break;
            case "eat":
                productsInCart.remove(productsInCart.size() - 1);
                instructions.remove(0);
                break;
            case "buy":
                Product product = Storage.INSTANCE.getShopOnPosition(position).getRandomProduct();
                if (product != null) {
                    productsInCart.add(product);
                    instructions.remove(0);
                }
                break;
            case "up":
                if (Map.getInstance().moveToPosition(this, new Position(position.getX(), position.getY() - 1))) {
                    instructions.remove(0);
                }
                break;
            case "down":
                if (Map.getInstance().moveToPosition(this, new Position(position.getX(), position.getY() + 1))) {
                    instructions.remove(0);
                }
                break;
            case "left":
                if (Map.getInstance().moveToPosition(this, new Position(position.getX() - 1, position.getY()))) {
                    instructions.remove(0);
                }
                break;
            case "right":
                if (Map.getInstance().moveToPosition(this, new Position(position.getX() + 1, position.getY()))) {
                    instructions.remove(0);
                }
                break;
            default:
                instructions.remove(0);
        }
    }

    @Override
    public boolean isSpecialPositionOccupied(Drawable stationaryObjectInNewPosition, List<Movable> entitiesOnNextPosition, Position position) {
        long clientsOnPosition = entitiesOnNextPosition.stream().filter(o -> o.getObjectType() == DrawableType.Client).count();

        return (stationaryObjectInNewPosition.getObjectType() == DrawableType.SidewalkIntersection && !entitiesOnNextPosition.isEmpty()) ||
                ((stationaryObjectInNewPosition.getObjectType() == DrawableType.RetailShop ||
                        stationaryObjectInNewPosition.getObjectType() == DrawableType.Wholesale)
                        && clientsOnPosition >= Storage.INSTANCE.getShopOnPosition(position).getActualCapacity());
    }

    @Override
    protected void _destroy() {
        Storage.INSTANCE.removeClient(this);
    }
}
