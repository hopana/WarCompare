package com.wft.controller;

import com.wft.util.WarDiff;
import com.wft.vo.CompareResult;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    public TextField oldWarPath;
    @FXML
    public TextField newWarPath;
    @FXML
    public Button chooseOldWar;
    @FXML
    public Button chooseNewWar;
    @FXML
    public Label filterLabel;
    @FXML
    public TextField filterList;
    @FXML
    public Button compareButton;
    @FXML
    public Button generateButton;
    @FXML
    public TableView compareResult;
    @FXML
    public Label compareResultLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void chooseOldWar(ActionEvent actionEvent) {
        FileChooser oldWarChooser = new FileChooser();
        oldWarChooser.setTitle("选择旧war包");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("war文件", "*.war");
        oldWarChooser.setSelectedExtensionFilter(extensionFilter);
        File file = oldWarChooser.showOpenDialog(null);
        if (file != null) {
            oldWarPath.setText(file.getAbsolutePath());
        }
    }

    public void chooseNewWar(ActionEvent actionEvent) {
        FileChooser oldWarChooser = new FileChooser();
        oldWarChooser.setTitle("选择新war包");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("war文件", "*.war");
        oldWarChooser.setSelectedExtensionFilter(extensionFilter);
        File file = oldWarChooser.showOpenDialog(null);
        if (file != null) {
            newWarPath.setText(file.getAbsolutePath());
        }
    }


    public void compare(ActionEvent actionEvent) throws IOException {
        System.out.println("compare");

        WarDiff warDiff = new WarDiff(oldWarPath.getText(), newWarPath.getText());
        CompareResult result = warDiff.compare();
        if (!result.isSuccess()) {
            new AlertBox().display("提示", "对比失败");
        }

        List<String> deletedFileList = result.getDeletedFileList();
        List<String> modifiedFileList = result.getModifiedFileList();
        List<String> addedFileList = result.getAddedFileList();

        ObservableList<TableColumn> observableList = compareResult.getColumns();

        observableList.get(0).setCellValueFactory(new PropertyValueFactory("fileName"));
        observableList.get(1).setCellValueFactory(new PropertyValueFactory("filePath"));
        observableList.get(2).setCellValueFactory(new PropertyValueFactory("fileStatus"));

        for (String deletedFile : deletedFileList) {
            System.out.println(deletedFile);
            //observableList.add(new String[]{deletedFile.substring(deletedFile.lastIndexOf("/") + 1), "deletedFile", "已删除"});
            observableList.add(new TableColumn("1111"));
        }

        compareResult.setItems(observableList);
    }

    public void generate(ActionEvent actionEvent) {

    }
}
