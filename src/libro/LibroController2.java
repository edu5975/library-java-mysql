package libro;

import archivo.Archivo;
import archivo.ArchivoDAO;
import autor.Autor;
import autor.AutorDAO;
import autores.Autores;
import autores.AutoresDAO;
import categoria.Subcategorias;
import categoria.SubcategoriaDAO;
import categorias.Categorias;
import categorias.CategoriasDAO;
import conexion.MySQL;
import editorial.Editorial;
import editorial.EditorialDAO;
import favorito.FavoritoDAO;
import historial.HistoriaDAO;
import idioma.Idioma;
import idioma.IdiomaDAO;
import imagen.Imagen;
import imagen.ImagenDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import sample.SearchComboBox;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class LibroController2 implements Initializable {

    private boolean insertMode = false,
            updateMode = false;

    @FXML
    TextField txtID, txtTitulo, txtPaginas, txtArchivo;

    @FXML
    TextArea txtContenido;

    @FXML
    Button btnSave, btnDelete,btnNew, btnCancel, btnAgregarImg, btnAnteriorImg, btnSiguienteImg,
            btnAgregarAutor, btnAgregarCategoria, btnAgregarArchivo, btnEliminarImg;

    @FXML
    ImageView imgLibro;

    @FXML
    DatePicker dptPublicacion;

    @FXML
    SearchComboBox<Idioma> cbxIdioma = new SearchComboBox<>();

    @FXML
    SearchComboBox<Autor> cbxAutores = new SearchComboBox<>();

    @FXML
    SearchComboBox<Subcategorias> cbxCategoria = new SearchComboBox<>();

    @FXML
    SearchComboBox<Editorial> cbxEditorial = new SearchComboBox<>();

    @FXML
    TableView<Libro> tblLibros;

    @FXML
    TableView<Categorias> tblCategorias;

    @FXML
    TableView<Archivo> tblArchivos;

    @FXML
    TableView<Autores> tblAutores;


    LibroDAO libroDAO = new LibroDAO(MySQL.getConnection());
    IdiomaDAO idiomaDAO = new IdiomaDAO(MySQL.getConnection());
    EditorialDAO editorialDAO = new EditorialDAO(MySQL.getConnection());
    AutorDAO autorDAO = new AutorDAO(MySQL.getConnection());
    AutoresDAO autoresDAO = new AutoresDAO(MySQL.getConnection());
    SubcategoriaDAO subcategoriaDAO = new SubcategoriaDAO(MySQL.getConnection());
    ImagenDAO imagenDAO = new ImagenDAO(MySQL.getConnection());
    CategoriasDAO categoriasDAO = new CategoriasDAO(MySQL.getConnection());
    ArchivoDAO archivoDAO = new ArchivoDAO(MySQL.getConnection());
    FavoritoDAO favoritoDAO = new FavoritoDAO(MySQL.getConnection());
    HistoriaDAO historiaDAO = new HistoriaDAO(MySQL.getConnection());

    ObservableList<Imagen> listaImagenes = FXCollections.observableArrayList();

    Image imageElegida;
    String rutaImagen, rutaArchivo;
    int indice = 0;

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

        cbxAutores.setItems(autorDAO.fetchAll());
        cbxAutores.setFilter((item, text) -> item.toString().contains(text));
        cbxAutores.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
        cbxAutores.setPrefWidth(250.0);

        cbxCategoria.setItems(subcategoriaDAO.fetchAll());
        cbxCategoria.setFilter((item, text) -> item.toString().contains(text));
        cbxCategoria.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
        cbxCategoria.setPrefWidth(250.0);

        btnNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearForm();
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
                btnSave.setDisable(false);
                btnNew.setDisable(false);
                insertMode = false;
                updateMode = false;
                clearForm();
            }
        });

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                verificar();
                btnDelete.setDisable(true);
                btnSave.setDisable(false);
                btnNew.setDisable(false);
            }
        });

        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                al.setTitle("Eliminar");
                al.setContentText("¿Esta usted seguro?");
                Optional<ButtonType> ol = al.showAndWait();
                if(ol.get()==ButtonType.OK){
                    int idLibro = Integer.valueOf(txtID.getText());
                    favoritoDAO.delete(idLibro);
                    historiaDAO.delete(idLibro);
                    autoresDAO.delete(idLibro);
                    categoriasDAO.delete(idLibro);
                    archivoDAO.delete(idLibro);
                    imagenDAO.borrarImagenes(idLibro);
                    libroDAO.delete(idLibro);
                    reloadLibrosList();
                }
                clearForm();
                btnDelete.setDisable(false);
                btnNew.setDisable(false);
            }
        });

        //IMAGENNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN
        controllerImagenes();

        //CATEGORIASSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
        controllerCategorias();

        //AUTOREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEES
        controllerAutores();

        //ARCHIVOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOS
        controllerArchivos();

        //LIBROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOSSS
        controllerLibros();

    }

    private void reloadLibrosList() {
        tblLibros.setItems(libroDAO.fetchAllInfo());
    }

    private void clearForm() {
        txtPaginas.setText("");
        txtContenido.setText("");
        txtTitulo.setText("");
        txtArchivo.setText("");
        txtID.setText("ID");

        cbxEditorial.valueProperty().setValue(null);
        cbxAutores.valueProperty().setValue(null);
        cbxIdioma.valueProperty().setValue(null);
        cbxCategoria.valueProperty().setValue(null);

        tblArchivos.getItems().clear();
        tblCategorias.getItems().clear();
        tblAutores.getItems().clear();

        listaImagenes.clear();

        imgLibro.setImage( new Image("/resources/null.png"));
    }

    private void controllerLibros(){
        TableColumn col1 = new TableColumn("ID");
        TableColumn col2 = new TableColumn("Titulo");
        TableColumn col3 = new TableColumn("Autor");
        TableColumn col4 = new TableColumn("Editorial");
        TableColumn col5 = new TableColumn("Publicacion");
        TableColumn col6 = new TableColumn("Paginas");

        col1.setCellValueFactory(new PropertyValueFactory<>("idLibro"));
        col2.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        col3.setCellValueFactory(new PropertyValueFactory<>("autor"));
        col4.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        col5.setCellValueFactory(new PropertyValueFactory<>("fechaPublicacion"));
        col6.setCellValueFactory(new PropertyValueFactory<>("paginas"));

        tblLibros.getColumns().addAll(col1, col2, col3, col4, col5, col6);
        tblLibros.setItems(libroDAO.fetchAllInfo());

        tblLibros.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    Libro libro = tblLibros.getSelectionModel().getSelectedItem();
                    txtID.setText(String.valueOf(libro.getIdLibro()));
                    txtTitulo.setText(libro.getTitulo());
                    txtContenido.setText(libro.getContenido());
                    txtPaginas.setText(String.valueOf(libro.getPaginas()));
                    dptPublicacion.setValue(libro.getFechaPublicacion().toLocalDate());
                    cbxEditorial.getSelectionModel().select(libro.getEditorial());

                    listaImagenes = imagenDAO.imagenesLibro(libro.getIdLibro());
                    indice=0;
                    imgLibro.setImage(listaImagenes.get(indice).getImagen());

                    tblArchivos.setItems(archivoDAO.fetchAll(libro.getIdLibro()));

                    tblCategorias.setItems(categoriasDAO.fetchbyid(libro.getIdLibro()));

                    tblAutores.setItems(autoresDAO.fetchAutoresByBook(libro.getIdLibro()));

                    insertMode = false;
                    updateMode = true;
                    btnDelete.setDisable(false);
                }
            }
        });
    }

    private void controllerAutores(){
        btnAgregarAutor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Autores autor = new Autores(
                        cbxAutores.getValue(),
                        new Libro(
                                Integer.valueOf(txtID.getText())
                        )
                );
                tblAutores.getItems().add(autor);
            }
        });

        tblAutores.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    Autores autores = tblAutores.getSelectionModel().getSelectedItem();
                    Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                    al.setTitle("Eliminar");
                    al.setContentText("¿Esta seguro?");
                    Optional<ButtonType> ol = al.showAndWait();
                    if(ol.get()==ButtonType.OK){
                        tblAutores.getItems().remove(autores);
                    }
                }
            }
        });

        TableColumn col8 = new TableColumn("Autores");
        col8.setCellValueFactory(new PropertyValueFactory<>("autor"));
        tblAutores.getColumns().add(col8);
    }

    private void controllerCategorias(){
        btnAgregarCategoria.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Categorias categorias = new Categorias(
                        new Libro(
                                Integer.valueOf(txtID.getText())
                        ),
                        cbxCategoria.getValue()
                );
                tblCategorias.getItems().add(categorias);
            }
        });

        tblCategorias.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    Categorias categorias = tblCategorias.getSelectionModel().getSelectedItem();
                    Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                    al.setTitle("Eliminar");
                    al.setContentText("¿Esta seguro?");
                    Optional<ButtonType> ol = al.showAndWait();
                    if(ol.get()==ButtonType.OK){
                        tblCategorias.getItems().remove(categorias);
                    }
                }
            }
        });

        TableColumn col7 = new TableColumn("Categorias");
        col7.setCellValueFactory(new PropertyValueFactory<>("subcategorias"));
        tblCategorias.getColumns().add(col7);
    }

    private void controllerImagenes(){
        btnAgregarImg.setOnAction(new EventHandler<ActionEvent>() {
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
                    int idLibro = Integer.valueOf(txtID.getText());
                    int idImagen = listaImagenes.size()+1;
                    imageElegida = new Image("file:" + imgFile.getAbsolutePath());
                    System.out.println(imgFile.getAbsoluteFile());
                    imgLibro.setImage(imageElegida);
                    rutaImagen = imgFile.getAbsolutePath();
                    Imagen img = new Imagen(idImagen,idLibro,imageElegida,"imagen",rutaImagen);
                    listaImagenes.add(img);
                    indice=listaImagenes.size()-1;
                    imgLibro.setImage(listaImagenes.get(indice).getImagen());
                }
            }
        });

        btnSiguienteImg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(indice < listaImagenes.size()-1){
                    indice++;
                    imgLibro.setImage(listaImagenes.get(indice).getImagen());
                }
            }
        });

        btnAnteriorImg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(indice > 0){
                    indice--;
                    imgLibro.setImage(listaImagenes.get(indice).getImagen());
                }
            }
        });

        btnEliminarImg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                listaImagenes.remove(indice);

                indice--;
                if(indice<=0) {
                    indice = 0;
                    Image img = new Image("/resources/null.png");
                    imgLibro.setImage(img);
                }
                else if(indice>0){
                    if(indice>listaImagenes.size())
                        indice--;
                    imgLibro.setImage(listaImagenes.get(indice).getImagen());
                }

            }
        });
    }

    private void controllerArchivos(){
        txtArchivo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
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

        btnAgregarArchivo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(cbxIdioma.getValue()!=null){
                    try{
                        Path pdfPath = Paths.get(rutaArchivo);
                        byte[] pdf = Files.readAllBytes(pdfPath);

                        int idLibro = Integer.valueOf(txtID.getText());
                        int idArchivo = tblArchivos.getItems().size()+1;
                        Archivo archivo = new Archivo(
                                idArchivo,
                                idLibro,
                                pdf,
                                cbxIdioma.getSelectionModel().getSelectedItem()
                        );
                        tblArchivos.getItems().add(archivo);
                    }
                    catch (Exception e){
                        System.out.println(e.toString());
                    }
                }
                else{
                    Alert al = new Alert(Alert.AlertType.ERROR);
                    al.setTitle("Error");
                    al.setContentText("Seleccione el idioma");
                    al.show();
                }
            }

        });

        tblArchivos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    Archivo archivo = tblArchivos.getSelectionModel().getSelectedItem();
                    Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                    al.setTitle("Eliminar");
                    al.setContentText("¿Esta seguro?");
                    Optional<ButtonType> ol = al.showAndWait();
                    if(ol.get()==ButtonType.OK){
                        tblArchivos.getItems().remove(archivo);
                    }
                }
            }
        });

        TableColumn col8 = new TableColumn("ID");
        TableColumn col9 = new TableColumn("Idioma");
        col8.setCellValueFactory(new PropertyValueFactory<>("idArchivo"));
        col9.setCellValueFactory(new PropertyValueFactory<>("idioma"));
        tblArchivos.getColumns().addAll(col8,col9);
    }

    private void verificar(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        if(!txtID.getText().equalsIgnoreCase("ID")){
            if(!txtTitulo.getText().equals("")){
                if(tblAutores.getItems().size()!=0){
                    if(tblCategorias.getItems().size()!=0){
                        if(tblArchivos.getItems().size()!=0){
                            if(cbxEditorial.getValue()!=null){
                                if(listaImagenes.size()!=0){
                                    if(!txtContenido.getText().equals("")){
                                        if(!txtPaginas.getText().equals("")){
                                            if(dptPublicacion.getValue()!=null){
                                                if(insertMode){
                                                    insertLibro();
                                                }else if(updateMode){
                                                    updateLibro();
                                                }
                                                reloadLibrosList();
                                            }
                                            else{
                                                alert.setContentText("Seleccione la fecha de publicacion");
                                                alert.show();
                                            }
                                        }
                                        else{
                                            alert.setContentText("Ingrese el numero de imagenes");
                                            alert.show();
                                        }
                                    }
                                    else{
                                        alert.setContentText("Ingrese una sinopsis");
                                        alert.show();
                                    }
                                }
                                else{
                                    alert.setContentText("Agregue por lo menos una imagen");
                                    alert.show();
                                }
                            }
                            else{
                                alert.setContentText("Seleccione la categoria");
                                alert.show();
                            }
                        }
                        else{
                            alert.setContentText("Agregue por lo menos un archivo");
                            alert.show();
                        }
                    }
                    else{
                        alert.setContentText("Ingrese por lo menos una categoria");
                        alert.show();
                    }
                }
                else{
                    alert.setContentText("Ingrese por lo menos un autor");
                    alert.show();
                }
            }
            else{
                alert.setContentText("Ingrese el titulo");
                alert.show();
            }
        }
        else{
            alert.setContentText("ID no valido");
            alert.show();
        }
    }

    private void insertLibro(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exito");
        alert.setContentText("Libro agregado correctamente");
        Libro libro = new Libro(
                Integer.valueOf(txtID.getText()),
                txtTitulo.getText(),
                Date.valueOf(dptPublicacion.getValue()),
                cbxEditorial.getValue(),
                Integer.parseInt(txtPaginas.getText()),
                txtContenido.getText()
        );
        if(libroDAO.insert(libro)){
            for(Autores a: tblAutores.getItems()){
                autoresDAO.insert(a);
            }
            for(Categorias c: tblCategorias.getItems()){
                categoriasDAO.insert(
                        c.getLibro().getIdLibro(),
                        c.getSubcategorias().getIdsubcat(),
                        c.getSubcategorias().getIdcat()
                );
            }
            for(Imagen i:listaImagenes){
                imagenDAO.guardarImagen(
                        i.getIdImagen(),
                        i.getIdLibro(),
                        i.getRuta(),
                        i.getDescripcion()
                );
            }
            for(Archivo a:tblArchivos.getItems()){
                archivoDAO.insertArchivo(a);
            }
        }
        alert.show();
    }

    private void updateLibro() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Libro libro = new Libro(
                Integer.valueOf(txtID.getText()),
                txtTitulo.getText(),
                Date.valueOf(dptPublicacion.getValue()),
                cbxEditorial.getValue(),
                Integer.parseInt(txtPaginas.getText()),
                txtContenido.getText()
        );
        if(libroDAO.update(libro)){
            alert.setTitle("Exito");
            alert.setContentText("Libro actualizado correctamente");
            //ACOMODAR UPDATE
            autoresDAO.delete(libro.getIdLibro());
            for(Autores a: tblAutores.getItems()){
                autoresDAO.insert(a);
            }
            categoriasDAO.delete(libro.getIdLibro());
            for(Categorias c: tblCategorias.getItems()){
                categoriasDAO.insert(
                        c.getLibro().getIdLibro(),
                        c.getSubcategorias().getIdsubcat(),
                        c.getSubcategorias().getIdcat()
                );
            }
            archivoDAO.delete(libro.getIdLibro());
            for(Archivo a:tblArchivos.getItems()){
                archivoDAO.insertArchivo(a);
            }

            for(Imagen i:listaImagenes){
                imagenDAO.guardarImagen(
                        i.getIdImagen(),
                        i.getIdLibro(),
                        i.getRuta(),
                        i.getDescripcion()
                );
            }
            alert.show();
        }
        alert.show();
    }
}













