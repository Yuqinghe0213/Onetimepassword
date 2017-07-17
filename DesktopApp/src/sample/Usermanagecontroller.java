package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/5/12.
 * This class is used to control the action in user management window
 */
public class Usermanagecontroller implements Initializable{

    @FXML
    private TextField tfusername, tfmobile, tfemail, inputpass;
    private String challegnm = null;

    private SSLSocket sslSocket;
    private String mobile;
    private String email;
    public static OutputStream out;
    public static InputStream in;
    public static BufferedWriter writer;
    public static BufferedReader reader;


    public void setFieldText (String username,  String mobile, String email,
                             SSLSocket socket)
    {
        this.mobile = mobile;
        this.email = email;
        this.tfusername.setText(username);
        this.tfemail.setText(email);
        this.tfmobile.setText(mobile);
        this.sslSocket = socket;
        try {
            out = sslSocket.getOutputStream();
            in = sslSocket.getInputStream();

            writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void UpdateButtonClick()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/securityupdate.fxml"));
            Parent root1 = fxmlLoader.load();
            SecurityController controller = fxmlLoader.<SecurityController>getController();
            controller.setFieldText(tfusername.getText(), sslSocket);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            stage.setTitle("Account Management");
            stage.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void ClearButtonClick()
    {
        tfmobile.setText(mobile);
        tfemail.setText(email);
    }

    public void SubmitButtonClick()
    {
        JSONObject changeinfo = JsonFormat.getInstance()
                .changeemail(tfusername.getText(),tfmobile.getText(), tfemail.getText());
        JSONParser parser = new JSONParser();

        try {
            writer.write(changeinfo.toJSONString()+ "\n");
            writer.flush();
            String msg;
            while(true){
                if((msg = reader.readLine()) != null){
                    JSONObject new_message;
                    new_message = (JSONObject) parser.parse(msg);
                    String type = (String) new_message.get("type");
                    if(type.equals("changeinfo")) {
                        Boolean result = (Boolean) new_message.get("result");
                        if (result) {
                            Warning.waring_M("Information change success");

                        }
                        else{
                            Warning.warning_W("Information update failed");
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

    public void ScanButtonOnClick()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/camera_3.fxml"));
            Parent root1 =  fxmlLoader.load();
            Camera_3Controller controller = fxmlLoader.<Camera_3Controller>getController();
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
        this.inputpass.setText(password);
    }

    public void SubmitButtonOnClick()throws ParseException
    {
        if(inputpass.getText().trim().isEmpty())
        {
            Warning.warning_W("Please Input Password");
        }
        else if (challegnm == null)
        {
            Warning.waring_M("Please Scan the Challenge String");

        }
        else {
            JSONObject checkpass = JsonFormat.getInstance()
                    .checkpassword(tfusername.getText(),challegnm, inputpass.getText());
            System.out.println(checkpass.toJSONString());
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

                            }
                            else{
                                Warning.warning_W("Password is not correct");
                                challegnm = null;
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

    public void ChallegButtonOnClick()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().
                    getResource("../view/showQrcode.fxml"));
            Parent root1 =  fxmlLoader.load();
            ShowQRcodeController controller = fxmlLoader.<ShowQRcodeController>getController();
            controller.setCon(this,tfusername.getText());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            stage.setTitle("Account Management");
            stage.show();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void ClockButtonclick()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog with Custom Actions");
        alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeConfirm = new ButtonType("Comfirm");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeConfirm,buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeConfirm){
            try {
                JSONParser parser = new JSONParser();
                JSONObject clock = JsonFormat.getInstance().
                        clockAccount(tfusername.getText());
                writer.write(clock.toJSONString() + "\n");
                writer.flush();
                String msg;
                while(true){
                    if((msg = reader.readLine()) != null){
                        JSONObject new_message;
                        new_message = (JSONObject) parser.parse(msg);
                        String type = (String) new_message.get("type");
                        if(type.equals("lockaccount")) {
                            Boolean result1 = (Boolean) new_message.get("result");
                            if (result1) {
                                Warning.waring_M("Account has locked");

                            }
                            else{
                                Warning.warning_W("Fail to lock account");
                            }
                        }

                        break;
                    }
                    break;
                }
            }catch (IOException e)
            {
                e.printStackTrace();
            }catch (org.json.simple.parser.ParseException e1) {
                e1.printStackTrace();
            }

        }  else {
            alert.close();
        }

    }

    public void ClearOnclick()
    {
        inputpass.setText("");
    }

    public void setString(String str)
    {
            this.challegnm = str;

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
