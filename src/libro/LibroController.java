package libro;

import autor.AutorDAO;
import categoria.Subcategorias;
import autor.Autor;
import categoria.SubcategoriaDAO;
import conexion.MySQL;
import editorial.Editorial;
import editorial.EditorialDAO;
import idioma.Idioma;
import idioma.IdiomaDAO;
import imagen.ImagenDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import sample.SearchComboBox;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class LibroController implements Initializable {

    private boolean insertMode = false,
            updateMode = false;

    @FXML
    TextField txtID,txtTitulo,txtPaginas, txtArchivo;

    @FXML
    TextArea txtContenido;

    @FXML
    Button btnArchivo, btnImagen, btnSave, btnDelete,btnNew, btnCancel;

    @FXML
    ImageView imgLibro;

    @FXML
    SearchComboBox<Idioma> cbxIdioma = new SearchComboBox<>();

    @FXML
    SearchComboBox<Autor> cbxAutor = new SearchComboBox<>();

    @FXML
    SearchComboBox<Subcategorias> cbxCategoria = new SearchComboBox<>();

    @FXML
    SearchComboBox<Editorial> cbxEditorial = new SearchComboBox<>();

    @FXML
    TableView<Libro> tblLibros;

    @FXML
    DatePicker dtpPublicacion;

    Image imageElegida;
    String rutaImagen, rutaArchivo;

    LibroDAO libroDAO = new LibroDAO(MySQL.getConnection());
    IdiomaDAO idiomaDAO = new IdiomaDAO(MySQL.getConnection());
    EditorialDAO editorialDAO = new EditorialDAO(MySQL.getConnection());
    AutorDAO autorDAO = new AutorDAO(MySQL.getConnection());
    SubcategoriaDAO subcategoriaDAO = new SubcategoriaDAO(MySQL.getConnection());
    ImagenDAO imagenDAO = new ImagenDAO(MySQL.getConnection());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbxIdioma.setItems(idiomaDAO.fetchAll());
        cbxIdioma.setFilter((item, text) -> item.toString().contains(text));
        cbxIdioma.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
        cbxIdioma.setPrefWidth(250.0);

        cbxEditorial.setItems(editorialDAO.fetchAll());
        cbxEditorial.setFilter((item, text) -> item.toString().contains(text));
        cbxEditorial.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
        cbxEditorial.setPrefWidth(250.0);

        cbxAutor.setItems(autorDAO.fetchAll());
        cbxAutor.setFilter((item, text) -> item.toString().contains(text));
        cbxAutor.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
        cbxAutor.setPrefWidth(250.0);

        cbxCategoria.setItems(subcategoriaDAO.fetchAll());
        cbxCategoria.setFilter((item, text) -> item.toString().contains(text));
        cbxCategoria.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
        cbxCategoria.setPrefWidth(250.0);

        btnImagen.setOnAction(new EventHandler<ActionEvent>() {
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
                    imageElegida = new Image("file:" + imgFile.getAbsolutePath());
                    System.out.println(imgFile.getAbsoluteFile());
                    imgLibro.setImage(imageElegida);
                    rutaImagen = imgFile.getAbsolutePath();
                }
            }
        });

        btnArchivo.setOnAction(new EventHandler<ActionEvent>() {
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
                    rutaArchivo = arch.getAbsolutePath();
                    txtArchivo.setText(rutaArchivo);
                }
            }
        });

        TableColumn col1 = new TableColumn("ID");
        TableColumn col2 = new TableColumn("Titulo");
        TableColumn col3 = new TableColumn("autor");
        TableColumn col4 = new TableColumn("Editorial");
        TableColumn col5 = new TableColumn("Publicacion");
        TableColumn col6 = new TableColumn("Paginas");

        col1.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        col2.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        col3.setCellValueFactory(new PropertyValueFactory<>("autor"));
        col4.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        col5.setCellValueFactory(new PropertyValueFactory<>("fechaPublicacion"));
        col6.setCellValueFactory(new PropertyValueFactory<>("paginas"));

        tblLibros.getColumns().addAll(col1,col2,col3,col4,col5,col6);
        tblLibros.setItems(libroDAO.fetchAll());

        btnNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateMode=false;
                insertMode=true;
                btnDelete.setDisable(true);
                btnSave.setDisable(false);
                btnNew.setDisable(true);
                txtID.setText(String.valueOf(libroDAO.noLibro()));
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

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(insertMode){
                    insertLibro();
                }else if(updateMode){
                    //updateLibro();
                }
                reloadLibrosList();
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
                    libroDAO.delete(Integer.valueOf(txtID.getText()));
                    reloadLibrosList();
                }
                clearForm();
                btnDelete.setDisable(false);
                btnNew.setDisable(false);
            }
        });

    }

    private void insertLibro(){
        Libro libro = new Libro(
                Integer.parseInt(txtID.getText()),
                txtTitulo.getText(),
                Date.valueOf(dtpPublicacion.getValue()),
                cbxEditorial.getValue(),
                Integer.parseInt(txtPaginas.getText()),
                txtContenido.getText()
        );
        if(libroDAO.insert(libro)) {
            imagenDAO.guardarImagen(1,Integer.valueOf(txtID.getText()),rutaImagen,"portada");
            clearForm();
            reloadLibrosList();
        }
    }

    /*private void updateIdioma(){
        Idioma idio = new Idioma(txtID.getText(),txtName.getText());
        if(idiomaDAO.update(idio))
            clearForm();
    }*/

    private void reloadLibrosList(){
        tblLibros.setItems(libroDAO.fetchAll());
    }

    private void clearForm(){
        txtID.setText("");
    }
}