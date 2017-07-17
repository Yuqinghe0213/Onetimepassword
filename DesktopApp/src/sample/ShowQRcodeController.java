package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import javax.activity.ActivityCompletedException;
import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/5/17.
 * This class is used to show the QR code to mobile phone
 */
public class ShowQRcodeController implements Initializable {

    @FXML
    ImageView qrcodeview;

    private Usermanagecontroller controller;

    private String username, srt1;

    public static String genRandomString(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer reader = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(base.length());
            reader.append(base.charAt(num));
        }
        return reader.toString();
    }

    private void bindToTime() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        event ->{

                            GenerateQRcode.generation(srt1, username);
                            Image img = new Image( new File("QRcode/"+username + ".jpg").toURI().toString());
                            qrcodeview.setImage(img);
                        }

                ),
                new KeyFrame(Duration.seconds(120))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void setCon(Usermanagecontroller controller, String username){
        this.controller = controller;
        this.username = username;
    }

    public void ReturnButtonClick(ActionEvent event)
    {
        controller.setString(srt1);
        ((Node)event.getSource()).getParent().getScene().getWindow().hide();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindToTime();
       srt1 = genRandomString(16);

    }

}
