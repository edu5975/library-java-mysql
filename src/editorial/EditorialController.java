package editorial;

import conexion.MySQL;
import idioma.Idioma;
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

public class EditorialController implements Initializable {
    EditorialDAO editorialDAO=new EditorialDAO(MySQL.getConnection());
    private boolean insertMode = false,
                    updateMode = false;

    @FXML
    TextField txtID, txtName;

    @FXML
    Button btnSave, btnDelete,btnNew, btnCancel;

    @FXML
    TableView<Editorial> tblEditorial;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn col1 = new TableColumn("ID");
        TableColumn col2 = new TableColumn("Nombre");

        col1.setCellValueFactory(new PropertyValueFactory<>("idEditorial"));
        col2.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        tblEditorial.getColumns().addAll(col1,col2);

        tblEditorial.setItems(editorialDAO.fetchAll());

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

        tblEditorial.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    updateMode=true;
                    insertMode=false;
                    btnNew.setDisable(true);
                    btnDelete.setDisable(false);
                    btnSave.setDisable(false);
                    Editorial editorial = tblEditorial.getSelectionModel().getSelectedItem();
                    txtID.setText(""+editorial.getIdEditorial());
                    txtName.setText(editorial.getNombre());
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
                txtID.setText(String.valueOf(editorialDAO.noEditorial()));
            }
        });

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(insertMode){
                    insertEditorial();
                }else if(updateMode){
                    updateEditorial();
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
                    editorialDAO.delete(txtID.getText());
                    reloadIdiomaList();
                }
                clearForm();
                btnDelete.setDisable(false);
                btnNew.setDisable(false);
            }
        });

    }

    private void insertEditorial(){
        Editorial editorial = new Editorial(Integer.parseInt(txtID.getText()),txtName.getText());
        if(editorialDAO.insert(editorial))
            clearForm();;
    }

    private void updateEditorial(){
        Editorial editorial = new Editorial(Integer.parseInt(txtID.getText()),txtName.getText());
        if(editorialDAO.update(editorial))
            clearForm();
    }

    private void reloadIdiomaList(){
        tblEditorial.setItems(editorialDAO.fetchAll());
    }

    private void clearForm(){
        txtID.setText("");
        txtName.setText("");
    }

}
