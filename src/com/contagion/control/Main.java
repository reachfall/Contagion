package com.contagion.control;

import com.contagion.map.Map;
import com.contagion.viewManager.controllers.DetailsView;
import com.contagion.viewManager.controllers.SidePanelControl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main extends Application {
    static SidePanelControl sidePanelControl;

    //ORACLE WEBSITE TOLD ME TO DO IT; I'M JUST FOLLOWING ORDERS
    //although it was just fine with pool.shutdown()
    private void shutdownAndAwaitTermination(ScheduledThreadPoolExecutor pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(1, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(1, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        final boolean useOnlyMap = true;
/*        if (useOnlyMap) {
            Map map = Map.getInstance();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            Scene scene = new Scene(map);
            Storage.INSTANCE.findLongestPath();
            stage.setFullScreen(true);
            stage.setScene(scene);

        } else {
            URL url = new File("C:\\Users\\doot\\Desktop\\Contagion\\src\\com\\contagion\\viewManager\\views\\SidePanelControl.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            SplitPane pane = loader.load();
            SidePanelControl control = loader.<SidePanelControl>getController();
            control.createAndAddMap();
            stage.setScene(new Scene(pane));
            stage.setResizable(false);
            stage.setFullScreen(true);
        }*/
        Stage stageMap = new Stage();
        Map map = Map.getInstance();
        Scene scene = new Scene(map);
        Storage.INSTANCE.findLongestPath();
        stageMap.setScene(scene);
        stageMap.setResizable(false);
        stageMap.show();

        URL url = new File("C:\\Users\\doot\\Desktop\\Contagion\\src\\com\\contagion\\viewManager\\views\\DetailsView.fxml").toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        TabPane tabPane = loader.load();
        DetailsView detailsView = loader.<DetailsView>getController();
        stage.setScene(new Scene(tabPane, 1000, 500));
        stage.setResizable(false);
        stage.show();


    }

    @Override
    public void stop() throws Exception {
        super.stop();
        shutdownAndAwaitTermination(ScheduledExecution.getInstance());
        PhaserExecution.getInstance().forceTermination();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
