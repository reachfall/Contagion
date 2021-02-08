package com.contagion.viewManager.controllers;

import com.contagion.control.SpeedAndSpeedOnly;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class SetSpeed implements Initializable {
    @FXML
    private Button verySlow;
    @FXML
    private Button slow;
    @FXML
    private Button medium;
    @FXML
    private Button fast;
    @FXML
    private Button veryFast;
    @FXML
    private Button tooFast;
    @FXML
    private Button ludicrousSpeed;

    private void startSimulation() {
        Stage simulation = new Stage();
        URL simulationUrl = null;
        try {
            simulationUrl = new File("src/com/contagion/viewManager/views/Simulation.fxml").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        FXMLLoader simulationLoader = new FXMLLoader(simulationUrl);
        SplitPane splitPane = null;
        try {
            splitPane = simulationLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert splitPane != null;
        simulation.setScene(new Scene(splitPane));
        simulation.setResizable(false);
        simulation.setTitle("Pandemic simulation");
        simulation.setOnCloseRequest(windowEvent -> Platform.exit());
        simulation.show();
        Stage currentStage = (Stage) verySlow.getScene().getWindow();
        currentStage.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        verySlow.setOnAction(actionEvent -> {
            SpeedAndSpeedOnly.INSTANCE.setSpeed(1000);
            startSimulation();
        });
        slow.setOnAction(actionEvent -> {
            SpeedAndSpeedOnly.INSTANCE.setSpeed(600);
            startSimulation();
        });
        medium.setOnAction(actionEvent -> {
            SpeedAndSpeedOnly.INSTANCE.setSpeed(300);
            startSimulation();
        });
        fast.setOnAction(actionEvent -> {
            SpeedAndSpeedOnly.INSTANCE.setSpeed(100);
            startSimulation();
        });
        veryFast.setOnAction(actionEvent -> {
            SpeedAndSpeedOnly.INSTANCE.setSpeed(50);
            startSimulation();
        });
        tooFast.setOnAction(actionEvent -> {
            SpeedAndSpeedOnly.INSTANCE.setSpeed(35);
            startSimulation();
        });
        ludicrousSpeed.setOnAction(actionEvent -> {
            SpeedAndSpeedOnly.INSTANCE.setSpeed(1);
            startSimulation();
        });
    }
}
