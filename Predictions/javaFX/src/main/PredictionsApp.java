package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import resources.Resources;


import java.net.URL;

public class PredictionsApp  extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(Resources.APP_FXML_RESOURCE);
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());

        AppController appController = fxmlLoader.getController();
        appController.setPrimaryStage(primaryStage);
        appController.setMainComponent((ScrollPane) root);

        Scene scene = new Scene(root, 700, 650);

        scene.getStylesheets().add(getClass().getResource("/main/App.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("Predictions Application");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
