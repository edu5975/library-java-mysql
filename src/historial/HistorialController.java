package historial;

import conexion.MySQL;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import libro.Libro;
import sample.Datos;
import sample.SearchComboBox;
import usuario.Usuario;
import usuario.UsuarioDAO;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HistorialController implements Initializable {

    @FXML
    GridPane grpLibros;

    @FXML
    SearchComboBox<Usuario> cbxUsuarios = new SearchComboBox<>();

    @FXML
    Button btn;

    HistoriaDAO libroDAO = new HistoriaDAO(MySQL.getConnection());
    ObservableList<Libro> libroArrayList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mostrarLibros(Datos.getNombreUsuario());

        if(Datos.getIdTipoU()==1){
            UsuarioDAO usuariosDAO = new UsuarioDAO(MySQL.getConnection());

            cbxUsuarios.setItems(usuariosDAO.fetchAll());
            cbxUsuarios.setFilter((item, text) -> item.toString().contains(text));
            cbxUsuarios.getSelectionModel().selectedItemProperty().addListener((p, o, n) -> System.out.println("ComboBox Item: " + n));
            cbxUsuarios.setPrefWidth(250.0);

            cbxUsuarios.valueProperty().addListener(new ChangeListener<Usuario>() {
                @Override
                public void changed(ObservableValue<? extends Usuario> observable, Usuario oldValue, Usuario newValue) {
                    mostrarLibros(newValue.getIdUsuario());
                }
            });
        }
        else{
            cbxUsuarios.setVisible(false);
        }

    }

    void mostrarLibros(String idUsuario){
        int renglon = 5;
        grpLibros.getChildren().clear();
        libroArrayList = libroDAO.fetchHistorialUsuario(idUsuario);
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
                    vBox.getChildren().addAll(portada,new Label(libroArrayList.get(k).getOtros().toString()));
                    grpLibros.add(vBox, j, i);
                }
            }
        }
    }
}
