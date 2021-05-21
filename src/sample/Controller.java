package sample;

import archivo.ArchivoDAO;
import conexion.MySQL;
import imagen.ImagenDAO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    Button btnImagenes, btnPDF, btnIdiomas, btnLogin,
            btnCategorias, btnLibro, btnEditorial,btnNacionalidad,
            btnAutor, btnGUI, btnSugerencia, btnVistaLibro,
            btnListaLibros,btnUsuario,btnAutores, btnCategoria,
            btnFavoritos, btnVerSugerencia, btnHistorial;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnImagenes.setOnAction(handlerOPT);
        btnPDF.setOnAction(handlerOPT);
        btnIdiomas.setOnAction(handlerOPT);
        btnLogin.setOnAction(handlerOPT);
        btnCategorias.setOnAction(handlerOPT);
        btnLibro.setOnAction(handlerOPT);
        btnEditorial.setOnAction(handlerOPT);
        btnNacionalidad.setOnAction(handlerOPT);
        btnAutor.setOnAction(handlerOPT);
        btnGUI.setOnAction(handlerOPT);
        btnSugerencia.setOnAction(handlerOPT);
        btnVistaLibro.setOnAction(handlerOPT);
        btnListaLibros.setOnAction(handlerOPT);
        btnUsuario.setOnAction(handlerOPT);
        btnAutores.setOnAction(handlerOPT);
        btnCategoria.setOnAction(handlerOPT);
        btnFavoritos.setOnAction(handlerOPT);
        btnVerSugerencia.setOnAction(handlerOPT);
        btnHistorial.setOnAction(handlerOPT);

        //cargarDatos();
    }

    EventHandler<ActionEvent> handlerOPT = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            Parent root = null;
            Stage ventana = new Stage();
            ventana.setTitle("ReLibros");
            try{
                if(event.getSource()==btnImagenes){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("imagen/Imagen.fxml"));
                }
                else if(event.getSource()==btnPDF){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("archivo/Archivo.fxml"));
                }
                else if(event.getSource()==btnIdiomas){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("idioma/Idioma.fxml"));
                }
                else if(event.getSource()==btnLogin){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("login/Login.fxml"));
                }
                else if(event.getSource()==btnCategoria){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("categoria/Categoria.fxml"));
                }
                else if(event.getSource()==btnLibro){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("libro/Libro2.fxml"));
                }
                else if(event.getSource()==btnEditorial){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("editorial/Editorial.fxml"));
                }
                else if(event.getSource()==btnNacionalidad){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("nacionalidad/Nacionalidad.fxml"));
                }
                else if(event.getSource()==btnAutor){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("autor/Autor.fxml"));
                }
                else if(event.getSource()==btnSugerencia){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("sugerencia/Sugerencia.fxml"));
                }
                else if(event.getSource()==btnVerSugerencia){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("sugerencia/VerSugerencia.fxml"));
                }
                else if(event.getSource()==btnVistaLibro){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/VistaLibro.fxml"));
                }
                else if(event.getSource()==btnGUI){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/Gui.fxml"));
                }
                else if(event.getSource()==btnListaLibros){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/ListaLibros.fxml"));
                }
                else if(event.getSource()==btnUsuario){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("usuario/Usuario.fxml"));
                }
                else if(event.getSource()==btnAutores){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("autores/Autores.fxml"));
                }
                else if(event.getSource()==btnCategorias){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("categorias/Categorias.fxml"));
                }
                else if(event.getSource()==btnFavoritos){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("favorito/Favorito.fxml"));
                }
                else if(event.getSource()==btnHistorial){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("historial/Historial.fxml"));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(root, Datos.getWidth(), Datos.getHeight());
            scene.getStylesheets().add(Datos.getCss());
            ventana.setScene(scene);
            ventana.show();
        }
    };
}
