package coordenadas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ConversorCoordenadas extends Application {
    public static void main (String [] args){
        launch();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxPrueba.fxml"));
        Scene scene = new Scene(root, 960, 555);
        scene.getStylesheets().add(getClass().getResource("/styleSheet.css").toExternalForm());
        primaryStage.setTitle("Conversor de Coordenadas");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
