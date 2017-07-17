package sample;

import javafx.scene.control.Alert;

/**
 * Created by Administrator on 2017/5/12.
 * This class is define the warning message
 */
public class Warning {

    public static void warning_W(String msg)
    {
        Alert alertname = new Alert(Alert.AlertType.WARNING);
        alertname.setTitle("Warning");
        alertname.setHeaderText("Warning!");
        alertname.setContentText(msg);
        alertname.showAndWait();
    }

    public static void waring_M(String msg)
    {
        Alert alertregis = new Alert(Alert.AlertType.INFORMATION);
        alertregis.setTitle("Successful");
        alertregis.setHeaderText("Successful");
        alertregis.setContentText(msg);
        alertregis.showAndWait();
    }
}
