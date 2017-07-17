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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/5/16.
 * This class is used to controll the action happen in the security window.
 */
public class SecurityController implements Initializable{

    private String username;
    @FXML
    TextField otpassword;
    @FXML
    Label challegnm;
    @FXML
    ImageView qrcodeview;

    private SSLSocket sslSocket;
    public static OutputStream out;
    public static InputStream in;
    public static BufferedWriter writer;
    public static BufferedReader reader;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindToTime();

    }

    public void setFieldText(String username, SSLSocket sslSocket)
    {
        this.username = username;
        this.sslSocket = sslSocket;
        try {
            out = sslSocket.getOutputStream();
            in = sslSocket.getInputStream();

            writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
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

    public void SubmitButtonOnClick(ActionEvent event) throws ParseException {

        if(otpassword.getText().trim().isEmpty())
        {
            Warning.warning_W("Please Input Password");
        }
        else if (challegnm == null)
        {
            Warning.waring_M("Please Scan the Challenge String");

        }
        else {
            JSONObject checkpass = JsonFormat.getInstance()
                    .checkpassword(username,challegnm.getText(), otpassword.getText());
            JSONParser parser = new JSONParser();
            try {
                writer.write(checkpass.toJSONString()+ "\n");
                writer.flush();
                String msg;
                while(true){
                    if((msg = reader.readLine()) != null){
                        JSONObject new_message;
                        new_message = (JSONObject) parser.parse(msg);
                        String type = (String) new_message.get("type");
                        if(type.equals("checkpass")) {
                            Boolean result = (Boolean) new_message.get("result");
                            if (result) {
                                Warning.waring_M("Passord is correct");
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/pwdchange.fxml"));
                                Parent root1 = fxmlLoader.load();
                                PwdchangeController controller = fxmlLoader.<PwdchangeController>getController();
                                controller.setFieldText(username, sslSocket);
                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setScene(new Scene(root1));
                                stage.setTitle("Account Management");
                                stage.show();
                                ((Node)event.getSource()).getParent().getScene().getWindow().hide();

                            }
                            else{
                                Warning.warning_W("Password is not correct");
                            }
                        }

                        break;
                    }
                    break;
                }
            } catch (IOException e1) {

                e1.printStackTrace();
            } catch (org.json.simple.parser.ParseException e1) {
                e1.printStackTrace();
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
