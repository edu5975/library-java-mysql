package sugerencia;

import conexion.MySQL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import sample.Datos;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SugerenciaController implements Initializable {

    @FXML
    TextField txtTitulo, txtAutor, txtArchivo;

    @FXML
    TextArea txtContenido;

    @FXML
    CheckBox chkArchivo;

    @FXML
    Button btnArchivo, btnEnviar;

    SugerenciaDAO sugerenciaDAO = new SugerenciaDAO(MySQL.getConnection());

    String ruta;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnArchivo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Buscar archivo");

                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("PDF", "*.pdf")
                );

                File arch = fileChooser.showOpenDialog(null);

                if (arch != null) {
                    ruta = arch.getAbsolutePath();
                    txtArchivo.setText(ruta);
                }
            }
        });

        btnArchivo.setDisable(true);
        chkArchivo.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    btnArchivo.setDisable(false);
                }
                else{
                    btnArchivo.setDisable(true);
                }
            }
        });

        btnEnviar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(chkArchivo.isSelected()){
                    sugerenciaDAO.insertArchivo(
                            sugerenciaDAO.NoSugerencia(Datos.getNombreUsuario()),
                            Datos.getNombreUsuario(),
                            txtTitulo.getText(),
                            txtAutor.getText(),
                            txtContenido.getText(),
                            ruta
                    );
                }
                else{
                    sugerenciaDAO.insertArchivo(
                            sugerenciaDAO.NoSugerencia(Datos.getNombreUsuario()),
                            Datos.getNombreUsuario(),
                            txtTitulo.getText(),
                            txtAutor.getText(),
                            txtContenido.getText()
                    );
                }
                Alert al = new Alert(Alert.AlertType.INFORMATION);
                al.setTitle("Enviado");
                al.setContentText("Sugerencia enviada con exito");
                al.show();
            }
        });
    }
}
