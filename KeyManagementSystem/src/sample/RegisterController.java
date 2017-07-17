package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/5/11.
 */
public class RegisterController implements Initializable {

    @FXML private TextField tfusername, tfpassword, tfpasswordc, tfmobilen, tfemail, mobileid;

    private String symkey;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void SubmitButtonClick()
    {
        if(tfusername.getText().trim().isEmpty()
                || tfpassword.getText().trim().isEmpty()
                || tfpassword.getText().trim().isEmpty()
                || tfmobilen.getText().trim().isEmpty()
                || tfemail.getText().trim().isEmpty()
                || mobileid.getText().trim().isEmpty())
        {
            Warning.warning_W("All filled can't be empty");
        }
        else if(!tfpassword.getText().equals(tfpassword.getText()))
        {
            Warning.warning_W("Password and password confirm are not same");
        }
        else {
            boolean check;
            DatabaseConnection connect = new DatabaseConnection();
            Connection conn = connect.DatabaseConnection();
            check = connect.usermanecheck(conn, tfusername.getText());
            if (check == true) {

                Warning.warning_W("Username is existed");

            } else {
                DatabaseConnection connect2 = new DatabaseConnection();
                Connection conn2 = connect2.DatabaseConnection();
                String id = connect.add_data(conn2, tfusername.getText(), tfpasswordc.getText(),
                        tfmobilen.getText(), tfemail.getText(), mobileid.getText());
                Warning.waring_M("New user add successful");
                String username = tfusername.getText();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/showinfo.fxml"));
                    Parent root1 =  fxmlLoader.load();
                    ShowinfoController controller = fxmlLoader.<ShowinfoController>getController();
                    controller.setText(username,id,symkey);
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(root1));
                    stage.setTitle("Personal Information");
                    stage.show();
                } catch(Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }

    public void LoadTextFrom(String data)
    {
        String context[] = data.split("/");
        this.symkey = context[0];
        this.mobileid.setText(context[1]);
    }

    public void ScanButtonClick(ActionEvent event)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/cameraopen.fxml"));
            Parent root1 =  fxmlLoader.load();
            CameraopenController controller = fxmlLoader.<CameraopenController>getController();
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

    public void ClearButtonClick()
    {
        tfusername.setText("");
        tfpassword.setText("");
        tfpasswordc.setText("");
        tfmobilen.setText("");
        tfemail.setText("");

    }


}
