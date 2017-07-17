package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/5/29.
 */
public class PwdchangeController implements Initializable{

    @FXML private TextField tfpassword, tfpasswordc, tfmobileid;

    private String username;
    private String data;
    private SSLSocket sslSocket;
    public static OutputStream out;
    public static InputStream in;
    public static BufferedWriter writer;
    public static BufferedReader reader;


    public void setFieldText(String username, SSLSocket sslsocket)
    {
        this.username = username;
        this.sslSocket = sslsocket;
        try {
            out = sslSocket.getOutputStream();
            in = sslSocket.getInputStream();
            writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void cancelButtonOnclick(ActionEvent event)
    {
        ((Node)event.getSource()).getParent().getScene().getWindow().hide();
    }

    public void OKButtonOnclick()
    {
        if (!tfpassword.getText().equals(tfpasswordc.getText()))
        {
            Warning.warning_W("Password and password confirm are not same");
        }
        else
        {
            JSONObject register = JsonFormat.getInstance()
                    .pwdchange(username,tfpassword.getText());
            JSONParser parser = new JSONParser();
            try {
                writer.write(register.toJSONString()+"\n");
                writer.flush();
                String msg;
                while(true) {
                    if ((msg = reader.readLine()) != null) {
                        JSONObject new_message;
                        new_message = (JSONObject) parser.parse(msg);
                        String type = (String) new_message.get("type");
                        if (type.equals("changepwd")) {
                            Boolean result = (Boolean) new_message.get("result");
                            if (result) {
                                Warning.waring_M("Password change successful");
                                break;

                            } else {
                                Warning.waring_M("Password change failed");
                                break;
                            }
                        }
                    }
                    break;
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    public void ScanOnclick()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/camera_4.fxml"));
            Parent root1 =  fxmlLoader.load();
            Camera_4Controller controller = fxmlLoader.<Camera_4Controller>getController();
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

    public void LoadTextFrom(String mobileid) {
        String context[] = mobileid.split(" ");
        if (context.length < 2) {
            Warning.warning_W("Please scan from mobile again");
        } else {
            tfmobileid.setText(mobileid);
            this.data = mobileid;
        }
    }

    public void UpdateOnclick()
    {
        JSONObject send = JsonFormat.getInstance().mobleidchange(username,data);
        JSONParser parser = new JSONParser();
        String msg;
        try
        {
            writer.write(send.toJSONString()+"\n");
            writer.flush();
            while(true) {
                if ((msg = reader.readLine()) != null) {
                    JSONObject new_message;
                    new_message = (JSONObject) parser.parse(msg);
                    String type = (String) new_message.get("type");
                    if (type.equals("mbidchange")) {
                        Boolean result = (Boolean) new_message.get("result");
                        if (result) {
                            String id = (String) new_message.get("id");
                            tfmobileid.setText(id);
                            try {
                                FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("../view/showinfo2.fxml"));
                                Parent root2 =  fxmlLoader2.load();
                                Showinfo2Controller controller = fxmlLoader2.<Showinfo2Controller>getController();
                                controller.setText(username,id);
                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setScene(new Scene(root2));
                                stage.setTitle("Personal Information");
                                stage.show();
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Warning.warning_W("Password change failed");
                        }
                    }
                    break;
                }
                break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
