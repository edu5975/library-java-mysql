package login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;

public class UsersDAO {

    Connection conn;
    public UsersDAO(Connection conn)
    {
        this.conn = conn;
    }

    public ObservableList<Users> fetchAll() {
        ObservableList<Users> users = FXCollections.observableArrayList();
        try {
            String query = "select idUsuario, contrasena " +
                    "from Usuario";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Users u = null;
            while(rs.next()) {
                u = new Users(rs.getString("username"), rs.getString("password"));
                users.add(u);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return users;
    }



    public List<Users> findAll() {
        List<Users> users = FXCollections.observableArrayList();
        try {
            String query = "select idUsuario, contrasena " +
                    "from Usuario";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Users u = null;
            while(rs.next()) {
                u = new Users(
                        rs.getString("username"), rs.getString("password"));
                users.add(u);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return users;
    }

    public Users validUser(String username, String password) {
        ResultSet rs = null;
        try {
            String query = "select idUsuario, descripcion " +
                    "from Usuario u " +
                    "join TipoUsuario t on u.tipoUsuario = t.idTipoU " +
                    "where idUsuario = '" + username + "' " +
                    "and contrasena = md5('" + password + "')";
            PreparedStatement st =  conn.prepareStatement(query);
            rs = st.executeQuery(query);

            if(rs.next()){
                Users e = new Users();
                e.setIduser(rs.getString("idUsuario"));
                e.setTipoUsuario(rs.getString("descripcion"));
                return e;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return null;
    }
}
