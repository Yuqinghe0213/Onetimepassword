package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Administrator on 2017/5/20.
 * This progrem is a desktop application of one time password
 * The main function of this program is to display the data sent by server and
 * send data that is input by user to server. It is a interface of this system.
 */

public class Main extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/sample.fxml"));
        primaryStage.setTitle("Key Distribution System");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> closeProgram());
    }


    public static void main(String[] args) {


        launch(args);
    }

    public void closeProgram()
    {

    }
}
