package sample;

import archivo.ArchivoDAO;
import conexion.MySQL;
import imagen.ImagenDAO;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.util.Date;
import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login/Login.fxml"));
        primaryStage.setTitle("ReLibros");
        Scene scene = new Scene(root, Datos.getWidth(), Datos.getHeight());
        scene.getStylesheets().add(Datos.getCss());
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert closeConfirmation = new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "¿Seguro que quiere salir?"
                );

                Optional<ButtonType> result = closeConfirmation.showAndWait();
                System.out.println(result.get()+" " + ButtonType.OK);
                if (result.get() != ButtonType.OK){
                    event.consume();
                }
            }
        });
        primaryStage.show();

    }

    public static void main(String[] args) {
        //cargarDatos();
        launch(args);
    }

    static void cargarDatos(){
        ImagenDAO imgDAO = new ImagenDAO(MySQL.getConnection());
        ArchivoDAO archivoDAO = new ArchivoDAO(MySQL.getConnection());
        String ruta;
        File archivo;
        for(int i = 1; i<=25;i++){
            //archivo = new File("src/resources/pic" + i + ".png");
            ruta = "C:/Users/eduda/Documents/Cuarto/1. Proyecto/imagenes/pic" + i + ".png";
            imgDAO.guardarImagen(1,i,ruta,"portada");
            //archivo = new File("src/resources/pdf" + i + ".pdf");
            ruta = "C:/Users/eduda/Documents/Cuarto/1. Proyecto/pdf/pdf" + i + ".pdf";
            archivoDAO.insertArchivo(1,i,ruta,"ES");
        }
    }
}
