package archivo;

import idioma.Idioma;
import idioma.IdiomaDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import conexion.MySQL;
import libro.Libro;
import libro.LibroDAO;
import sample.SearchComboBox;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

public class ArchivoController implements Initializable {

    @FXML
    Button btnSave, btnDelete,btnNew, btnCancel, btnBuscarArchivo;

    @FXML
    TableView<Archivo> tblArchivos;

    @FXML
    SearchComboBox<Idioma> cbxIdioma = new SearchComboBox<>();

    @FXML
    TextField txtArchivo;

    @FXML
    SearchComboBox<Libro> cbxLibro = new SearchComboBox<>();

    ArchivoDAO archivoDAO = new ArchivoDAO(MySQL.getConnection());
    LibroDAO libroDAO = new LibroDAO(MySQL.getConnection());
    IdiomaDAO idiomaDAO = new IdiomaDAO(MySQL.getConnection());
    private boolean insertMode = false,
            updateMode = false;
    String ruta;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Ruta de una carpeta
        /*DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("JavaFX Projects");
        File selectedDirectory = chooser.showDialog(null);

        System.out.println(selectedDirectory.toString());*/



        cbxIdioma.setItems(idiomaDAO.fetchAll());
        cbxIdioma.setFilter((item, text) -> item.toString().contains(text));
        cbxIdioma.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
        cbxIdioma.setPrefWidth(250.0);

        cbxLibro.setItems(libroDAO.fetchAll());
        cbxLibro.setFilter((item, text) -> item.toString().contains(text));
        cbxLibro.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
        cbxLibro.setPrefWidth(250.0);

        TableColumn col1 = new TableColumn("Id archivo");
        TableColumn col2 = new TableColumn("Id Libro");
        TableColumn col3 = new TableColumn("idioma");

        col1.setCellValueFactory(new PropertyValueFactory<>("idArchivo"));
        col2.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        col3.setCellValueFactory(new PropertyValueFactory<>("idioma"));

        tblArchivos.getColumns().addAll(col1,col2,col3);

        cbxLibro.valueProperty().addListener(new ChangeListener<Libro>() {
            @Override
            public void changed(ObservableValue<? extends Libro> observable, Libro oldValue, Libro newValue) {
                tblArchivos.setItems(archivoDAO.fetchAll(cbxLibro.getValue().getIdLibro()));
            }
        });

        btnBuscarArchivo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Buscar archivo");

                // Agregar filtros para facilitar la busqueda
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("PDF", "*.pdf")
                );

                // Obtener la imagen seleccionada
                File arch = fileChooser.showOpenDialog(null);

                // Mostar la imagen
                if (arch != null) {
                    ruta = arch.getAbsolutePath();
                    txtArchivo.setText(ruta);
                }
            }
        });

        tblArchivos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Archivo archivo = tblArchivos.getSelectionModel().getSelectedItem();
                if(event.getClickCount()==2){
                    try{
                        Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                        al.setTitle("Open PDF");
                        al.setContentText("Are you sure?");
                        Optional<ButtonType> ol = al.showAndWait();
                        if(ol.get()==ButtonType.OK){
                            String salida = "results/salida.pdf";

                            File file = new File(salida);
                            file.getParentFile().mkdirs();
                            OutputStream out = new FileOutputStream("results/salida.pdf");
                            out.write(archivo.getPdf());
                            out.close();

                            Runtime.getRuntime().exec("cmd /c start "+ salida);

                            System.out.println("\nPDF generado");
                        }
                    }
                    catch (Exception e){
                        System.out.println(e);
                    }
                }
            }
        });

        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                btnDelete.setDisable(true);
                btnSave.setDisable(true);
                btnNew.setDisable(false);
                insertMode = false;
                updateMode = false;
                clearForm();
            }
        });

        btnNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateMode=false;
                insertMode=true;
                btnDelete.setDisable(true);
                btnSave.setDisable(false);
                btnNew.setDisable(true);
            }
        });

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(insertMode){
                    insertArchivo();
                }else if(updateMode){
                    //updateIdioma();
                }
                reloadArchivosList();
                btnDelete.setDisable(true);
                btnSave.setDisable(true);
                btnNew.setDisable(false);
            }
        });

        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                al.setTitle("Delete");
                al.setContentText("Are you sure?");
                Optional<ButtonType> ol = al.showAndWait();
                if(ol.get()==ButtonType.OK){
                    Archivo archivo = tblArchivos.getSelectionModel().getSelectedItem();
                    archivoDAO.delete(archivo);
                    reloadArchivosList();
                }
                clearForm();
                btnDelete.setDisable(false);
                btnNew.setDisable(false);
            }
        });
    }

    private void insertArchivo(){
        try {
            Path pdfPath = Paths.get(ruta);
            byte[] pdf = Files.readAllBytes(pdfPath);

            int idLibro = cbxLibro.getValue().getIdLibro();
            int idArchivo = archivoDAO.noarchivo(idLibro) +1;
            Archivo archivo = new Archivo(
                    idArchivo,
                    idLibro,
                    pdf,
                    cbxIdioma.getSelectionModel().getSelectedItem()
            );
            archivoDAO.insertArchivo(archivo);
            clearForm();
        } catch (Exception ex) {
            System.out.println("Error al agregar archivo pdf "+ex.getMessage());
        }
    }

    private void reloadArchivosList(){
        tblArchivos.setItems(archivoDAO.fetchAll(cbxLibro.getValue().getIdLibro()));
    }

    private void clearForm(){
        txtArchivo.setText("");
    }
}
