package usuario;

import conexion.MySQL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import sample.Datos;

import java.net.URL;
import java.sql.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class usuarioController implements Initializable {

    @FXML
    TextField txtNombre,txtApellido,txtEmail,txtUsername;

    @FXML
    DatePicker dateNacimiento;

    @FXML
    PasswordField Contrasena,VerifContrasena;

    @FXML
    Label lblerrores,lblsucess;

    @FXML
    CheckBox ckbAdmin;

    @FXML
    Button Crear,Borrar,Modificar;

    @FXML
    VBox vboxLista;

    @FXML
    TableView<Usuario> tblUsuarios;

    UsuarioDAO usuariodao=new UsuarioDAO(MySQL.getConnection());
    int tipousuario=2;
    Usuario UserSelected=new Usuario();
    ObservableList<Usuario> users= FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initGUI();
    }

    public void initGUI(){
        if(Datos.getIdTipoU()!=1){
            vboxLista.setVisible(false);
            ckbAdmin.setVisible(false);
            Borrar.setVisible(false);
            Modificar.setVisible(false);
        }

        lblerrores.setVisible(false);
        lblsucess.setVisible(false);
        users=usuariodao.fetchAll();

        TableColumn colidUsuario = new TableColumn("Usuario");
        TableColumn colNombre = new TableColumn("Nombre");
        TableColumn colApellido = new TableColumn("Apellido");
        TableColumn colEmail = new TableColumn("Email");
        TableColumn colNacimiento = new TableColumn("Nacimiento");
        TableColumn colTipo = new TableColumn("Tipo Usuario");
        TableColumn colPass = new TableColumn("Contrasena");
        colidUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colNacimiento.setCellValueFactory(new PropertyValueFactory<>("nacimiento"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoUsuario"));
        colPass.setCellValueFactory(new PropertyValueFactory<>("contraseña"));
      tblUsuarios.getColumns().addAll(colidUsuario,colNombre,colApellido,colEmail,colNacimiento,colTipo,colPass);
      tblUsuarios.setItems(users);

        ckbAdmin.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                  tipousuario=1;
                }
                else
                {
                    tipousuario=2;
                }
            }
        });

        Crear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lblerrores.setVisible(false);
                if(Contrasena.getText().equals(VerifContrasena.getText())){
                    Usuario user=new Usuario(txtUsername.getText(),txtNombre.getText(),txtApellido.getText(),txtEmail.getText(), Date.valueOf(dateNacimiento.getValue()),Contrasena.getText(),tipousuario);
                   if(usuariodao.Exist(txtUsername.getText())){
                       lblerrores.setVisible(true);
                       lblerrores.setText("Usuario existente");
                   }
                   else{
                       lblerrores.setVisible(false);
                        usuariodao.insert(user);
                        Alert alert=new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Usuario creado correctamente");
                        alert.show();
                        refreshUsers();
                   }
                }
                else
                {
                    lblerrores.setVisible(true);
                    lblerrores.setText("Las contraseñas no coinciden");
                }
            }
        });

        Borrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lblerrores.setVisible(false);
            usuariodao.delete(txtUsername.getText());
            refreshUsers();
            }
        });

        Modificar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lblerrores.setVisible(false);
                lblsucess.setVisible(false);
               if(UserSelected.getIdUsuario().equals(txtUsername.getText()) && UserSelected!=null){
                   if(!UserSelected.getNombre().equals(txtNombre.getText())){
                        usuariodao.updateNombre(UserSelected.getNombre(),txtNombre.getText(),UserSelected.getIdUsuario());
                   }
                   if(!UserSelected.getApellido().equals(txtApellido.getText())){
                       usuariodao.updateApellido(UserSelected.getApellido(),txtApellido.getText(),UserSelected.getIdUsuario());
                   }
                   if(!UserSelected.getEmail().equals(txtEmail.getText())){
                       usuariodao.updateEmail(UserSelected.getEmail(),txtEmail.getText(),UserSelected.getIdUsuario());
                   }
                 /* if(!UserSelected.getNacimiento().equals(Date.valueOf(dateNacimiento.getValue()))){
                       usuariodao.updateNacimiento(UserSelected.getNacimiento(),Date.valueOf(dateNacimiento.getValue()),UserSelected.getIdUsuario());
                   }*/

                   if(UserSelected.getTipoUsuario()!=tipousuario){
                       usuariodao.updatetipoUsuario(UserSelected.getTipoUsuario(),tipousuario,UserSelected.getIdUsuario());
                   }

                   if(!Contrasena.getText().equals("") && Contrasena.getText().equals(VerifContrasena.getText())){
                       TextInputDialog dialog = new TextInputDialog("Pass");
                       dialog.setTitle("Modificar");
                       dialog.setHeaderText("Cambio de Contraseña");
                       dialog.setContentText("Ingresa la contraseña actual:");

                       Optional<String> result = dialog.showAndWait();
                       if (result.isPresent() && usuariodao.validUser(txtUsername.getText(),result.get())){
                           lblsucess.setVisible(true);
                           usuariodao.updateContrasena(result.get(),Contrasena.getText(),UserSelected.getIdUsuario());
                           lblsucess.setText("Cambios realizados correctamente");

                       }
                       else{
                           lblerrores.setText("Contraseña no valida");
                           lblerrores.setVisible(true);
                       }
                   }
                   else if(!Contrasena.getText().equals(VerifContrasena.getText())){
                       lblerrores.setVisible(true);
                       lblerrores.setText("Las contraseñas no coinciden");
                   }

               }else{
                   lblerrores.setVisible(true);
                   lblerrores.setText("No se puede cambiar el nombre de Usuario");
               }
                refreshUsers();
               Contrasena.clear();
               VerifContrasena.clear();
            }
        });



        tblUsuarios.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    txtUsername.setText(tblUsuarios.getSelectionModel().getSelectedItem().getIdUsuario());
                    txtNombre.setText(tblUsuarios.getSelectionModel().getSelectedItem().getNombre());
                    txtApellido.setText(tblUsuarios.getSelectionModel().getSelectedItem().getApellido());
                    txtEmail.setText(tblUsuarios.getSelectionModel().getSelectedItem().getEmail());
                    dateNacimiento.setValue(tblUsuarios.getSelectionModel().getSelectedItem().getNacimiento().toLocalDate());
                    UserSelected.setIdUsuario(tblUsuarios.getSelectionModel().getSelectedItem().getIdUsuario());
                   UserSelected.setNombre(tblUsuarios.getSelectionModel().getSelectedItem().getNombre());
                    UserSelected.setApellido(tblUsuarios.getSelectionModel().getSelectedItem().getApellido());
                    UserSelected.setEmail(tblUsuarios.getSelectionModel().getSelectedItem().getEmail());
                    UserSelected.setNacimiento(tblUsuarios.getSelectionModel().getSelectedItem().getNacimiento());
                    UserSelected.setTipoUsuario(tblUsuarios.getSelectionModel().getSelectedItem().getTipoUsuario());
                    Contrasena.clear();
                    VerifContrasena.clear();

                }
            }
        });
    }


    public void refreshUsers(){
        users=usuariodao.fetchAll();
        tblUsuarios.setItems(users);
        tblUsuarios.refresh();

    }

}