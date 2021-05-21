package categorias;

import categoria.Subcategorias;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import libro.Libro;

import java.sql.*;

public class CategoriasDAO {
    Connection conn;

    public CategoriasDAO(Connection conn)
    {
        this.conn = conn;
    }


    public ObservableList<Categorias> fetchAll() {
        ObservableList<Categorias> categorias = FXCollections.observableArrayList();
        try {
            String query = "select * " +
                    "from Categoria " +
                    "join SubCategoria SC on Categoria.idCategoria = SC.idCategoria " +
                    "join Categorias C on SC.idSubCategoria = C.idSubCategoria and SC.idCategoria = C.idCategoria " +
                    "join Libro L on C.idLibro = L.idLibro " +
                    "order by L.titulo asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Categorias p = null;
            while(rs.next()) {
                p = new Categorias(
                        new Libro(
                                rs.getInt("L.idLibro"),
                                rs.getString("L.titulo")
                        ),
                        new Subcategorias(
                                rs.getInt("SC.idSubCategoria") ,
                                rs.getString("SC.descripcion"),
                                rs.getString("Categoria.descripcion"),
                                rs.getInt("Categoria.idCategoria")
                        )
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

    public ObservableList<Categorias>  fetchbyid(int idLibro){
        ObservableList<Categorias> categorias = FXCollections.observableArrayList();
        try {
            String query = "select * " +
                    "from Categoria " +
                    "join SubCategoria SC on Categoria.idCategoria = SC.idCategoria " +
                    "join Categorias C on SC.idSubCategoria = C.idSubCategoria and SC.idCategoria = C.idCategoria " +
                    "join Libro L on C.idLibro = L.idLibro " +
                    "where L.idLibro = " + idLibro;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                Categorias p = new Categorias(
                        new Libro(
                                rs.getInt("L.idLibro"),
                                rs.getString("L.titulo")
                        ),
                        new Subcategorias(
                                rs.getInt("SC.idSubCategoria") ,
                                rs.getString("SC.descripcion"),
                                rs.getString("Categoria.descripcion"),
                                rs.getInt("Categoria.idCategoria")
                        )
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

    public Boolean insert(int idLibro,int idSubCategoria, int idCategoria) {
        try {
            String query = "insert into " +
                    "Categorias(idSubCategoria, idCategoria, idLibro) " +
                    "values (?,?,?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setInt(1,idSubCategoria);
            st.setInt(2,idCategoria);
            st.setInt(3,idLibro);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Boolean delete(int idLibro,int idSubCategoria, int idCategoria) {
        try {
            String query = "delete from Categorias " +
                    "where idLibro = ? " +
                    "and idSubCategoria = ? " +
                    "and idCategoria = ? ";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setInt(1,idLibro);
            st.setInt(2,idSubCategoria);
            st.setInt(3,idCategoria);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }
    public Boolean delete(int idLibro) {
        try {
            String query = "delete from Categorias " +
                    "where idLibro = ? ";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setInt(1,idLibro);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

}
