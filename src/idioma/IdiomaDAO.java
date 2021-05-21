package idioma;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class IdiomaDAO {

    Connection conn;
    public IdiomaDAO(Connection conn)
    {
        this.conn = conn;
    }


    public ObservableList<Idioma> fetchAll() {
        ObservableList<Idioma> idiomas = FXCollections.observableArrayList();
        try {
            String query = "select * from Idioma order by descripcion asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Idioma p = null;
            while(rs.next()) {
                p = new Idioma(
                        rs.getString("idIdioma"), rs.getString("descripcion")
                );
                idiomas.add(p);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return idiomas;
    }

    public Idioma fetch(String idIdioma) {
        ResultSet rs = null;
        Idioma d = null;
        try {
            String query = "SELECT * FROM Idioma where idIdioma = '" + idIdioma + "'";
            Statement st = conn.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            d = new Idioma(
                    rs.getString("idIdioma"), rs.getString("descripcion")
            );
            System.out.println(d);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return d;
    }

    public Boolean delete(String idIdioma) {
        try {
            String query = "delete from Idioma where idIdioma = '" + idIdioma +"'";
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean insert(Idioma idio) {
        try {
            String query = "insert into Idioma "
                    + " (idIdioma,descripcion)"
                    + " values (?, ?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1, idio.getIdIdioma());
            st.setString(2, idio.getDescripcion());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Boolean update(Idioma idio) {
        try {
            String query = "update Idioma "
                    + " set descripcion = ? "
                    + " where idIdioma =?";
            PreparedStatement st =  conn.prepareStatement(query);

            st.setString(1, idio.getDescripcion());
            st.setString(2, idio.getIdIdioma());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }
}
