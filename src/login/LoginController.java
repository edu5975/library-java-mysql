package login;

import conexion.MySQL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.Datos;
import tipoUsuario.TipoUsuario;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    TextField txtUser;

    @FXML
    PasswordField txtPass;

    @FXML
    Button btnCan,btnAcept, btnRegistrar;

    @FXML
    ComboBox<TipoUsuario> cbxTipo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Datos.setLista(true);

        cbxTipo.getItems().add(new TipoUsuario(1,"Invitado"));
        cbxTipo.getItems().add(new TipoUsuario(2,"Usuario"));
        cbxTipo.getSelectionModel().select(1);

        cbxTipo.valueProperty().addListener(new ChangeListener<TipoUsuario>() {
            @Override
            public void changed(ObservableValue<? extends TipoUsuario> observable, TipoUsuario oldValue, TipoUsuario newValue) {
                if(newValue.getDescripcion().equals("Invitado")){
                    txtPass.setText("Invitado");
                    txtUser.setText("Invitado");
                    txtPass.setEditable(false);
                    txtUser.setEditable(false);
                }
                else{
                    txtPass.setText("");
                    txtUser.setText("");
                    txtPass.setEditable(true);
                    txtUser.setEditable(true);
                }
            }
        });

        btnAcept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                UsersDAO udao = new UsersDAO(MySQL.getConnection());
                Users user = udao.validUser(txtUser.getText(),txtPass.getText());
                if(user!=null){
                    if(user.tipoUsuario.equals("Administrador")){
                        Datos.setIdTipoU(1);
                    }
                    else if(user.tipoUsuario.equals("Usuario")){
                        Datos.setIdTipoU(2);
                    }
                    else if(user.tipoUsuario.equals("Invitado")){
                        Datos.setIdTipoU(3);
                    }
                    Datos.setNombreUsuario(txtUser.getText());
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
                        root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/Gui.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ((Stage)(btnAcept.getScene().getWindow())).hide();
                    Scene scene = new Scene(root, Datos.getWidth(), Datos.getHeight());
                    scene.getStylesheets().add(Datos.getCss());
                    ventana.setScene(scene);
                    ventana.show();
                }
                else{
                    Alert alert= new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Usuario Inconrrecto");
                    alert.show();
                }
            }
        });

        btnRegistrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Datos.setIdTipoU(3);
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
                ventana.setTitle("ReLibros");
                try {
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("usuario/Usuario.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene scene = new Scene(root, Datos.getWidth(), Datos.getHeight());
                scene.getStylesheets().add(Datos.getCss());
                ventana.setScene(scene);
                ventana.show();
            }
        });
    }


}
