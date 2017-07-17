package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/5/31.
 * This class is used to display the new seed for the new mobile phone
 */
public class Showinfo2Controller implements Initializable {

    @FXML
    private Label lblId;
    @FXML
    private ImageView view;
    private String username;

    public void setText(String username, String encryption)
    {
        this.username = username;
        this.lblId.setText("Hello, " + username);
        String content = encryption;
        GenerateQRcode.generation(content,username);
        Image img = new Image( new File("QRcode/"+username + ".jpg").toURI().toString());
        view.setImage(img);
        System.out.println("operating here "+ username + "   "+ encryption);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void OKOnclick(ActionEvent event) {

        ((Node)event.getSource()).getParent().getScene().getWindow().hide();

    }
}
