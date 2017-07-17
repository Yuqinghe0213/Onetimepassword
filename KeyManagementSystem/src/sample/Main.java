package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/sample.fxml"));
        primaryStage.setTitle("Key Distribution System");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> closeProgram());
    }

    public void closeProgram()
    {
        DatabaseConnection connect = new DatabaseConnection();
        Connection conn = connect.DatabaseConnection();
        connect.updateDate(conn);

    }


    public static void main(String[] args) {
        launch(args);
    }
}
