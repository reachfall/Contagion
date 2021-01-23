package com.contagion.map;

import com.contagion.person.Client;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TileMapGenerator extends VBox {
    XMLTileReader data;
    private final ArrayList<Canvas> canvases;
    private Affine affine;


    public TileMapGenerator() {
        canvases = new ArrayList<>();
        data = new XMLTileReader("/Users/doot/Desktop/Contagion/res/tile/tilemap.xml");
        for(int i = 0; i < data.getLayers(); i++) {
            canvases.add(new Canvas(400, 400));
        }
//        this.getChildren().addAll(this.canvases);
        Button but = new Button();
        but.setText("asdasd");
        but.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new Client("asdasd", "asdasd", new Position(0, 0), 10);
            }
        });
        this.getChildren().addAll(but);
        this.affine = new Affine();
        this.affine.appendScale(400 / 50f, 400 / 50f);
    }

// temporary
    public static String leftPadStringWithChar(String s, int fixedLength, char c){
        if(fixedLength < s.length()){
            throw new IllegalArgumentException();
        }

        StringBuilder sb = new StringBuilder(s);

        for(int i = 0; i < fixedLength - s.length(); i++){
            sb.insert(0, c);
        }

        return sb.toString();
    }


    synchronized public void drawLayer(int layer){
        int width = data.getWidth();
        int height = data.getHeight();

        int[][] tileMap = data.getTileMapValues()[layer];

        GraphicsContext g = this.canvases.get(layer).getGraphicsContext2D();
        g.setTransform(this.affine);
        g.setFill(Color.TRANSPARENT);
        g.fillRect(0,0,400,400);
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                String color = leftPadStringWithChar(String.valueOf(tileMap[y][x]), 3, '0');
                if(!color.equals("000")){
                    g.setFill(Color.web(color));
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
    }
}
