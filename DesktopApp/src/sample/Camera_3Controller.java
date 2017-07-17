package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/5/17.
 */
public class Camera_3Controller implements Initializable {

    @FXML
    private Label code;
    @FXML private Label fps;
    @FXML private ImageView imageView;
    private Usermanagecontroller maincon;
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
            code.setText(qrcode);
            maincon.LoadTextFrom(qrcode);

        });

    }
    public void setCon(Usermanagecontroller main)
    {
        this.maincon = main;
    }
    public void StopButtonClick(ActionEvent event)
    {
        ((Node)event.getSource()).getParent().getScene().getWindow().hide();
    }
}
