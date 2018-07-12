package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public TextField oldWarPath;
    @FXML
    public TextField newWarPath;
    @FXML
    public Button chooseOldWar;
    @FXML
    public Button chooseNewWar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO (don't really need to do anything here).
    }

    public void chooseOldWar(ActionEvent event) {
        FileChooser oldWarChooser = new FileChooser();
        oldWarChooser.setTitle("选择旧war包");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("war文件", "*.war");
        oldWarChooser.setSelectedExtensionFilter(extensionFilter);
        File file = oldWarChooser.showOpenDialog(null);
        System.out.println(file.getAbsolutePath());
    }

    public void chooseNewWar(ActionEvent event) {

    }

}
