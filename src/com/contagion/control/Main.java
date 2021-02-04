package com.contagion.control;

import com.contagion.map.Map;
import com.contagion.viewManager.controllers.SidePanelControl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

public class Main extends Application {
    static SidePanelControl sidePanelControl;

    @Override
    public void start(Stage stage) throws Exception {
        Map map = Map.getInstance();
        Scene scene = new Scene(map, 400, 400);
        stage.setScene(scene);
        stage.show();


//        URL url = new File("src/com/contagion/viewManager/views/SidePanelControl.fxml").toURI().toURL();
//        FXMLLoader loader = new FXMLLoader().load(url);
//        Parent root = loader.load();
//        sidePanelControl = loader.getController();
//        sidePanelControl.createAndAddMap();
//        stage.setScene(new Scene(root));
//        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
//        Storage.INSTANCE.getSuppliers().stream().forEach(System.out::println);
        ScheduledExecution.getInstance().shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
