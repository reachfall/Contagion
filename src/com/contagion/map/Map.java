package com.contagion.map;

import com.contagion.control.Storage;
import com.contagion.infrastructure.*;
import com.contagion.person.Client;
import com.contagion.shop.RetailShop;
import com.contagion.shop.Wholesale;
import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableType;
import com.contagion.tiles.Movable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Map extends AnchorPane {
    private static Map instance;
    private final ArrayList<Canvas> canvases = new ArrayList<>();
    private Affine affine;
    private int[][][] tileMapValues;
    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;
    private int layers;
    private GraphicsContext gObjLayer;

    private HashMap<Position, ArrayList<Drawable>> locationToDrawable = new HashMap<>(0);
    Object locationToDrawableMonitor = new Object();
    private HashMap<Position, Drawable> locationToStationaryDrawable = new HashMap<>(0);

    public void createHorde(){
        for (int i = 0; i < 10; i++) {
            new Client("aaa", "aaa", String.valueOf(Math.random()), new Position(7, 5), 10);
        }
    }

    private Map() {
        System.out.println("Tworzenie mapy...");

        readXML("/Users/doot/Desktop/Contagion/res/tile/tilemap.xml");
        prepareCanvases();
        drawLayers();

        Button but = new Button();
        but.setText("Add client");
        but.setOnAction(actionEvent -> createHorde());
        this.getChildren().addAll(but);
    }


    public HashMap<Position, ArrayList<Drawable>> getLocationToDrawable() {
        return locationToDrawable;
    }

    public HashMap<Position, Drawable> getLocationToStationaryDrawable() {
        return locationToStationaryDrawable;
    }

    public void readXML(String path) {

        String imagePath;

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(path));
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("map");
            Node node = list.item(0);
            Element eElement = (Element) node;

            imagePath = eElement.getAttribute("name");
            tileWidth = Integer.parseInt(eElement.getAttribute("tilewidth"));
            tileHeight = Integer.parseInt(eElement.getAttribute("tileheight"));
            width = Integer.parseInt(eElement.getAttribute("width"));
            height = Integer.parseInt(eElement.getAttribute("height"));

            list = doc.getElementsByTagName("layer");
            layers = list.getLength();

            String[] tileMapStrArr;
            tileMapValues = new int[layers][width][height];

            for (int i = 0; i < layers; i++) {
                node = list.item(i);
                eElement = (Element) node;
                tileMapStrArr = eElement.getElementsByTagName("data").item(0).getTextContent().replaceAll("\\s+", "").split(",");

                Storage storage = Storage.getInstance();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        tileMapValues[i][y][x] = Integer.parseInt(tileMapStrArr[y * width + x]);

                        switch (tileMapValues[i][y][x]) {
                            case 2:
                                locationToStationaryDrawable.put(new Position(x, y), new Road());
                                break;
                            case 3:
                                locationToStationaryDrawable.put(new Position(x, y), new Intersection());
                                break;
                            case 4:
                                locationToStationaryDrawable.put(new Position(x, y), new Sidewalk());
                                break;
                            case 5:
                                locationToStationaryDrawable.put(new Position(x, y), new SidewalkIntersection());
                                break;
                            case 6:
                                locationToStationaryDrawable.put(new Position(x, y), new Underpass());
                                break;
                            case 7:
                                RetailShop retailShop = new RetailShop(new Position(x, y));
                                locationToStationaryDrawable.put(retailShop.getPosition(), retailShop);
                                storage.getRetailShops().add(retailShop);
                                storage.getAllShops().add(retailShop);
                                storage.getLocationToShop().put(retailShop.getPosition(), retailShop);
                                break;
                            case 8:
                                Wholesale wholesale = new Wholesale(new Position(x, y));
                                locationToStationaryDrawable.put(wholesale.getPosition(), wholesale);
                                storage.getWholesales().add(wholesale);
                                storage.getAllShops().add(wholesale);
                                storage.getLocationToShop().put(wholesale.getPosition(), wholesale);
                                break;
                        }
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prepareCanvases() {
        for (int i = 0; i < layers + 1; i++) {
            canvases.add(new Canvas(400, 400));
        }
        this.getChildren().addAll(canvases);

        affine = new Affine();
        affine.appendScale(400 / width, 400 / height);
    }

    public Color mapColor(int tileID) {
//        1. Background
//        2. Road
//        3. Intersection
//        4. Sidewalk
//        5. Sidewalk intersection
//        6. Underpass
//        7. RetailShop
//        8. Wholesale
        switch (tileID) {
            case 1:
                return Color.web("000000");
            case 2:
            case 6:
                return Color.web("fbf236");
            case 3:
                return Color.web("99e550");
            case 4:
                return Color.web("639bff");
            case 5:
                return Color.web("5fcde4");
            case 7:
                return Color.web("d95763");
            case 8:
                return Color.web("d77bba");
            default:
                return Color.WHITE;
        }
    }

    public void drawLayers() {
        for (int i = 0; i < layers; i++) {
            int canvas;
            if (i == layers - 1) {
                canvas = i + 1;
            } else {
                canvas = i;
            }

            GraphicsContext g = canvases.get(canvas).getGraphicsContext2D();
            g.setTransform(affine);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (tileMapValues[i][y][x] != 0) {
                        g.setFill(mapColor(tileMapValues[i][y][x]));
                        g.fillRect(x, y, 1, 1);
                    }
                }
            }
        }
        gObjLayer = canvases.get(canvases.size() - 2).getGraphicsContext2D();
        gObjLayer.setTransform(affine);
    }

    public Color SpritePicker(Drawable drawable) {
        switch (drawable.getObjectType()) {
            case Client -> {
                return Color.WHITE;
            }
            case Supplier -> {
                return Color.DARKBLUE;
            }
            case None -> {
                return Color.RED;
            }
        }
        return null;
    }

//    public boolean isIntersectionOccupied(Drawable stationaryObjectInNewPosition, ArrayList<Drawable> entitiesOnNextPosition) {
//        return (stationaryObjectInNewPosition.getObjectType() == DrawableType.Intersection ||
//                stationaryObjectInNewPosition.getObjectType() == DrawableType.SidewalkIntersection) && !entitiesOnNextPosition.isEmpty();
//    }
//
//    public boolean isShopOccupied(Drawable stationaryObjectInNewPosition, ArrayList<Drawable> entitiesOnNextPosition, Position position) {
//        return (stationaryObjectInNewPosition.getObjectType() == DrawableType.RetailShop ||
//                stationaryObjectInNewPosition.getObjectType() == DrawableType.Wholesale)
//                && entitiesOnNextPosition.size() >= Storage.getInstance().getLocationToShop().get(position).getActualCapacity();
//    }

    public void moveToPosition(Movable objectToMove, Position position) {
        Drawable stationaryObjectInNewPosition = locationToStationaryDrawable.get(position);

        boolean canMove = true;

        synchronized (locationToDrawableMonitor) {

            ArrayList<Drawable> entitiesOnNextPosition = locationToDrawable.get(position);
            ArrayList<Drawable> entitiesOnCurrentPosition = locationToDrawable.get(objectToMove.getPosition());

            if (entitiesOnNextPosition != null) {
                if (objectToMove.isSpecialPositionOccupied(stationaryObjectInNewPosition, entitiesOnNextPosition, position)) {
                    canMove = false;
                }

                if (canMove) {
                    if (entitiesOnCurrentPosition != null) {
                        entitiesOnCurrentPosition.remove(objectToMove);
                    }
                    objectToMove.setLastPosition(objectToMove.getPosition());
                    objectToMove.setPosition(position);
                    entitiesOnNextPosition.add(objectToMove);
                } else {
                    objectToMove.setLastPosition(objectToMove.getPosition());
                }

            } else {
                if (entitiesOnCurrentPosition != null) {
                    entitiesOnCurrentPosition.remove(objectToMove);
                }
                objectToMove.setLastPosition(objectToMove.getPosition());
                objectToMove.setPosition(position);
                locationToDrawable.put(position, new ArrayList<>(List.of(objectToMove)));
            }
        }
    }

    public boolean isNotUnderpassClient(Movable movable) {
        return !(getLocationToStationaryDrawable().get(movable.getPosition()).getObjectType() == DrawableType.Underpass
                && movable.getObjectType() == DrawableType.Client);
    }

    synchronized public void drawOnMap(Movable movable) {
        gObjLayer.clearRect(movable.getLastPosition().getX(), movable.getLastPosition().getY(), 1, 1);

        // if Client is on Underpass type don't render Client sprite
        if (getLocationToStationaryDrawable().get(movable.getPosition()) != null && isNotUnderpassClient(movable)) {
            gObjLayer.setFill(SpritePicker(movable));
            gObjLayer.fillRect(movable.getPosition().getX(), movable.getPosition().getY(), 1, 1);
        }

        List<Drawable> objOnlastPosition = locationToDrawable.get(movable.getLastPosition());

        if (objOnlastPosition != null) {
            if (objOnlastPosition.isEmpty()) {
                locationToDrawable.remove(movable.getLastPosition());
            } else {
                // if last position was underpass check if there is supplier to render
                if (getLocationToStationaryDrawable().get(movable.getPosition()).getObjectType() == DrawableType.Underpass) {
                    Drawable draw = objOnlastPosition.stream()
                            .filter(drawable -> drawable.getObjectType().equals(DrawableType.Supplier))
                            .findAny()
                            .orElse(null);
                    if(draw != null) {
                        gObjLayer.setFill(SpritePicker(draw));
                        gObjLayer.fillRect(movable.getLastPosition().getX(), movable.getLastPosition().getY(), 1, 1);
                    }
                } else {
                    Drawable draw = objOnlastPosition.get(0);
                    gObjLayer.setFill(SpritePicker(draw));
                    gObjLayer.fillRect(movable.getLastPosition().getX(), movable.getLastPosition().getY(), 1, 1);
                }
            }
        }
    }

    public static Map getInstance() {
        if (instance == null) {
            instance = new Map();
        }
        return instance;
    }
}
