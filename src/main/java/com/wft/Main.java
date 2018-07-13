package com.wft;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
        primaryStage.setTitle("WAR包对比工具");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1120, 675));
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/images/logo.png")));
        // primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        //System.out.println(Main.class.getResourceAsStream("/fxml/sample.fxml"));
    }
}
