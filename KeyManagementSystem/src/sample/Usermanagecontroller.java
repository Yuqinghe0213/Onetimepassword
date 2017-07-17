package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/5/12.
 */
public class Usermanagecontroller implements Initializable{

    @FXML
    private TextField tfusername, tfmobile, tfemail, inputpass;
    private String challegnm = null;

    public void setFieldText(String username,  String mobile, String email)
    {
        this.tfusername.setText(username);
        this.tfemail.setText(email);
        this.tfmobile.setText(mobile);
    }

    public void UpdateButtonClick()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/securityupdate.fxml"));
            Parent root1 = fxmlLoader.load();
            SecurityController controller = fxmlLoader.<SecurityController>getController();
            controller.setFieldText(tfusername.getText());
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
        tfmobile.setText(User.getInstance().getMobile());
        tfemail.setText(User.getInstance().getEmail());
    }

    public void SubmitButtonClick()
    {
        User.getInstance().setMobile(tfmobile.getText());
        User.getInstance().setEmail(tfemail.getText());

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
        String id = User.getInstance().getId();
        if(inputpass.getText().trim().isEmpty())
        {
            Warning.warning_W("Please Input Password");
        }
        else if (challegnm == null)
        {
            Warning.waring_M("Please Scan the Challenge String");

        }
        else {
            String crpassword = GenPassword.getTOTP(challegnm, id);

            if (!crpassword.equals(inputpass.getText())) {
                Warning.warning_W("Password is not correct");
            } else {
                Warning.waring_M("Passord is correct");
                challegnm = null;
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
