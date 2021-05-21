package categorias;

import categoria.Subcategorias;
import categoria.SubcategoriaDAO;
import conexion.MySQL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import libro.Libro;
import libro.LibroDAO;
import sample.SearchComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoriasController implements Initializable {

    @FXML
    SearchComboBox<Subcategorias> cbxCategorias = new SearchComboBox<>();

    @FXML
    SearchComboBox<Libro> cbxLibros = new SearchComboBox<>();

    @FXML
    TableView<Categorias> tblCategorias;

    @FXML
    Button btnAdd, btnDelete;


    LibroDAO libroDAO=new LibroDAO(MySQL.getConnection());
    SubcategoriaDAO subcategoriaDAO = new SubcategoriaDAO(MySQL.getConnection());
    CategoriasDAO categoriasDAO = new CategoriasDAO(MySQL.getConnection());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbxLibros.setItems(libroDAO.fetchAll());
        cbxLibros.setFilter((item, text) -> item.toString().contains(text));
        cbxLibros.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
        cbxLibros.setPrefWidth(250.0);

        cbxCategorias.setItems(subcategoriaDAO.fetchAll());
        cbxCategorias.setFilter((item, text) -> item.toString().contains(text));
        cbxCategorias.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
        cbxCategorias.setPrefWidth(250.0);

        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                categoriasDAO.insert(
                        cbxLibros.getValue().getIdLibro(),
                        cbxCategorias.getValue().getIdsubcat(),
                        cbxCategorias.getValue().getIdcat()
                );
                tblCategorias.setItems(categoriasDAO.fetchbyid(cbxLibros.getValue().getIdLibro()));
            }
        });

        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Categorias cat = tblCategorias.getSelectionModel().getSelectedItem();
                categoriasDAO.delete(
                    cat.getLibro().getIdLibro(),
                    cat.getSubcategorias().getIdsubcat(),
                    cat.getSubcategorias().getIdcat()
                );
                tblCategorias.setItems(categoriasDAO.fetchbyid(cbxLibros.getValue().getIdLibro()));
            }
        });

        cbxLibros.valueProperty().addListener(new ChangeListener<Libro>() {
            @Override
            public void changed(ObservableValue<? extends Libro> observable, Libro oldValue, Libro newValue) {
                tblCategorias.setItems(categoriasDAO.fetchbyid(newValue.getIdLibro()));
            }
        });

        TableColumn col1 = new TableColumn("Categorias");
        col1.setCellValueFactory(new PropertyValueFactory<>("subcategorias"));
        tblCategorias.getColumns().add(col1);
    }
}
