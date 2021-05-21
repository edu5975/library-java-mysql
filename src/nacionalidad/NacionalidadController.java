package nacionalidad;

import conexion.MySQL;
import editorial.Editorial;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class NacionalidadController implements Initializable {
    NacionalidadDAO nacionalidadDAO=new NacionalidadDAO(MySQL.getConnection());

    private boolean insertMode = false,
            updateMode = false;

    @FXML
    TextField txtID, txtName;

    @FXML
    Button btnSave, btnDelete,btnNew, btnCancel;

    @FXML
    TableView<Nacionalidad> tblNacionalidad;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn col1 = new TableColumn("ID");
        TableColumn col2 = new TableColumn("Nombre");

        col1.setCellValueFactory(new PropertyValueFactory<>("idNacionalidad"));
        col2.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tblNacionalidad.getColumns().addAll(col1,col2);

        tblNacionalidad.setItems(nacionalidadDAO.fetchAll());

        btnDelete.setDisable(true);
        btnSave.setDisable(true);
        btnNew.setDisable(false);

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

        tblNacionalidad.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    updateMode=true;
                    insertMode=false;
                    btnNew.setDisable(true);
                    btnDelete.setDisable(false);
                    btnSave.setDisable(false);
                    Nacionalidad nacionalidad = tblNacionalidad.getSelectionModel().getSelectedItem();
                    txtID.setText(""+nacionalidad.getIdNacionalidad());
                    txtName.setText(nacionalidad.getDescripcion());
                }
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
                    insertNacionalidad();
                }else if(updateMode){
                    updateNacionalidad();
                }
                reloadNacionalidad();
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
                    nacionalidadDAO.delete(txtID.getText());
                    reloadNacionalidad();
                }
                clearForm();
                btnDelete.setDisable(false);
                btnNew.setDisable(false);
            }
        });

    }

    private void insertNacionalidad(){
        Nacionalidad nacionalidad = new Nacionalidad(txtID.getText(),txtName.getText());
        if(nacionalidadDAO.insert(nacionalidad))
            clearForm();;
    }

    private void updateNacionalidad(){
        Nacionalidad nacionalidad = new Nacionalidad(txtID.getText(),txtName.getText());
        if(nacionalidadDAO.update(nacionalidad))
            clearForm();
    }

    private void reloadNacionalidad(){
       tblNacionalidad.setItems(nacionalidadDAO.fetchAll());
    }

    private void clearForm(){
        txtID.setText("");
        txtName.setText("");
    }
}
