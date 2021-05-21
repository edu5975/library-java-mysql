package gui;

import categoria.Categoria;
import categoria.CategoriaDAO;
import categoria.Subcategorias;
import categoria.SubcategoriaDAO;
import conexion.MySQL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import libro.Libro;
import libro.LibroDAO;
import sample.Datos;
import sample.SearchComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ListaLibrosController implements Initializable {

    LibroDAO libroDAO = new LibroDAO(MySQL.getConnection());
    CategoriaDAO categoriaDAO = new CategoriaDAO(MySQL.getConnection());
    SubcategoriaDAO subcategoriaDAO = new SubcategoriaDAO(MySQL.getConnection());

    ObservableList<Libro> libroArrayList;

    @FXML
    GridPane grpLibros;

    @FXML
    HBox hboxBotones;

    @FXML
    Button btn, btnReinicio;

    @FXML
    TreeView<Subcategorias> viewCategorias = new TreeView<>();

    @FXML
    TextField txtLibro;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        txtLibro.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(txtLibro.getText().equals("")){
                    mostrarLibros(libroDAO.fetchAllImage());
                }
                else{
                    mostrarLibros(libroDAO.fetchBusqueda(txtLibro.getText()));
                }
            }
        });


        mostrarLibros(libroDAO.fetchAllImage());
        btnReinicio.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mostrarLibros(libroDAO.fetchAllImage());
            }
        });

        TreeItem<Subcategorias> cat = new TreeItem<>();
        viewCategorias.setRoot(cat);
        viewCategorias.setShowRoot(false);
        for(Categoria c:categoriaDAO.fetchAll()){
            System.out.println(c);
            TreeItem<Subcategorias> sub = new TreeItem<>(new Subcategorias(c.getIdCat(),-1,c.getDescripcion()));
            for(Subcategorias sc: subcategoriaDAO.fetchByCat(c.getIdCat())){
                sub.getChildren().add(new TreeItem<>(sc));
            }
            cat.getChildren().add(sub);
        }
        viewCategorias.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    Subcategorias sc = viewCategorias.getSelectionModel().getSelectedItem().getValue();
                    if(sc.getIdsubcat()==-1){
                        System.out.println(sc.getIdcat()+ "-"+sc.getIdcat());
                        mostrarLibros(libroDAO.fetchByCat(sc.getIdcat()));
                    }
                    else{
                        mostrarLibros(libroDAO.fetchBySubCat(sc.getIdcat(),sc.getIdsubcat()));
                    }
                }
            }
        });
    }

    void mostrarLibros(ObservableList<Libro> libroArrayList){
        int renglon = 5;
        grpLibros.getChildren().clear();
        int filas =  libroArrayList.size()/renglon + 1;
        for(int i = 0; i<filas;i++){
            for(int j = 0; j<renglon;j++){
                int k = i*renglon + j;
                if(k <libroArrayList.size()) {
                    VBox vBox = new VBox(5);
                    Image image = libroArrayList.get(k).getPortada().getImagen();
                    ImageView portada = new ImageView(image);
                    portada.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            Datos.setIdLibro(libroArrayList.get(k).getIdLibro());
                            Parent root = null;
                            Stage ventana = new Stage();
                            ventana.setTitle("Libreria");
                            try {
                                Datos.setLista(false); //AQUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII
                                root = FXMLLoader.load(getClass().getClassLoader().getResource("gui/Gui.fxml"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Scene scene = new Scene(root, Datos.getWidth(), Datos.getHeight());
                            ((Stage)(btn.getScene().getWindow())).hide();
                            scene.getStylesheets().add(Datos.getCss());
                            ventana.setScene(scene);
                            ventana.show();
                        }
                    });
                    vBox.getChildren().addAll(portada);
                    grpLibros.add(vBox, j, i);
                }
            }
        }
    }

    void crearBotones(){
        hboxBotones.getChildren().clear();
        int botones = libroDAO.noLibro()/20+1;
        for(int i = 0; i < botones; i++){
            Button pagina = new Button(String.valueOf(i+1));
            pagina.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //CAMBIAR DE PAGINA
                }
            });
            hboxBotones.getChildren().add(pagina);
        }
    }
}
