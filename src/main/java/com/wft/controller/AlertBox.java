package com.wft.controller;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class AlertBox {

    public void display(String title, String message) {
        display(title, message, true);
    }

    public void display(String title, String message, boolean wait) {
        Stage stage = new Stage();
        stage.setTitle(title);
        //让底部窗口无法点击
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setWidth(300);
        stage.setHeight(150);
        stage.setResizable(false);

        Label label = new Label(message);
        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(label);
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        if (wait) {
            // 阻塞主线程
            stage.showAndWait();
        } else {
            stage.show();
        }
    }

    public void wait(String title, String message) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setWidth(300);
        stage.setHeight(150);
        stage.setResizable(false);

        Label label = new Label(message);
        ProgressIndicator progressIndicator = new ProgressIndicator(-1);
        progressIndicator.setMaxWidth(USE_PREF_SIZE);
        progressIndicator.setMaxHeight(USE_PREF_SIZE);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(label, progressIndicator);
        vBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }


}