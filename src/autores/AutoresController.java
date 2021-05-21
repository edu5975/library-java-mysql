package autores;

import autor.Autor;
import autor.AutorDAO;
import conexion.MySQL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import libro.Libro;
import libro.LibroDAO;
import sample.SearchComboBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AutoresController implements Initializable {
    AutoresDAO autoresDAO=new AutoresDAO(MySQL.getConnection());

    @FXML
    TableView<Autor> tblAutor2;

    @FXML
    SearchComboBox<Libro> cbxLibros = new SearchComboBox<>();

    @FXML
    SearchComboBox<Autor> cbxAutores = new SearchComboBox<>();

    @FXML
    Button btnAdd, btnDelete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LibroDAO libroDAO=new LibroDAO(MySQL.getConnection());
        AutorDAO autorDAO=new AutorDAO(MySQL.getConnection());
        cbxLibros.setItems(libroDAO.fetchAll());
        cbxLibros.setFilter((item, text) -> item.toString().contains(text));
        cbxLibros.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
        cbxLibros.setPrefWidth(250.0);

        cbxAutores.setItems(autorDAO.fetchAll());
        cbxAutores.setFilter((item, text) -> item.toString().contains(text));
        cbxAutores.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
        cbxAutores.setPrefWidth(250.0);

        TableColumn col3 = new TableColumn("idAutor");
        TableColumn col4 = new TableColumn("Nombre");
        TableColumn col6 = new TableColumn("Nacionalidad");
        col3.setCellValueFactory(new PropertyValueFactory<>("idAutor"));
        col4.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col6.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        tblAutor2.getColumns().addAll(col3,col4,col6);

        cbxLibros.valueProperty().addListener(new ChangeListener<Libro>() {
            @Override
            public void changed(ObservableValue<? extends Libro> observable, Libro oldValue, Libro newValue) {
                refreshTables();
            }
        });

        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Autor autor = cbxAutores.getValue();
                Libro libro = cbxLibros.getValue();
                Autores autores=new Autores(autor,libro);
                autoresDAO.insert(autores);
                refreshTables();
            }
        });

        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert closeConfirmation = new Alert(
                        Alert.AlertType.CONFIRMATION,
                        "¿Está seguro de quitar este autor del Libro?"
                );

                Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
                if (ButtonType.OK.equals(closeResponse.get())) {
                    Autor autor = tblAutor2.getSelectionModel().getSelectedItem();
                    Libro libro = cbxLibros.getValue();
                    autoresDAO.delete(autor.getIdAutor(),libro.getIdLibro());
                    refreshTables();
                }
            }
        });
    }
    public void refreshTables(){
        Libro libro = cbxLibros.getValue();
        cbxAutores.setItems(autoresDAO.fetchAutorNotInBook(libro.getTitulo()));
        tblAutor2.setItems(autoresDAO.fetchAutorByBook(libro.getTitulo()));
    }


}
