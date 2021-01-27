package com.contagion.person;

import com.contagion.control.Storage;
import com.contagion.map.Map;
import com.contagion.shop.Product;
import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableType;
import com.contagion.map.Position;

import java.util.ArrayList;

public class Supplier extends Person {

    public Supplier(String name, String surname, String id, Position position) {
        super(name, surname, id, position);
    }

    @Override
    public void run() {

    }

    //TODO
    public void interpretInstructions() {
        switch (instructions.get(0)) {
            case "shop":
//                getShoppingInstructions();
                instructions.remove(0);
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

    @Override
    public DrawableType getObjectType() {
        return DrawableType.Supplier;
    }

    @Override
    public boolean isSpecialPositionOccupied(Drawable stationaryObjectInNewPosition, ArrayList<Drawable> entitiesOnNextPosition, Position position) {
        if (stationaryObjectInNewPosition.getObjectType() == DrawableType.Intersection ||
                stationaryObjectInNewPosition.getObjectType() == DrawableType.RetailShop ||
                stationaryObjectInNewPosition.getObjectType() == DrawableType.Wholesale) {
            for(Drawable e: entitiesOnNextPosition){
                if(e.getObjectType() == DrawableType.Supplier){
                    return true;
                }
            }
        }
        return false;
    }
}
