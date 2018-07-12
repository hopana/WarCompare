package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Main extends Application {

    public Parent createContent() {
        TableColumn firstNameCol = new TableColumn();
        firstNameCol.setText("文件名");
        firstNameCol.setCellValueFactory(new PropertyValueFactory("fileName"));
        TableColumn lastNameCol = new TableColumn();
        lastNameCol.setText("文件路径");
        lastNameCol.setCellValueFactory(new PropertyValueFactory("filePath"));
        TableColumn emailCol = new TableColumn();
        emailCol.setText("状态");
        emailCol.setMinWidth(200);
        emailCol.setCellValueFactory(new PropertyValueFactory("fileState"));
        final TableView tableView = new TableView();
        tableView.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
        return tableView;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("WAR包对比工具");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1120, 675));
        // primaryStage.setScene(new Scene(createContent()));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
