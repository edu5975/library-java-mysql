package categoria;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class CategoriaDAO {
    Connection conn;

    public CategoriaDAO(Connection conn)
    {
        this.conn = conn;
    }

    public ObservableList<Categoria> fetchAll() {
        ObservableList<Categoria> categorias = FXCollections.observableArrayList();
        try {
            String query = "select * from Categoria order by descripcion asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Categoria p = null;
            while(rs.next()) {
                p = new Categoria(
                        rs.getInt("idCategoria"), rs.getString("descripcion")
                );
                categorias.add(p);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return categorias;
    }

    public boolean lookfor(String name){
        String resultName="test";
        try {
            String query = "select * from Categoria where descripcion= '"+name+"'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                //rs.getInt("idCategoria")
                //rs.getString("descripcion")
                resultName=rs.getString("descripcion");
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }

        if(resultName.equals(name)){
            return true;
        }
        else
        {
            return false;
        }

    }


    public Boolean insert(String categoria) {
        try {
            String query = "insert into Categoria "
                    + " (descripcion)"
                    + " values (?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1, categoria);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Boolean update(String categoria,String newcategoria) {
        try {
            String query = "update Categoria "
                    + " set descripcion = ? "
                    + " where descripcion =?";
            PreparedStatement st =  conn.prepareStatement(query);

            st.setString(1, newcategoria);
            st.setString(2, categoria);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Boolean delete(String categoria) {
        try {
            String query = "delete from Categoria where descripcion = '" + categoria +"'";
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Imposible borrar\n"+categoria+ " ya contiene Subcategorias");
            alert.show();
        }
        return false;
    }

    public int getid(String categoria){
        try {
            String query = "select idCategoria from Categoria where descripcion='"+categoria+"'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                return rs.getInt("idCategoria");
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return 0;

    }
}

