package tipoUsuario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class TipoUsuarioDAO {

    Connection conn;
    public TipoUsuarioDAO(Connection conn)
    {
        this.conn = conn;
    }


    public ObservableList<TipoUsuario> fetchAll() {
        ObservableList<TipoUsuario> tipoUsuarios = FXCollections.observableArrayList();
        try {
            String query = "select * from TipoUsuario;";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            TipoUsuario p = null;
            while(rs.next()) {
                p = new TipoUsuario(
                        rs.getInt("idTipoU"), rs.getString("descripcion")
                );
                tipoUsuarios.add(p);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return tipoUsuarios;
    }

    public TipoUsuario fetch(String idTipoU) {
        ResultSet rs = null;
        TipoUsuario d = null;
        try {
            String query = "SELECT * FROM TipoUsuario where idTipoU = '" + idTipoU + "'";
            Statement st = conn.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            d = new TipoUsuario(
                    rs.getInt("idTipoU"), rs.getString("descripcion")
            );
            System.out.println(d);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return d;
    }

    public Boolean delete(String idTipoU) {
        try {
            String query = "delete from TipoUsuario where idTipoU = '" + idTipoU +"'";
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean insert(TipoUsuario tipoUsuario) {
        try {
            String query = "insert into TipoUsuario "
                    + " (idTipoU,descripcion)"
                    + " values (?, ?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setInt(1, tipoUsuario.getIdTipoU());
            st.setString(2, tipoUsuario.getDescripcion());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Boolean update(TipoUsuario tipoUsuario) {
        try {
            String query = "update TipoUsuario "
                    + " set descripcion = ? "
                    + " where idTipoU =?";
            PreparedStatement st =  conn.prepareStatement(query);

            st.setString(1, tipoUsuario.getDescripcion());
            st.setInt(2, tipoUsuario.getIdTipoU());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }
}
