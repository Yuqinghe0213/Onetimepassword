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
import java.sql.Connection;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private TextField tfusername;
    @FXML
    private TextField tfpassword;

    public void LoginButtonClick()
    {

        if(tfusername.getText().trim().isEmpty()
                || tfpassword.getText().trim().isEmpty())
        {
            Warning.warning_W("Username and Password can't empty");
        }
        else {
            String result;
            DatabaseConnection connect = new DatabaseConnection();
            Connection conn = connect.DatabaseConnection();
            result = connect.password_check(conn, tfusername.getText(),
                    String.valueOf(tfpassword.getText()));
            if (result == null) {
                System.out.println("fail connection");
            } else if (result.equals("n")) {

                Warning.warning_W("User name is not exist");

            } else if (result.equals("w")) {

                Warning.warning_W("Password is wrong");
            } else if (result.equals("p")){

                Warning.warning_W("This Account has been locked");
            } else {
                DatabaseConnection connect2 = new DatabaseConnection();
                Connection conn2 = connect2.DatabaseConnection();
                connect2.getData(conn2,tfusername.getText());
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().
                            getResource("../view/usermanage.fxml"));
                    Parent root1 =  fxmlLoader.load();
                    Usermanagecontroller controller = fxmlLoader.<Usermanagecontroller>getController();
                    controller.setFieldText(User.getInstance().getUsername(),
                            User.getInstance().getMobile(),
                            User.getInstance().getEmail());
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setScene(new Scene(root1));
                    stage.setTitle("Account Management");
                    stage.show();
                } catch(Exception e) {
                    e.printStackTrace();
                }


            }
        }

    }
    public void ClearButtonClick()
    {
        tfusername.setText("");
        tfpassword.setText("");

    }

    public void RegisterButtonClick()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/register.fxml"));
            Parent root1 =  fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root1));
            stage.setTitle("New User");
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
