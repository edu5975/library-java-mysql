package gui;

import archivo.Archivo;
import archivo.ArchivoDAO;
import autor.Autor;
import autores.AutoresDAO;
import categorias.Categorias;
import categorias.CategoriasDAO;
import conexion.MySQL;
import favorito.Favorito;
import favorito.FavoritoDAO;
import historial.HistoriaDAO;
import historial.Historial;
import imagen.ImagenDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import libro.Libro;
import libro.LibroDAO;
import sample.Datos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class VistaLibroController implements Initializable {

    Libro libro;
    LibroDAO libroDAO = new LibroDAO(MySQL.getConnection());
    ArchivoDAO archivoDAO = new ArchivoDAO(MySQL.getConnection());
    AutoresDAO autoresDAO = new AutoresDAO(MySQL.getConnection());
    CategoriasDAO categoriasDAO = new CategoriasDAO(MySQL.getConnection());
    ImagenDAO imagenDAO = new ImagenDAO(MySQL.getConnection());
    FavoritoDAO favoritoDAO = new FavoritoDAO(MySQL.getConnection());
    HistoriaDAO historiaDAO = new HistoriaDAO(MySQL.getConnection());

    @FXML
    Label lblAutor, lblTitulo, lblContenido, lblEditorial, lblPaginas, lblPublicacion, lblCategorias;

    @FXML
    ImageView imgLibro;

    @FXML
    TableView<Archivo> tblArchivos;

    @FXML
    Button btnAnterior, btnSiguiente, btnFavorito;

    int indice = 0;
    boolean librofavorito;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Datos.setLista(true);
        if(Datos.getIdTipoU()==3)
            btnFavorito.setVisible(false);
        libro = libroDAO.fetchWithImage(Datos.getIdLibro());
        libro.setAutores(autoresDAO.fetchAutorByBook(Datos.getIdLibro()));
        libro.setImagenes(imagenDAO.imagenesLibro(Datos.getIdLibro()));
        libro.setCategorias(categoriasDAO.fetchbyid(Datos.getIdLibro()));

        librofavorito = favoritoDAO.noFavot(Datos.getNombreUsuario(),libro.getIdLibro());
        if(librofavorito){
            btnFavorito.setText("Eliminar de favoritos");
        }
        else{
            btnFavorito.setText("Agregar a favoritos");
        }

        historiaDAO.insert(new Historial(
            libro.getIdLibro(),
            Datos.getNombreUsuario()
        ));

        imgLibro.setImage(libro.getPortada().getImagen());

        lblTitulo.setText(libro.getTitulo());
        lblEditorial.setText("Editorial: " + libro.getEditorial().getNombre());
        lblPublicacion.setText("Publicacion: " + libro.getFechaPublicacion());
        lblPaginas.setText("Paginas: " + libro.getPaginas());
        lblContenido.setText("Sinopsis: " + libro.getContenido() + "\n\n");

        String aux = "";
        for(Autor a:libro.getAutores()){
            aux = aux + a.getNombre() + ". ";
        }
        lblAutor.setText("Autor(es): " + aux);

        aux = "";
        for(Categorias c:libro.getCategorias()){
            aux = aux + c.getSubcategorias().getDescripcion() + ". ";
        }
        lblCategorias.setText("Categoria(s): " + aux);

        TableColumn col1 = new TableColumn("No.");
        TableColumn col3 = new TableColumn("idioma");

        col1.setCellValueFactory(new PropertyValueFactory<>("idArchivo"));
        col3.setCellValueFactory(new PropertyValueFactory<>("idioma"));

        tblArchivos.getColumns().addAll(col1,col3);

        tblArchivos.setItems(archivoDAO.fetchAll(libro.getIdLibro()));
        tblArchivos.setPrefWidth(200);
        tblArchivos.setPrefHeight(70 + 40*tblArchivos.getItems().size());

        tblArchivos.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Archivo archivo = tblArchivos.getSelectionModel().getSelectedItem();
                if(event.getClickCount()==2){
                    try{
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
                            salida = salida + "\\" + libro.getTitulo()+ "-" + archivo.getIdArchivo() + archivo.getIdioma().getIdIdioma() +".pdf";

                            salida = salida.replace("\\","/");
                            salida = salida.replace(" ", "_");

                            OutputStream out = new FileOutputStream(salida);
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

        btnSiguiente.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(indice < libro.getImagenes().size()-1){
                    indice++;
                    imgLibro.setImage(libro.getImagenes().get(indice).getImagen());
                }
            }
        });

        btnAnterior.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(indice > 0){
                    indice--;
                    imgLibro.setImage(libro.getImagenes().get(indice).getImagen());
                }
            }
        });

        btnFavorito.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //ARREGLAR FAVORITO
                if(librofavorito){
                    favoritoDAO.delete(libro.getIdLibro(),Datos.getNombreUsuario());
                    librofavorito = false;
                    btnFavorito.setText("Agregar a favoritos");
                }
                else{
                    java.util.Date fecha = new java.util.Date();
                    java.sql.Date actual = new java.sql.Date(fecha.getTime());
                    Favorito favorito = new Favorito(actual,Datos.getNombreUsuario(),libro.getIdLibro());
                    System.out.println(actual);
                    System.out.println(favoritoDAO.insert(favorito));
                    librofavorito  = true;
                    btnFavorito.setText("Eliminar de favoritos");
                }
            }
        });

    }
}
