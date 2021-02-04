package com.contagion.viewManager.controllers;

import com.contagion.map.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class SidePanelControl {
    @FXML private AnchorPane display;

    public void showClientsDetails(ActionEvent actionEvent) {
    }

    public void addClient(ActionEvent actionEvent) {
    }

    public void showSuppliersDetails(ActionEvent actionEvent) {
    }

    public void addSupplier(ActionEvent actionEvent) {
    }

    public void showRetailShopDetails(ActionEvent actionEvent) {
    }

    public void showWholesaleDetails(ActionEvent actionEvent) {
    }

    @FXML public void createAndAddMap() {
        Map map = Map.getInstance();
        display.getChildren().add(map);
    }
}
