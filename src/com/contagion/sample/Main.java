package com.contagion.sample;

import com.contagion.map.Map;
import com.contagion.map.TileMapGenerator;
import com.contagion.map.XMLTileReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{

        TileMapGenerator tileMapGenerator = new TileMapGenerator();
        Scene scene = new Scene(tileMapGenerator, 640, 480);
        stage.setScene(scene);
        stage.show();

        Map.getInstance();
        Map map = Map.getInstance();

//        Parent root = FXMLLoader.load(getClass().getResource("com/contagion/viewManager/views/AddClient.fxml"));
        tileMapGenerator.drawLayer(0);
        tileMapGenerator.drawLayer( 1);


//
//        Parent root = FXMLLoader.load(getClass().getResource("com/contagion/viewManager/views/AddClient.fxml"));
//        Stage stage1 = new Stage();
//        stage1.setScene(new Scene(root, 300, 400));
//        stage1.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        ScheduledExecution.getInstance().shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
