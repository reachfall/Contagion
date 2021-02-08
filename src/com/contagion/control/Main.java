package com.contagion.control;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;


import java.io.File;
import java.net.URL;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main extends Application {

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
        Stage simulation = new Stage();
        URL simulationUrl = new File("src/com/contagion/viewManager/views/Simulation.fxml").toURI().toURL();
        FXMLLoader simulationLoader = new FXMLLoader(simulationUrl);
        SplitPane splitPane = simulationLoader.load();
        simulation.setScene(new Scene(splitPane));
        simulation.setResizable(false);
        simulation.setTitle("Pandemic simulation");
        simulation.setOnCloseRequest(windowEvent -> Platform.exit());
        simulation.show();
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
