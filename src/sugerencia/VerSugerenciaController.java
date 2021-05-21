package sugerencia;

import conexion.MySQL;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class VerSugerenciaController implements Initializable {

    @FXML
    TextField txtID, txtUsuario,txtTitulo, txtAutor;

    @FXML
    TextArea txtContenido;

    @FXML
    Button btnArchivo;

    @FXML
    TableView<Sugerencia> tblSugerencias;

    SugerenciaDAO sugerenciaDAO = new SugerenciaDAO(MySQL.getConnection());
    Sugerencia sug;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn col1 = new TableColumn("ID");
        TableColumn col2 = new TableColumn("Usuario");
        TableColumn col3 = new TableColumn("Titulo");
        TableColumn col4 = new TableColumn("Autor");

        col1.setCellValueFactory(new PropertyValueFactory<>("idSugerencia"));
        col2.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        col3.setCellValueFactory(new PropertyValueFactory<>("tituloLibro"));
        col4.setCellValueFactory(new PropertyValueFactory<>("autorLibro"));

        tblSugerencias.getColumns().addAll(col1,col2,col3,col4);

        tblSugerencias.setItems(sugerenciaDAO.fetchAll());

        tblSugerencias.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    sug = tblSugerencias.getSelectionModel().getSelectedItem();
                    txtID.setText(String.valueOf(sug.idSugerencia));
                    txtTitulo.setText(sug.getTituloLibro());
                    txtAutor.setText(sug.getAutorLibro());
                    txtUsuario.setText(sug.getIdUsuario());
                    txtContenido.setText(sug.getContenido());
                    if(sug.getPdf()!=null)
                        btnArchivo.setDisable(false);
                    else
                        btnArchivo.setDisable(true);
                }
            }
        });

        btnArchivo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                al.setTitle("Descargar el PDF");
                al.setContentText("¿Esta usted seguro?");
                Optional<ButtonType> ol = al.showAndWait();
                if(ol.get()==ButtonType.OK){
                    DirectoryChooser chooser = new DirectoryChooser();
                    chooser.setTitle("JavaFX Projects");
                    File selectedDirectory = chooser.showDialog(null);

                    String salida = (selectedDirectory.getAbsolutePath());

                    File file = new File(salida);
                    salida = salida + "\\" + sug.getIdSugerencia()+ "-"+sug.getIdUsuario()+".pdf";

                    salida = salida.replace("\\","/");
                    salida = salida.replace(" ", "_");

                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(salida);
                        out.write(sug.getPdf());
                        out.close();
                        Runtime.getRuntime().exec("cmd /c start "+ salida);

                        System.out.println("\nPDF generado");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
