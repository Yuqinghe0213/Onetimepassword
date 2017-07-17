package sample;

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
import org.json.simple.parser.ParseException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextField tfusername;
    @FXML
    private TextField tfpassword;

    private SSLSocket sslSocket;
    public static OutputStream out;
    public static InputStream in;
    public static BufferedWriter writer;
    public static BufferedReader reader;

    public void LoginButtonClick()
    {

        if(tfusername.getText().trim().isEmpty()
                || tfpassword.getText().trim().isEmpty())
        {
            Warning.warning_W("Username and Password can't empty");
        }
        else {

            JSONObject logininfo = JsonFormat.getInstance()
                    .userpass(tfusername.getText(),tfpassword.getText());
            JSONParser parser = new JSONParser();

            try {
                System.out.println(logininfo.toString());
                writer.write(logininfo.toJSONString()+"\n");
                writer.flush();
                String msg;
                while(true){
                    if((msg = reader.readLine()) != null){
                        JSONObject new_message;
                        new_message = (JSONObject) parser.parse(msg);
                        System.out.println(new_message.toJSONString());
                        String type = (String) new_message.get("type");
                        if(type.equals("userpass")){
                            String result = (String) new_message.get("result");
                            switch (result) {
                                case "n": {
                                    Warning.warning_W("User name is not exist");
                                    break;
                                }
                                case "w": {
                                    Warning.warning_W("Password is wrong");
                                    break;
                                }
                                case "p": {
                                    Warning.warning_W("This Account has been locked");
                                    break;
                                }
                                default: {
                                    String mobile = (String) new_message.get("mobile");
                                    String email = (String) new_message.get("email");
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().
                                            getResource("../view/usermanage.fxml"));
                                    Parent root1 =  fxmlLoader.load();
                                    Usermanagecontroller controller = fxmlLoader.<Usermanagecontroller>getController();
                                    controller.setFieldText(tfusername.getText(), mobile, email, sslSocket);
                                    Stage stage = new Stage();
                                    stage.initModality(Modality.APPLICATION_MODAL);
                                    stage.setScene(new Scene(root1));
                                    stage.setTitle("Account Management");
                                    stage.show();
                                }
                            }
                        }
                        break;
                    }
                }
            } catch (IOException e1) {

                e1.printStackTrace();
            } catch (ParseException e1) {
                e1.printStackTrace();
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
            RegisterController controller = fxmlLoader.<RegisterController>getController();
            controller.setSocket(sslSocket);
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
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

            KeyStore ks = KeyStore.getInstance("JKS");
            KeyStore tks = KeyStore.getInstance("JKS");

            ks.load(new FileInputStream("cert/kclient.ks"), "heyuqing".toCharArray());
            tks.load(new FileInputStream("cert/tclient.ks"), "heyuqing".toCharArray());

            kmf.init(ks, "heyuqing".toCharArray());
            tmf.init(tks);

            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            sslSocket = (SSLSocket) ctx.getSocketFactory().createSocket("localhost", 8000);
            out = sslSocket.getOutputStream();
            in = sslSocket.getInputStream();

            writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
