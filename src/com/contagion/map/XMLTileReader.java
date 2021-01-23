package com.contagion.map;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

public class XMLTileReader {

    private int[][][] tileMapValues;
    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;
    private int layers;

    public int[][][] getTileMapValues() {
        return tileMapValues;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getLayers() {
        return layers;
    }


    public XMLTileReader(String path) {
        addTileMap(path);
    }

    private void addTileMap(String path) {

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
            this.tileWidth = Integer.parseInt(eElement.getAttribute("tilewidth"));
            this.tileHeight = Integer.parseInt(eElement.getAttribute("tileheight"));
            this.width = Integer.parseInt(eElement.getAttribute("width"));
            this.height = Integer.parseInt(eElement.getAttribute("height"));

            list = doc.getElementsByTagName("layer");
            this.layers = list.getLength();

            String[] tileMapStrArr;
            this.tileMapValues = new int[this.layers][this.width][this.height];

            for(int i = 0; i < layers; i++){
                node = list.item(i);
                eElement = (Element) node;
                tileMapStrArr = eElement.getElementsByTagName("data").item(0).getTextContent().replaceAll("\\s+", "").split(",");

                for(int y = 0; y < height; y++){
                    for(int x = 0; x < width; x++){
                        this.tileMapValues[i][y][x] = Integer.parseInt(tileMapStrArr[y * this.width + x]);
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
}
