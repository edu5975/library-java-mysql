package editorial;

import idioma.Idioma;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class EditorialDAO {
    Connection conn;
    public EditorialDAO(Connection conn)
    {
        this.conn = conn;
    }

    public ObservableList<Editorial> fetchAll() {
        ObservableList<Editorial> editorials = FXCollections.observableArrayList();
        try {
            String query = "select * from Editorial order by idEditorial asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Editorial p = null;
            while(rs.next()) {
                p = new Editorial(
                        rs.getInt("idEditorial"), rs.getString("nombre")
                );
                editorials.add(p);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return editorials;
    }

    public Editorial fetch(String idEditorial) {
        ResultSet rs = null;
        Editorial d = null;
        try {
            String query = "SELECT * FROM Editorial where idEditorial = '" + idEditorial + "'";
            Statement st = conn.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            d = new Editorial(
                    rs.getInt("idEditorial"), rs.getString("descripcion")
            );
            System.out.println(d);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return d;
    }

    public Boolean delete(String idEditorial) {
        try {
            String query = "delete from Editorial where idEditorial = '" + idEditorial +"'";
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean insert(Editorial editorial) {
        try {
            String query = "insert into Editorial "
                    + " (idEditorial,nombre)"
                    + " values (?, ?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setInt(1, editorial.getIdEditorial());
            st.setString(2, editorial.getNombre());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Boolean update(Editorial editorial) {
        try {
            String query = "update Editorial "
                    + " set nombre = ? "
                    + " where idEditorial =?";
            PreparedStatement st =  conn.prepareStatement(query);

            st.setString(1, editorial.getNombre());
            st.setInt(2, editorial.getIdEditorial());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public int noEditorial(){
        try {
            String query = "select max(idEditorial) as no from Editorial ";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next())
                return rs.getInt("no") + 1;
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 0;
    }
}
