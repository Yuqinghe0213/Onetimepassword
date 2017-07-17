package sample;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/5/12.
 */
public class ShowinfoController implements Initializable {

    @FXML
    private Label lblId;
    @FXML
    private ImageView view;
    private String username;

    public void setText(String username, String id, String symkey)
    {
        this.username = username;
        this.lblId.setText("Hello, " + username);
        System.out.println(id);
        String cryption = AESUtil.encrypt(id,symkey);
        String content = cryption;
        String path = "QRcode";
        GenerateQRcode.generation(content,username);
        Image img = new Image( new File("QRcode/"+username + ".jpg").toURI().toString());
        view.setImage(img);

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void OKOnclick(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/usermanage.fxml"));
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
        ((Node)event.getSource()).getParent().getScene().getWindow().hide();

    }

}
