package idioma;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import conexion.MySQL;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class IdiomaController implements Initializable {

    IdiomaDAO idiomaDAO = new IdiomaDAO(MySQL.getConnection());
    private boolean insertMode = false,
            updateMode = false;

    @FXML
    TextField txtID, txtName;

    @FXML
    Button btnSave, btnDelete,btnNew, btnCancel;

    @FXML
    TableView<Idioma> tblIdiomas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn col1 = new TableColumn("ID");
        TableColumn col2 = new TableColumn("Descripcion");

        col1.setCellValueFactory(new PropertyValueFactory<>("idIdioma"));
        col2.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        tblIdiomas.getColumns().addAll(col1,col2);

        tblIdiomas.setItems(idiomaDAO.fetchAll());

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

        tblIdiomas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    updateMode=true;
                    insertMode=false;
                    btnNew.setDisable(true);
                    btnDelete.setDisable(false);
                    btnSave.setDisable(false);
                    Idioma idio = tblIdiomas.getSelectionModel().getSelectedItem();
                    txtID.setText(idio.getIdIdioma());
                    txtName.setText(idio.getDescripcion());
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
                    insertIdioma();
                }else if(updateMode){
                    updateIdioma();
                }
                reloadIdiomaList();
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
                    idiomaDAO.delete(txtID.getText());
                    reloadIdiomaList();
                }
                clearForm();
                btnDelete.setDisable(false);
                btnNew.setDisable(false);
            }
        });

    }

    private void insertIdioma(){
        Idioma idio = new Idioma(txtID.getText(),txtName.getText());
        if(idiomaDAO.insert(idio))
            clearForm();;
    }

    private void updateIdioma(){
        Idioma idio = new Idioma(txtID.getText(),txtName.getText());
        if(idiomaDAO.update(idio))
            clearForm();
    }

    private void reloadIdiomaList(){
        tblIdiomas.setItems(idiomaDAO.fetchAll());
    }

    private void clearForm(){
        txtID.setText("");
        txtName.setText("");
    }
}
