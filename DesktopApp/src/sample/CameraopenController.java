package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/5/16.
 */
public class CameraopenController implements Initializable{

    @FXML private Label code;
    @FXML private Label fps;
    @FXML private ImageView imageView;
    private RegisterController maincon;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        WebCamImageProvider imageProvider = new WebCamImageProvider();
        imageView.imageProperty().bind(imageProvider.imageProperty());
        fps.textProperty().bind(imageProvider.fpsProperty());
        Thread t = new Thread(imageProvider);
        t.setDaemon(true);
        t.start();

        imageProvider.setOnSucceeded(event1 ->{
            String qrcode = imageProvider.getQrCode();
            maincon.LoadTextFrom(qrcode);

        });

    }
    public void setCon(RegisterController main)
    {
        this.maincon = main;
    }
    public void StopButtonClick(ActionEvent event)
    {
        ((Node)event.getSource()).getParent().getScene().getWindow().hide();
    }
}
