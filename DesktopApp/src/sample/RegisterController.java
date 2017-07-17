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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/5/11.
 */
public class RegisterController implements Initializable {

    @FXML private TextField tfusername, tfpassword, tfpasswordc, tfmobilen, tfemail, mobileid;

    public static OutputStream out;
    public static InputStream in;
    public static BufferedWriter writer;
    public static BufferedReader reader;
    private SSLSocket sslSocket;
    private String data;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setSocket (SSLSocket socket)
    {
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

    public void SubmitButtonClick()
    {
        if(tfusername.getText().trim().isEmpty()
                || tfpassword.getText().trim().isEmpty()
                || tfpasswordc.getText().trim().isEmpty()
                || tfmobilen.getText().trim().isEmpty()
                || tfemail.getText().trim().isEmpty()
                || mobileid.getText().trim().isEmpty())
        {
            Warning.warning_W("All filled can't be empty");
        }
        else if(!tfpassword.getText().equals(tfpasswordc.getText()))
        {
            Warning.warning_W("Password and password confirm are not same");
        }
        else {
            JSONObject register = JsonFormat.getInstance()
                    .register(tfusername.getText(),tfpassword.getText()
                            ,tfmobilen.getText(), tfemail.getText(), data);
            JSONParser parser = new JSONParser();
            try {
                writer.write(register.toJSONString()+"\n");
                System.out.println(register.toJSONString());
                writer.flush();
                String msg;
                while(true){
                    if((msg = reader.readLine()) != null){
                        JSONObject new_message;
                        new_message = (JSONObject) parser.parse(msg);
                        String type = (String) new_message.get("type");
                        if(type.equals("registeresult")) {
                            Boolean result = (Boolean) new_message.get("result");
                            if (!result) {
                                Warning.waring_M("Username is existed");

                            }
                            else{
                                String cryption = (String) new_message.get("id");
                                Warning.waring_M("New user add successful");
                                try {
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/showinfo.fxml"));
                                    Parent root1 =  fxmlLoader.load();
                                    ShowinfoController controller = fxmlLoader.<ShowinfoController>getController();
                                    controller.setText(tfusername.getText(),cryption);
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

    public void LoadTextFrom(String data)
    {
        String context[] = data.split(" ");
        if(context.length < 2)
        {
            Warning.warning_W("Please scan from mobile again");
        }
        else{
            this.data = data;
            mobileid.setText(data);
        }
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
