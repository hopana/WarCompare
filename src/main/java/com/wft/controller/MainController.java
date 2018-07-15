package com.wft.controller;

import com.wft.util.DateUtils;
import com.wft.util.FileUtil;
import com.wft.util.PatchGenerator;
import com.wft.util.WarDiff;
import com.wft.vo.CompareResult;
import com.wft.vo.FileVo;
import com.wft.vo.ResultVo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import org.apache.commons.lang.StringUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
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
    public TableView<FileVo> resultTable;
    @FXML
    public HBox resultBox;
    @FXML
    public Label productionWarNameLabel;
    @FXML
    public TextField productionWarName;
    @FXML
    TableColumn<FileVo, String> fileNameCol;
    @FXML
    TableColumn<FileVo, String> filePathCol;
    @FXML
    TableColumn<FileVo, String> fileStatusCol;
    @FXML
    public Label resultLabel;

    private CompareResult result;

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
        List<String> ignoreList = Collections.emptyList();
        if (StringUtils.isNotBlank(filterList.getText())) {
            ignoreList = Arrays.asList(filterList.getText().split(","));
        }

        //new AlertBox().wait("提示", "war包对比中，请稍后....");

        WarDiff warDiff = new WarDiff(oldWarPath.getText(), newWarPath.getText(), ignoreList);
        result = warDiff.compare();
        if (!result.isSuccess()) {
            new AlertBox().display("提示", "对比失败");
            return;
        }

        List<String> deletedFileList = result.getDeletedFileList();
        List<String> modifiedFileList = result.getModifiedFileList();
        List<String> addedFileList = result.getAddedFileList();

        fileNameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        filePathCol.setCellValueFactory(new PropertyValueFactory<>("filePath"));
        fileStatusCol.setCellValueFactory(new PropertyValueFactory<>("fileStatus"));
        fileStatusCol.setStyle("-fx-alignment: CENTER;");

        resultTable.setRowFactory(tableView -> new TableRow<FileVo>() {
            @Override
            public void updateItem(FileVo item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else {
                    if ("删除".equals(item.getFileStatus())) {
                        for (int i = 0; i < getChildren().size(); i++) {
                            ((Labeled) getChildren().get(i)).setTextFill(Paint.valueOf("#d81e06"));
                        }
                    } else if ("修改".equals(item.getFileStatus())) {
                        for (int i = 0; i < getChildren().size(); i++) {
                            ((Labeled) getChildren().get(i)).setTextFill(Paint.valueOf("#0066cc"));
                        }
                    } else {
                        for (int i = 0; i < getChildren().size(); i++) {
                            ((Labeled) getChildren().get(i)).setTextFill(Paint.valueOf("#6a00d5"));
                        }
                    }
                }
            }
        });

        ObservableList<FileVo> observableList = FXCollections.observableArrayList();
        for (String deletedFile : deletedFileList) {
            observableList.add(new FileVo(deletedFile.substring(deletedFile.lastIndexOf("/") + 1), deletedFile, "删除"));
        }

        for (String deletedFile : modifiedFileList) {
            observableList.add(new FileVo(deletedFile.substring(deletedFile.lastIndexOf("/") + 1), deletedFile, "修改"));
        }

        for (String deletedFile : addedFileList) {
            observableList.add(new FileVo(deletedFile.substring(deletedFile.lastIndexOf("/") + 1), deletedFile, "新增"));
        }

        setResultInfo(deletedFileList.size(), modifiedFileList.size(), addedFileList.size());

        resultTable.setItems(observableList);
    }

    private void setResultInfo(int deleteCount, int modifyCount, int addCount) {
        resultLabel.setText("对比结果：");

        Label deleteLabel = new Label("删除");
        deleteLabel.setPrefHeight(30);
        Label deleteCountLabel = new Label("" + deleteCount);
        deleteCountLabel.setPrefHeight(30);
        deleteCountLabel.setTextFill(Paint.valueOf("#d81e06"));
        deleteCountLabel.setFont(new Font(20));

        Label modifyLabel = new Label("修改");
        modifyLabel.setPrefHeight(30);
        Label modifyCountLabel = new Label("" + modifyCount);
        modifyCountLabel.setPrefHeight(30);
        modifyCountLabel.setTextFill(Paint.valueOf("#0066cc"));
        modifyCountLabel.setFont(new Font(20));

        Label addLabel = new Label("新增");
        addLabel.setPrefHeight(30);
        Label addCountLabel = new Label("" + addCount);
        addCountLabel.setPrefHeight(30);
        addCountLabel.setTextFill(Paint.valueOf("#6a00d5"));
        addCountLabel.setFont(new Font(20));

        resultBox.getChildren().retainAll();
        resultBox.getChildren().addAll(resultLabel, deleteLabel, deleteCountLabel, modifyLabel, modifyCountLabel, addLabel, addCountLabel);
        HBox.setMargin(deleteCountLabel, new Insets(0, 0, 0, 1));
        HBox.setMargin(modifyLabel, new Insets(0, 0, 0, 7));
        HBox.setMargin(modifyCountLabel, new Insets(0, 0, 0, 1));
        HBox.setMargin(addLabel, new Insets(0, 0, 0, 7));
        HBox.setMargin(addCountLabel, new Insets(0, 0, 0, 1));
    }

    public void generate(ActionEvent actionEvent) {
        if (StringUtils.isBlank(productionWarName.getText())) {
            new AlertBox().display("提示", "请输入生产环境war包名称");
            return;
        }

        //new AlertBox().wait("提示", "补丁包生成中....");

        result.setProductionWarName(productionWarName.getText());
        PatchGenerator generator = new PatchGenerator(result, MainController.class.getResource("/templates/").getPath());
        ResultVo result = generator.generate();

        if (!result.isSuccess()) {
            new AlertBox().display("提示", "生成失败");
            return;
        }

        FileChooser fileSaver = new FileChooser();
        fileSaver.setTitle("导出补丁包");
        fileSaver.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Zip File", "*.zip"), new FileChooser.ExtensionFilter("Allfiles", "*.*"));
        fileSaver.setInitialDirectory(new File(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath()));

        fileSaver.setInitialFileName(DateUtils.getCurrentMonthDay() + "_PATCH.zip");
        File targetFile = fileSaver.showSaveDialog(null);
        if (targetFile != null) {
            try {
                FileUtil.copyFile(new File(result.getData().toString()), targetFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
