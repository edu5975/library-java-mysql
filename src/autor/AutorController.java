package autor;

import conexion.MySQL;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import nacionalidad.Nacionalidad;
import nacionalidad.NacionalidadDAO;
import sample.SearchComboBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AutorController implements Initializable {
    AutorDAO autorDAO=new AutorDAO(MySQL.getConnection());
    private boolean insertMode = false,
            updateMode = false;
    @FXML
    TextField txtID, txtName;

    @FXML
    SearchComboBox<Nacionalidad> cbxNacionalidad; //CAMBIE POR UN COMBOBOX

    @FXML
    Button btnSave, btnDelete,btnNew, btnCancel;

    @FXML
    TableView<Autor> tblAutor;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn col1 = new TableColumn("ID");
        TableColumn col2 = new TableColumn("Nombre");
        TableColumn col3 = new TableColumn("Nacionalidad");

        col1.setCellValueFactory(new PropertyValueFactory<>("idAutor"));
        col2.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col3.setCellValueFactory(new PropertyValueFactory<>("Nacionalidad"));

        tblAutor.getColumns().addAll(col1,col2,col3);

        tblAutor.setItems(autorDAO.fetchAll());

        NacionalidadDAO nacionalidadDAO = new NacionalidadDAO(MySQL.getConnection());
        cbxNacionalidad.setItems(nacionalidadDAO.fetchAll());
        cbxNacionalidad.setFilter((item, text) -> item.toString().contains(text));
        cbxNacionalidad.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
        cbxNacionalidad.setPrefWidth(250.0);

        txtID.setDisable(true);

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

        tblAutor.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    updateMode=true;
                    insertMode=false;
                    btnNew.setDisable(true);
                    btnDelete.setDisable(false);
                    btnSave.setDisable(false);
                    Autor autor = tblAutor.getSelectionModel().getSelectedItem();
                    txtID.setText(""+autor.getIdAutor());
                    txtName.setText(autor.getNombre());
                    cbxNacionalidad.getSelectionModel().select(autor.nacionalidad);
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
                txtID.setText(String.valueOf(autorDAO.noAutor()));
            }
        });

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(insertMode){
                    insertAutor();
                }else if(updateMode){
                    updateAutor();
                }
                reloadAutor();
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
                    autorDAO.delete(Integer.parseInt(txtID.getText()));
                    reloadAutor();
                }
                clearForm();
                btnDelete.setDisable(false);
                btnNew.setDisable(false);
            }
        });

    }

    private void insertAutor(){             //ESTABAS JALANDO UN DATO NULO
        Autor autor = new Autor(-1,txtName.getText(),cbxNacionalidad.getValue());
        System.out.println(autor.getIdAutor());
        if(autorDAO.insertAutor(autor))
            clearForm();
    }

    private void updateAutor(){
        Autor autor = new Autor(Integer.parseInt(txtID.getText()),txtName.getText(),cbxNacionalidad.getValue());

        if(autorDAO.update(autor))
            clearForm();
    }

    private void reloadAutor(){
        tblAutor.setItems(autorDAO.fetchAll());
    }

    private void clearForm(){
        txtID.setText("");
        txtName.setText("");
    }
}
