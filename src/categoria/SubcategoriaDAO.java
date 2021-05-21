package categoria;

import conexion.MySQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;


public class SubcategoriaDAO {

    Connection conn;

    public SubcategoriaDAO(Connection conn)
    {
        this.conn = conn;
    }


    public ObservableList<Subcategorias> fetchAll() {
        ObservableList<Subcategorias> subcategorias = FXCollections.observableArrayList();
        ObservableList<Categoria> categorias = FXCollections.observableArrayList();
        CategoriaDAO categoriaDAO=new CategoriaDAO(MySQL.getConnection());
        try {
            String query = "select s.idSubCategoria,s.descripcion,c.descripcion,c.idCategoria " +
                    "from SubCategoria s " +
                    "inner join Categoria c on s.idCategoria=c.idCategoria " +
                    "" +
                    "order by c.idCategoria asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            categorias=categoriaDAO.fetchAll();
            Subcategorias p = null;
            while(rs.next()) {
                p = new Subcategorias(
                        rs.getInt("s.idSubCategoria") ,
                        rs.getString("s.descripcion"),
                        rs.getString("c.descripcion"),
                        rs.getInt("c.idCategoria")
                );
                subcategorias.add(p);
                //rs.getInt("idCategoria")
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return subcategorias;
    }

    public ObservableList<Subcategorias> fetchByCat(int idCategoria) {
        ObservableList<Subcategorias> subcategorias = FXCollections.observableArrayList();
        ObservableList<Categoria> categorias = FXCollections.observableArrayList();
        CategoriaDAO categoriaDAO=new CategoriaDAO(MySQL.getConnection());
        try {
            String query = "select s.idSubCategoria,s.descripcion,c.descripcion,c.idCategoria " +
                    "from SubCategoria s " +
                    "inner join Categoria c on s.idCategoria=c.idCategoria " +
                    "where c.idCategoria = " + idCategoria + " " +
                    "order by c.idCategoria asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            categorias=categoriaDAO.fetchAll();
            Subcategorias p = null;
            while(rs.next()) {
                p = new Subcategorias(
                        rs.getInt("s.idSubCategoria") ,
                        rs.getString("s.descripcion"),
                        rs.getString("c.descripcion"),
                        rs.getInt("c.idCategoria")
                );
                subcategorias.add(p);
                //rs.getInt("idCategoria")
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return subcategorias;
    }

    public int noSubCat(int idCategoria){
        try {
            String query = "select max(idSubCategoria) as no from SubCategoria where idCategoria = " + idCategoria;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next())
                return rs.getInt("no")+1;
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 1;
    }


    public boolean lookfor(String subname, String name){
        String resultSubName="test";
        String resultName="test";
        try {
            String query = "select * from SubCategoria s inner join Categoria c where s.descripcion= '"+subname+"' and c.descripcion='"+name+"'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                //rs.getInt("idCategoria")
                //rs.getString("descripcion")
                resultSubName=rs.getString("s.descripcion");
                resultName=rs.getString("c.descripcion");
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }

        if(resultName.equals(name) && resultSubName.equals(subname)){
            return true;
        }
        else
        {
            return false;
        }
    }

    public Boolean insert(String subname,int idcat) {
        try {
            String query = "insert into SubCategoria "
                    + " (idSubCategoria,idCategoria,descripcion)"
                    + " values (?,?,?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setInt(1, noSubCat(idcat));
            st.setInt(2, idcat);
            st.setString(3, subname);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Boolean delete(String subcategoria, int idCat) {
        try {
            String query = "delete from SubCategoria where descripcion = '" + subcategoria +"' and idCategoria='"+idCat+"'";
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Imposible borrar\n"+subcategoria+ " ya contiene libros asignados");
            alert.show();
        }
        return false;
    }


    public Boolean update(String subcategoria,String newsubcategoria) {
        try {
            String query = "update SubCategoria "
                    + " set descripcion = ? "
                    + " where descripcion =?";
            PreparedStatement st =  conn.prepareStatement(query);

            st.setString(1, newsubcategoria);
            st.setString(2, subcategoria);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }
}

