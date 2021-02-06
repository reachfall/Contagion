package com.contagion.map;

import com.contagion.control.*;
import com.contagion.infrastructure.*;
import com.contagion.person.Client;
import com.contagion.person.Supplier;
import com.contagion.shop.RetailShop;
import com.contagion.shop.Shop;
import com.contagion.shop.Wholesale;
import com.contagion.tiles.Drawable;
import com.contagion.tiles.DrawableType;
import com.contagion.tiles.Movable;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
import java.awt.*;
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
    private int tileWidth;
    private int tileHeight;
    private int layers;
    private GraphicsContext gObjLayer;
    private final int canvasHeight;

    private HashMap<Position, List<Movable>> locationToDrawable = new HashMap<>(0);
    Object locationToDrawableMonitor = new Object();

    private HashMap<Position, Drawable> locationToStationaryDrawable = new HashMap<>(0);

    public DrawableType getPositionType(Position position) {
        return locationToStationaryDrawable.get(position).getObjectType();
    }

    public void removeFromLocationToDrawable(Movable entity) {
        synchronized (locationToDrawableMonitor) {
            System.out.println(locationToDrawable);
            locationToDrawable.remove(entity.getPosition());
            System.out.println(locationToDrawable);
        }
    }

    private Map() {
        System.out.println("Tworzenie mapy...");

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        canvasHeight = dim.height - 30;
        readXML("/Users/doot/Desktop/Contagion/res/tile/tilemap.xml");
        prepareCanvases();
        drawLayers();

        Button client = new Button();
        client.setText("Add client");
        client.setOnAction(actionEvent -> createClientHorde());

        Button supplier = new Button();
        supplier.setText("Add supplier");
        supplier.setOnAction(actionEvent -> createSupplierHorde());

        Button removeClient = new Button();
        removeClient.setText("Remove client");
        removeClient.setOnAction(actionEvent -> Storage.INSTANCE.getClients().get(0).destroy());

        HBox hBox = new HBox();
        hBox.getChildren().add(client);
        hBox.getChildren().add(supplier);
        hBox.getChildren().add(removeClient);
        this.getChildren().add(hBox);

        ScheduledExecution.getInstance().scheduleAtFixedRate(this::run, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void createClientHorde() {
        for (int i = 0; i < 10; i++) {
            Storage.INSTANCE.addClient(new Client("aaa", "aaa", new Position(7, 5), 10));
        }
    }

    public void createSupplierHorde() {
        List retailShops = Storage.INSTANCE.getRetailShops();
        List wholesales = Storage.INSTANCE.getWholesales();

        for (int i = 0; i < 10; i++) {
            List<Shop> shops = new ArrayList<>(Randomize.INSTANCE.sample(wholesales, Randomize.INSTANCE.randomNumberGenerator(1, wholesales.size() - 1)));
            shops.addAll(Randomize.INSTANCE.sample(retailShops, Randomize.INSTANCE.randomNumberGenerator(1, retailShops.size() - 1)));

            Storage.INSTANCE.addSupplier(new Supplier("aaa", "aaa", new Position(7, 8), shops));
        }
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

                Storage storage = Storage.INSTANCE;

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
                                RetailShop retailShop = Randomize.INSTANCE.createRetailShop(new Position(x, y));
                                locationToStationaryDrawable.put(retailShop.getPosition(), retailShop);
                                storage.addRetailShop(retailShop);
                                storage.addShop(retailShop);
                                storage.addLocationToShop(retailShop.getPosition(), retailShop);
                                break;
                            case 8:
                                Wholesale wholesale = Randomize.INSTANCE.createWholesale(new Position(x, y));
                                locationToStationaryDrawable.put(wholesale.getPosition(), wholesale);
                                storage.addWholesale(wholesale);
                                storage.addShop(wholesale);
                                storage.addLocationToShop(wholesale.getPosition(), wholesale);
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
            canvases.add(new Canvas(canvasHeight, canvasHeight));
        }
        this.getChildren().addAll(canvases);

        affine = new Affine();
        affine.appendScale(canvasHeight / width, canvasHeight / height);
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

    public Color SpritePicker(Movable entity) {
        if (entity.isSick()) {
            return Color.LIGHTGREEN;
        }
        switch (entity.getObjectType()) {
            case Client -> {
                return Color.WHITE;
            }
            case Supplier -> {
                return Color.DARKBLUE;
            }
        }
        return null;
    }

    public boolean moveToPosition(Movable entity, Position nextPosition) {
        Drawable stationaryOnNNextPosition = locationToStationaryDrawable.get(nextPosition);

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
            gObjLayer.clearRect(0, 0, width, height);

            Iterator<HashMap.Entry<Position, List<Movable>>> iter = locationToDrawable.entrySet().iterator();
            while (iter.hasNext()) {
                HashMap.Entry<Position, List<Movable>> entry = iter.next();
                Position position = entry.getKey();
                List<Movable> entitiesOnPosition = entry.getValue();

                //if entity is in the shop do not render
                if (getPositionType(position) != DrawableType.RetailShop && getPositionType(position) != DrawableType.Wholesale) {
                    if (getPositionType(position) == DrawableType.Underpass) {
                        for (Movable entity : entitiesOnPosition) {
                            if (entity.getObjectType() == DrawableType.Supplier) {
                                gObjLayer.setFill(SpritePicker(entity));
                                gObjLayer.fillRect(entity.getPosition().getX(), entity.getPosition().getY(), 1, 1);
                                break;
                            }
                        }
                    } else {
                        Movable entity = entitiesOnPosition.get(0);
                        gObjLayer.setFill(SpritePicker(entity));
                        gObjLayer.fillRect(entity.getPosition().getX(), entity.getPosition().getY(), 1, 1);
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
            pandemicControl();
            renderEntities();
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

                // possibility of infection without infected 0.03
                double infectionProbability = PandemicControl.INSTANCE.getInitialSicknessProbability();

                for (Movable entity : entitiesOnPosition) {
                    if (entity.isSick()) {
                        infectionProbability += PandemicControl.INSTANCE.getSicknessSpreadProbability();
                    }
                }

                for (Movable entity : entitiesOnPosition) {

                    if (!entity.isSick()) {
                        double probability = entity.isVaccinated() ? infectionProbability - PandemicControl.INSTANCE.getVaccineProtectionProbability() : infectionProbability;
                        if ((entity.getObjectType() == DrawableType.Supplier ? probability / PandemicControl.INSTANCE.getSupplierViralSecurityRatio() : probability) > Math.random()) {
                            entity.setSick();
                            infected++;
                        }
                    }
                }
            }
        }
        PandemicControl.INSTANCE.setLockdown(infected);
    }
}
