package com.contagion.viewManager.controllers;

import com.contagion.map.Map;
import com.contagion.map.Position;
import com.contagion.person.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AddClient {

    @FXML
    private void createRandomClient(ActionEvent actionEvent) {
        Client client = new Client("asdasd", "asdasd", String.valueOf(Math.random()), new Position(0, 0), 10, Map.getInstance().getPhaser());
    }

    public void saveClient(ActionEvent actionEvent) {

    }
}
