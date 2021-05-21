package categoria;


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

import java.net.URL;
import java.util.ResourceBundle;

public class CategoriaController implements Initializable {
    @FXML
    TableView<Subcategorias> tblSubCategorias;

    @FXML
    ComboBox<Categoria> cmbCategoria;

    @FXML
    TextField CatNom,SubNom;

    @FXML
    Button btnAgregar,btnModificar,btnBorrar,btnAgregarSub,btnModificarSub,btnBorrarSub;

    ObservableList<Subcategorias> subcatList= FXCollections.observableArrayList();
    CategoriaDAO categoriaDAO= new CategoriaDAO(MySQL.getConnection());
    SubcategoriaDAO subcategoriaDAO =new SubcategoriaDAO(MySQL.getConnection());


    @Override
    public void initialize(URL location, ResourceBundle resources) {
       initGUI();
    }
    String Catnam,Subnam;
    int idCat;

    public void initGUI(){
        cmbCategoria.setItems(categoriaDAO.fetchAll());
        TableColumn colidsub = new TableColumn("ID SUB");
        TableColumn colidcat2 = new TableColumn("CATEGORIA");
        TableColumn coldesub = new TableColumn("Nombre");
        colidsub.setCellValueFactory(new PropertyValueFactory<>("idsubcat"));
        colidcat2.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        coldesub.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        tblSubCategorias.getColumns().addAll(coldesub,colidsub,colidcat2);
        subcatList= subcategoriaDAO.fetchAll();
        tblSubCategorias.setItems(subcatList);
        tblSubCategorias.refresh();
        cmbCategoria.valueProperty().addListener(new ChangeListener<Categoria>() {
            @Override
            public void changed(ObservableValue<? extends Categoria> observable, Categoria oldValue, Categoria newValue) {
                if(newValue!=null) {
                    filterData(newValue.getIdCat());
                    CatNom.setText(newValue.getDescripcion());
                    Catnam = newValue.getDescripcion();
                    idCat = newValue.idCat;
                }
            }
        });

        btnAgregar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name;
                name=CatNom.getText();
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                if (categoriaDAO.lookfor(name)) {
                    alert.setContentText("Categoria Existente");

                } else {
                    if(categoriaDAO.insert(name)){
                        alert.setContentText("Categoria Agregada Correctamente");
                        refreshCategories();
                    }
                    else{
                        alert.setContentText("Ocurrio un error");
                    }
                }

                alert.show();
            }
        });


        btnModificar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                if(categoriaDAO.update(Catnam,CatNom.getText())){
                    alert.setContentText("Categoria Actualizada Correctamente");
                }
                else{
                    alert.setContentText("Error al Actualizar");
                }
                alert.show();
                refreshCategories();
            }
        });

        btnBorrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                categoriaDAO.delete(CatNom.getText());
                refreshCategories();
            }
        });

        btnAgregarSub.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                if (categoriaDAO.lookfor(CatNom.getText())){
                    if(subcategoriaDAO.lookfor(SubNom.getText(),CatNom.getText())){
                        alert.setContentText(SubNom.getText()+" ya es una subcategoria de "+CatNom.getText());
                    }
                    else{
                        subcategoriaDAO.insert(SubNom.getText(),categoriaDAO.getid(CatNom.getText()));
                        refreshSubCategories();
                        alert.setContentText("Subcategoria Agregada Correctamente");
                    }

                }
                else
                {
                    alert.setContentText("Categoria Inexistente");
                }

                alert.show();

            }
        });

        btnModificarSub.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                if(subcategoriaDAO.update(Subnam,SubNom.getText())){
                    alert.setContentText("Categoria Actualizada Correctamente");
                }
                else{
                    alert.setContentText("Error al Actualizar");
                }
                alert.show();
                refreshSubCategories();
            }
        });

        btnBorrarSub.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                subcategoriaDAO.delete(SubNom.getText(),categoriaDAO.getid(CatNom.getText()));
                refreshSubCategories();

            }
        });

        tblSubCategorias.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount()==2){
                    CatNom.setText(tblSubCategorias.getSelectionModel().getSelectedItem().getCategoria());
                    SubNom.setText(tblSubCategorias.getSelectionModel().getSelectedItem().getDescripcion());
                    Subnam=SubNom.getText();

                }
            }
        });

    }


    private void filterData(int id_cat){
        ObservableList<Subcategorias> filterData= FXCollections.observableArrayList();
        for(int i=0;i<subcatList.size();i++){
            if(id_cat==subcatList.get(i).getIdcat()){
                filterData.add(subcatList.get(i));
            }
        }
        tblSubCategorias.setItems(filterData);
    }

    private void refreshCategories(){
        cmbCategoria.getSelectionModel().clearSelection();
        cmbCategoria.getItems().clear();
        cmbCategoria.setItems(categoriaDAO.fetchAll());
    }

    private void refreshSubCategories(){
        CatNom.clear();
        SubNom.clear();
        cmbCategoria.getSelectionModel().clearSelection();
        subcatList= subcategoriaDAO.fetchAll();
        tblSubCategorias.setItems(subcatList);
    }




}
