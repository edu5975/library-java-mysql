package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.Datos;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    @FXML
    Menu miCuenta, miAdmin;

    @FXML
    MenuItem miHistorial, miFavorito, miSugerir, miSesion,
            miLibro, miAutor, miCategoria, miEditorial, miIdioma, miNacionalidad,
            miUsuario, miSugerencia, miInicio, miLibroArchivo, miLibroCategoria,
            miLibroAutor, miLibroImagen;

    @FXML
    Button btn;

    @FXML
    BorderPane brpPrincipal;

    MenuItem sesion = new MenuItem("Registrarse");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Datos.getIdTipoU()==1)
            miAdmin.setVisible(true);
        if(Datos.getIdTipoU()==3){
            miHistorial.setVisible(false);
            miFavorito.setVisible(false);
            miSugerir.setVisible(false);

            sesion.setOnAction(handlerOPT);
            miCuenta.getItems().add(1,sesion);
        }

        miCuenta.setText(Datos.getNombreUsuario());

        miSesion.setOnAction(handlerCerrarSesion);

        miLibro.setOnAction(handlerOPT);
        miAutor.setOnAction(handlerOPT);
        miNacionalidad.setOnAction(handlerOPT);
        miCategoria.setOnAction(handlerOPT);
        miEditorial.setOnAction(handlerOPT);
        miIdioma.setOnAction(handlerOPT);
        miUsuario.setOnAction(handlerOPT);
        miSugerencia.setOnAction(handlerOPT);
        miInicio.setOnAction(handlerOPT);
        miSugerir.setOnAction(handlerOPT);
        miFavorito.setOnAction(handlerOPT);
        miLibroArchivo.setOnAction(handlerOPT);
        miLibroCategoria.setOnAction(handlerOPT);
        miLibroAutor.setOnAction(handlerOPT);
        miLibroImagen.setOnAction(handlerOPT);
        miHistorial.setOnAction(handlerOPT);

        if(Datos.isLista()) {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/ListaLibros.fxml"));
                brpPrincipal.setCenter(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/VistaLibro.fxml"));
                brpPrincipal.setCenter(root);
                Datos.setLista(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    EventHandler<ActionEvent> handlerCerrarSesion = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            Alert closeConfirmation = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "¿Seguro que quiere salir?"
            );

            Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
            if (ButtonType.OK.equals(closeResponse.get())) {
                Parent root = null;
                Stage ventana = new Stage();
                ventana.setOnCloseRequest(new EventHandler<WindowEvent>() {
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
                ventana.setTitle("Libreria");
                try {
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("login/Login.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ((Stage)(btn.getScene().getWindow())).hide();
                Scene scene = new Scene(root, Datos.getWidth(), Datos.getHeight());
                scene.getStylesheets().add(Datos.getCss());
                ventana.setScene(scene);
                ventana.show();
            }
        }
    };

    EventHandler<ActionEvent> handlerOPT = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            Parent root = null;
            try{
                if(event.getSource()==miIdioma){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("idioma/Idioma.fxml"));
                }
                else if(event.getSource()==miCategoria){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("categoria/Categoria.fxml"));
                }
                else if(event.getSource()==miLibro){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("libro/Libro2.fxml"));
                }
                else if(event.getSource()==miEditorial){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("editorial/Editorial.fxml"));
                }
                else if(event.getSource()==miNacionalidad){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("nacionalidad/Nacionalidad.fxml"));
                }
                else if(event.getSource()==miAutor){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("autor/Autor.fxml"));
                }
                else if(event.getSource()==miSugerir){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("sugerencia/Sugerencia.fxml"));
                }
                else if(event.getSource()==miSugerencia){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("sugerencia/VerSugerencia.fxml"));
                }
                else if(event.getSource()==miInicio){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/ListaLibros.fxml"));
                }
                else if(event.getSource()==miUsuario||event.getSource()==sesion){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("usuario/Usuario.fxml"));
                }
                else if(event.getSource()==miFavorito){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("favorito/Favorito.fxml"));
                }
                else if(event.getSource()==miHistorial){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("historial/Historial.fxml"));
                }
                else if(event.getSource()==miLibroArchivo){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("archivo/Archivo.fxml"));
                }
                else if(event.getSource()==miLibroAutor){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("autores/Autores.fxml"));
                }
                else if(event.getSource()==miLibroCategoria){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("categorias/Categorias.fxml"));
                }
                else if(event.getSource()==miLibroImagen){
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("imagen/Imagen.fxml"));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            brpPrincipal.setCenter(root);
        }
    };
}
