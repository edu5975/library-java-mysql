package imagen;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import conexion.MySQL;
import libro.Libro;
import libro.LibroDAO;
import sample.SearchComboBox;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ImagenController implements Initializable {

    @FXML
    TextField txtDescripcion;

    @FXML
    SearchComboBox<Libro> cbxLibro = new SearchComboBox();

    @FXML
    Button btnAgregar ,btnAtras, btnSiguiente, btnBorrarImagen;

    @FXML
    ImageView imgVista;

    String ruta;

    ImagenDAO imgDAO = new ImagenDAO(MySQL.getConnection());
    LibroDAO libroDAO = new LibroDAO(MySQL.getConnection());

    ObservableList<Imagen> lista;
    int indice = 0;
    Image imageElegida;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbxLibro.setItems(libroDAO.fetchAll());
        cbxLibro.setFilter((item, text) -> item.toString().contains(text));
        cbxLibro.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
        cbxLibro.setPrefWidth(250.0);


        btnAgregar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Buscar imagen");

                // Agregar filtros para facilitar la busqueda
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("All Images", "*.*"),
                        new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                        new FileChooser.ExtensionFilter("PNG", "*.png")
                );

                // Obtener la imagen seleccionada
                File imgFile = fileChooser.showOpenDialog(null);

                // Mostar la imagen
                if (imgFile != null) {
                    int idLibro = cbxLibro.getValue().getIdLibro();
                    int idImagen = imgDAO.noimagen(idLibro)+1;

                    imageElegida = new Image("file:" + imgFile.getAbsolutePath());
                    System.out.println(imgFile.getAbsoluteFile());
                    ruta = imgFile.getAbsolutePath();
                    String descripcion = txtDescripcion.getText();

                    imgDAO.guardarImagen(idImagen,idLibro,ruta,descripcion);

                    Imagen img = new Imagen(idImagen,idLibro,imageElegida,descripcion);
                    lista.add(img);
                    indice=lista.size()-1;
                    imgVista.setImage(lista.get(indice).getImagen());

                    ruta = "";
                }
            }
        });

        cbxLibro.valueProperty().addListener(new ChangeListener<Libro>() {
            @Override
            public void changed(ObservableValue<? extends Libro> observable, Libro oldValue, Libro newValue) {
                int idLibro = newValue.getIdLibro();
                lista = imgDAO.imagenesLibro(idLibro);
                if(lista.size()>0)
                    imgVista.setImage(lista.get(0).getImagen());
                else
                    imgVista.setImage(new Image("/resources/null.png"));
                indice = 0;
            }
        });

        btnSiguiente.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(indice < lista.size()-1){
                    indice++;
                    imgVista.setImage(lista.get(indice).getImagen());
                }
            }
        });

        btnAtras.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(indice > 0){
                    indice--;
                    imgVista.setImage(lista.get(indice).getImagen());
                }
            }
        });

        btnBorrarImagen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Imagen ima = lista.get(indice);
                imgDAO.borrarImagen(ima.idLibro,ima.idImagen);
                lista.remove(indice);

                indice--;
                if(indice<=0) {
                    indice = 0;
                    Image img = new Image("/resources/null.png");
                    imgVista.setImage(img);
                }
                else if(indice>0){
                    if(indice>lista.size())
                        indice--;
                    imgVista.setImage(lista.get(indice).getImagen());
                }

            }
        });

    }


}
