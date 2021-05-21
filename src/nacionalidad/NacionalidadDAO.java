package nacionalidad;

import idioma.Idioma;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class NacionalidadDAO {

    Connection conn;
    public NacionalidadDAO(Connection conn)
    {
        this.conn = conn;
    }

    public ObservableList<Nacionalidad> fetchAll() {
        ObservableList<Nacionalidad> nacionalidads = FXCollections.observableArrayList();
        try {
            String query = "select * from Nacionalidad order by descripcion asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Nacionalidad p = null;
            while(rs.next()) {
                p = new Nacionalidad(
                        rs.getString("idNacionalidad"), rs.getString("descripcion")
                );
                nacionalidads.add(p);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return nacionalidads;
    }

    public Nacionalidad fetch(String idNacionalidad) {
        ResultSet rs = null;
        Nacionalidad d = null;
        try {
            String query = "SELECT * FROM Nacionalidad where idNacionalidad = '" + idNacionalidad + "'";
            Statement st = conn.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            d = new Nacionalidad(
                    rs.getString("idNacionalidad"), rs.getString("descripcion")
            );
            System.out.println(d);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return d;
    }

    public Boolean delete(String idNacionalidad) {
        try {
            String query = "delete from Nacionalidad where idNacionalidad = '" + idNacionalidad +"'";
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean insert(Nacionalidad nacionalidad) {
        try {
            String query = "insert into Nacionalidad "
                    + " (idNacionalidad,descripcion)"
                    + " values (?, ?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1, nacionalidad.getIdNacionalidad());
            st.setString(2, nacionalidad.getDescripcion());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Boolean update(Nacionalidad nacionalidad) {
        try {
            String query = "update Nacionalidad "
                    + " set descripcion = ? "
                    + " where idNacionalidad =?";
            PreparedStatement st =  conn.prepareStatement(query);

            st.setString(1, nacionalidad.getDescripcion());
            st.setString(2, nacionalidad.getIdNacionalidad());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }
}
