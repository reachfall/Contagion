package com.contagion.control;

import com.contagion.map.Map;
import com.contagion.pathfinding.Node;
import com.contagion.tiles.DrawableType;
import com.sun.source.tree.Tree;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {



    @Override
    public void start(Stage stage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("com/contagion/viewManager/views/AddClient.fxml"));

        Map map = Map.getInstance();
        Scene scene = new Scene(map, 400, 400);
        stage.setScene(scene);
        stage.show();

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
