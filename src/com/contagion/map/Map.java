package com.contagion.map;

import com.contagion.control.*;
import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableShop;
import com.contagion.tiles.DrawableType;
import com.contagion.tiles.Movable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Map extends AnchorPane implements Runnable {
    private static Map instance;
    private final ArrayList<Canvas> canvases = new ArrayList<>();
    private Affine affine;
    private int[][][] tileMapValues;
    private int width;
    private int height;
    private int layers;
    private GraphicsContext entityLayer;
    private GraphicsContext shopLayer;
    private final int mapCanvasSize;
    private UUID trackedMovable;
    private final Object trackedShopMonitor = new Object();

    private final HashMap<Position, List<Movable>> locationToDrawable = new HashMap<>(0);
    final Object locationToDrawableMonitor = new Object();

    private final HashMap<Position, DrawableType> locationToStationaryDrawable = new HashMap<>(0);

    public DrawableType getPositionType(Position position) {
        return locationToStationaryDrawable.get(position);
    }

    public void removeFromLocationToDrawable(Movable entity) {
        synchronized (locationToDrawableMonitor) {
            System.out.println(locationToDrawable);
            locationToDrawable.remove(entity.getPosition());
            System.out.println(locationToDrawable);
        }
    }

    private Map() {
        mapCanvasSize = 750;
        System.out.println("Tworzenie mapy...");
        readXML("src/com/contagion/resources/tilemap.xml");
        prepareCanvases();
        drawLayers();

        ScheduledExecution.getInstance().scheduleAtFixedRate(this, 0, SpeedAndSpeedOnly.INSTANCE.getSpeed(), TimeUnit.MILLISECONDS);
    }

    public void readXML(String path) {

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(path));
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("map");
            Node node = list.item(0);
            Element eElement = (Element) node;

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

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        tileMapValues[i][y][x] = Integer.parseInt(tileMapStrArr[y * width + x]);
                        Position position = new Position(x, y);

                        switch (tileMapValues[i][y][x]) {
                            case 2 -> {
                                locationToStationaryDrawable.put(position, DrawableType.Road);
                                Storage.INSTANCE.addSupplierPossibleSpawnPoints(position);
                            }
                            case 3 -> locationToStationaryDrawable.put(position, DrawableType.Intersection);
                            case 4 -> {
                                locationToStationaryDrawable.put(position, DrawableType.Sidewalk);
                                Storage.INSTANCE.addClientPossibleSpawnPoints(position);
                            }
                            case 5 -> locationToStationaryDrawable.put(position, DrawableType.SidewalkIntersection);
                            case 6 -> locationToStationaryDrawable.put(position, DrawableType.Underpass);
                            case 7 -> {
                                Storage.INSTANCE.createRetailShop(position);
                                locationToStationaryDrawable.put(position, DrawableType.RetailShop);
                            }
                            case 8 -> {
                                Storage.INSTANCE.createWholesale(position);
                                locationToStationaryDrawable.put(position, DrawableType.Wholesale);
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public void prepareCanvases() {
        for (int i = 0; i < layers + 1; i++) {
            canvases.add(new Canvas(mapCanvasSize, mapCanvasSize));
        }
        this.getChildren().addAll(canvases);

        affine = new Affine();
        affine.appendScale((double) mapCanvasSize / width, (double) mapCanvasSize / height);
    }

    public Color mapColor(int tileID) {
//        1. Background -- grey
//        2. Road -- cyan
//        3. Intersection -- cyan
//        4. Sidewalk -- gold
//        5. Sidewalk intersection -- gold
//        6. Underpass -- cyan
//        7. RetailShop -- light pink
//        8. Wholesale -- dark pink
        return switch (tileID) {
            case 1 -> Color.web("333333");
            case 2, 3, 6 -> Color.web("2FF3E0");
            case 4, 5 -> Color.web("F8D210");
            case 7 -> Color.web("D8A7B1");
            case 8 -> Color.web("EF7C8E");
            default -> Color.WHITE;
        };
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
        entityLayer = canvases.get(canvases.size() - 2).getGraphicsContext2D();
        shopLayer = canvases.get(canvases.size() - 1).getGraphicsContext2D();
        entityLayer.setTransform(affine);
        shopLayer.setTransform(affine);
    }

    public Color SpritePicker(Movable entity) {
        if (entity.getId().equals(trackedMovable)) {
            return Color.BLACK;
        }

        if (entity.isSick()) {
            return Color.DARKGREEN;
        }

        switch (entity.getObjectType()) {
            case Client -> {
                return Color.web("FA26A0"); // -- intensive pink
            }
            case Supplier -> {
                return Color.web("F51720"); // -- chilli pepper
            }
        }
        return null;
    }

    public boolean moveToPosition(Movable entity, Position nextPosition) {
        DrawableType stationaryOnNNextPosition = locationToStationaryDrawable.get(nextPosition);

        synchronized (locationToDrawableMonitor) {
            List<Movable> entitiesOnNextPosition = locationToDrawable.get(nextPosition);
            List<Movable> entitiesOnCurrentPosition = locationToDrawable.get(entity.getPosition());

            if (entitiesOnNextPosition == null) {
                if (entitiesOnCurrentPosition != null) {
                    entitiesOnCurrentPosition.remove(entity);
                }
                entity.setPosition(nextPosition);
                locationToDrawable.put(nextPosition, new ArrayList<>(List.of(entity)));
                return true;
            } else {
                if (entity.isSpecialPositionOccupied(stationaryOnNNextPosition, entitiesOnNextPosition, nextPosition)) {
                    return false;
                } else {
                    if (entitiesOnCurrentPosition != null) {
                        entitiesOnCurrentPosition.remove(entity);
                    }
                    entity.setPosition(nextPosition);
                    entitiesOnNextPosition.add(entity);
                    return true;
                }
            }
        }
    }

    public void renderEntities() {
        synchronized (locationToDrawableMonitor) {
            entityLayer.clearRect(0, 0, width, height);

            for (HashMap.Entry<Position, List<Movable>> entry : locationToDrawable.entrySet()) {
                Position position = entry.getKey();
                List<Movable> entitiesOnPosition = entry.getValue();

                //if entity is in the shop do not render
                if (getPositionType(position) != DrawableType.RetailShop && getPositionType(position) != DrawableType.Wholesale) {
                    if (getPositionType(position) == DrawableType.Underpass) {
                        for (Movable entity : entitiesOnPosition) {
                            if (entity.getObjectType() == DrawableType.Supplier) {
                                entityLayer.setFill(SpritePicker(entity));
                                entityLayer.fillRect(entity.getPosition().getX(), entity.getPosition().getY(), 1, 1);
                                break;
                            }
                        }
                    } else {
                        Movable entity = entitiesOnPosition.get(0);
                        entityLayer.setFill(SpritePicker(entity));
                        entityLayer.fillRect(entity.getPosition().getX(), entity.getPosition().getY(), 1, 1);
                    }
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

    @Override
    public void run() {
        if (PhaserExecution.getInstance().getUnarrivedParties() == 1) {
            while (SpeedAndSpeedOnly.INSTANCE.getTimeControl().get()) {
            }
            pandemicControl();
            renderEntities();
            highlightShop();
            PhaserExecution.getInstance().arrive();
        }
    }


    public void pandemicControl() {
        int infected = 0;
        synchronized (locationToDrawableMonitor) {
            Iterator<HashMap.Entry<Position, List<Movable>>> iter = locationToDrawable.entrySet().iterator();
            while (iter.hasNext()) {
                HashMap.Entry<Position, List<Movable>> entry = iter.next();
                List<Movable> entitiesOnPosition = entry.getValue();

                // removes empty locations
                if (entitiesOnPosition.isEmpty()) {
                    iter.remove();
                    continue;
                }
                infected = PandemicControl.INSTANCE.evaluateEntitiesForDisease(entitiesOnPosition, infected);
            }
        }
        PandemicControl.INSTANCE.setLockdown(infected);
    }

    public void setTrackedObject(Drawable o) {
        if (o instanceof Movable) {
            Movable entity = (Movable) o;
            if (entity.getId().equals(this.trackedMovable)) {
                this.trackedMovable = null;
            } else {
                this.trackedMovable = entity.getId();
            }
        } else if (o instanceof DrawableShop) {
            synchronized (trackedShopMonitor) {
                DrawableShop shop = (DrawableShop) o;
                TrackedShop.INSTANCE.setDraw(true);
                TrackedShop.INSTANCE.setLastPosition(TrackedShop.INSTANCE.getCurrentPosition());
                TrackedShop.INSTANCE.setLastType(TrackedShop.INSTANCE.getCurrentType());
                if (shop.getPosition().equals(TrackedShop.INSTANCE.getCurrentPosition())) {
                    TrackedShop.INSTANCE.setCurrentPosition(null);
                } else {
                    TrackedShop.INSTANCE.setCurrentType(shop.getObjectType());
                    TrackedShop.INSTANCE.setCurrentPosition(shop.getPosition());
                }
            }
        }
    }

    //highlight given shop
    private void highlightShop() {
        synchronized (trackedShopMonitor) {
            if (TrackedShop.INSTANCE.isDraw()) {
                if (TrackedShop.INSTANCE.getLastPosition() != null) {
                    shopLayer.setFill(mapColor(TrackedShop.INSTANCE.getLastType() == DrawableType.RetailShop ? 7 : 8));
                    shopLayer.fillRect(TrackedShop.INSTANCE.getLastPosition().getX(), TrackedShop.INSTANCE.getLastPosition().getY(), 1, 1);
                }

                if (TrackedShop.INSTANCE.getCurrentPosition() != null) {
                    shopLayer.setFill(Color.BLACK);
                    shopLayer.fillRect(TrackedShop.INSTANCE.getCurrentPosition().getX(), TrackedShop.INSTANCE.getCurrentPosition().getY(), 1, 1);
                }
                TrackedShop.INSTANCE.setDraw(false);
            }
        }
    }
}
