package sample;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/5/16.
 */
public class SecurityController implements Initializable{

    private String username;
    @FXML
    TextField otpassword;
    @FXML
    Label challegnm;
    @FXML
    ImageView qrcodeview;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindToTime();

    }

    public void setFieldText(String username)
    {
        this.username = username;
    }

    private void bindToTime() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        event ->{
                            String srt1 = getRandomNumber(16);
                            challegnm.setText(srt1);
                            String path = "QRcode";
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

    public static String getRandomNumber(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public void ClearButtonOnClick()
    {
        otpassword.setText("");
    }

    public void SubmitButtonOnClick() throws ParseException {

        String id = User.getInstance().getId();
        if(otpassword.getText().trim().isEmpty())
        {
            Warning.warning_W("Please Input Password");
        }
        else {
            String crpassword = GenPassword.getTOTP(challegnm.getText(), id);
            if (!crpassword.equals(otpassword.getText())) {
                Warning.warning_W("Password is not correct");
            } else {

            }
        }
    }

    public void ScanButtonOnClick()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/camera_2.fxml"));
            Parent root1 =  fxmlLoader.load();
            Camera_2Controller controller = fxmlLoader.<Camera_2Controller>getController();
            controller.setCon(this);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            stage.setTitle("Scan QR code");
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void LoadTextFrom(String password)
    {
        this.otpassword.setText(password);
    }
}
