package com.contagion.viewManager.controllers;

import com.contagion.control.PandemicControl;
import com.contagion.control.PhaserExecution;
import com.contagion.control.SpeedAndSpeedOnly;
import com.contagion.control.Storage;
import com.contagion.map.Map;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class Simulation implements Initializable {
    @FXML
    private AnchorPane mapAnchorPane;
    @FXML
    private Label lockdownLabel;
    @FXML
    private Slider lockdownFactorSlider;
    @FXML
    private Label lockdownFactorLabel;
    @FXML
    private Slider infectionRateSlider;
    @FXML
    private Label infectionRateLabel;
    @FXML
    private Slider transmissionRateSlider;
    @FXML
    private Label transmissionRateLabel;
    @FXML
    private Slider maskTransmissionRateSlider;
    @FXML
    private Label maskTransmissionRateLabel;
    @FXML
    private Slider vaccineEfficacySlider;
    @FXML
    private Label vaccineEfficacyLabel;
    @FXML
    private Button resetCoefficients;
    @FXML
    private Button addClient;
    @FXML
    private Button addSupplier;
    @FXML
    private Button displayDetails;
    @FXML
    private Button pauseButton;
    @FXML
    private Button resumeButton;

    private Stage detailsView;

    private void initializeLockdownLabel() {
        //TODO
    }

    private void initializeLockdownFactor() {
        lockdownFactorSlider.valueProperty().addListener((observableValue, number, t1) -> {
            PandemicControl.INSTANCE.setLockdownFactor(t1.doubleValue());
            lockdownFactorLabel.setText(String.format("Lockdown factor: %.2f", t1.doubleValue()));
        });
    }

    private void initializeInfectionRate() {
        infectionRateSlider.valueProperty().addListener((observableValue, number, t1) -> {
            PandemicControl.INSTANCE.setPassiveInfectionRate(t1.doubleValue());
            infectionRateLabel.setText(String.format("Passive infection rate: %.3f", t1.doubleValue()));
        });
    }

    private void initializeTransmissionRate() {
        transmissionRateSlider.valueProperty().addListener((observableValue, number, t1) -> {
            PandemicControl.INSTANCE.setTransmissionRate(t1.doubleValue());
            transmissionRateLabel.setText(String.format("Transmission rate: %.2f", t1.doubleValue()));
        });
    }

    private void initializeMaskTransmissionRate() {
        maskTransmissionRateSlider.valueProperty().addListener((observableValue, number, t1) -> {
            PandemicControl.INSTANCE.setMaskTransmissionFactor(t1.doubleValue());
            maskTransmissionRateLabel.setText(String.format("Mask transmission rate: %.2f", t1.doubleValue()));
        });
    }

    private void initializeVaccineEfficacy() {
        vaccineEfficacySlider.valueProperty().addListener((observableValue, number, t1) -> {
            PandemicControl.INSTANCE.setVaccineEfficacy(t1.doubleValue());
            vaccineEfficacyLabel.setText(String.format("Vaccine efficacy: %.2f", t1.doubleValue()));
        });
    }


    private void initializeResetCoefficientsButton() {
        resetCoefficients.setOnAction(actionEvent -> {
            lockdownFactorSlider.setValue(PandemicControl.LOCKDOWN_FACTOR);
            infectionRateSlider.setValue(PandemicControl.PASSIVE_INFECTION_RATE_DEFAULT);
            transmissionRateSlider.setValue(PandemicControl.TRANSMISSION_RATE_DEFAULT);
            vaccineEfficacySlider.setValue(PandemicControl.VACCINE_EFFICACY_DEFAULT);
            maskTransmissionRateSlider.setValue(PandemicControl.MASK_TRANSMISSION_FACTOR);
            PandemicControl.INSTANCE.resetCoefficients();
        });
    }

    private void initializeAddClientButton() {
        addClient.setOnAction(actionEvent -> Storage.INSTANCE.createClient());
    }

    private void initializeAddSupplierButton() {
        addSupplier.setOnAction(actionEvent -> Storage.INSTANCE.createSupplier());
    }

    private void openDetailsView() {
        try {
            URL url = new File("src/com/contagion/viewManager/views/DetailsView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            detailsView = new Stage();
            detailsView.setScene(new Scene(root));
            detailsView.setTitle("Details table");
            detailsView.show();
            detailsView.setOnCloseRequest(event -> detailsView = null);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void initializeDisplayDetailsButton() {

        displayDetails.setOnAction(actionEvent -> {
            if (detailsView == null) {
                openDetailsView();
            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pauseButton.setOnAction(actionEvent -> SpeedAndSpeedOnly.INSTANCE.setTimeControl(new AtomicBoolean(true)));
        resumeButton.setOnAction(actionEvent -> SpeedAndSpeedOnly.INSTANCE.setTimeControl(new AtomicBoolean(false)));

        Map map = Map.getInstance();
        Storage.INSTANCE.findLongestPath();
        mapAnchorPane.getChildren().add(map);

        initializeLockdownLabel();
        initializeLockdownFactor();
        initializeInfectionRate();
        initializeTransmissionRate();
        initializeVaccineEfficacy();
        initializeMaskTransmissionRate();

        initializeResetCoefficientsButton();

        initializeAddClientButton();
        initializeAddSupplierButton();
        initializeDisplayDetailsButton();
    }
}
