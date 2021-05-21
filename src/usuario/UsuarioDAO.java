package usuario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class UsuarioDAO {
    Connection conn;

    public UsuarioDAO(Connection conn)
    {
        this.conn = conn;
    }

    public boolean Exist(String user){
        String resultName="test";
        try {
            String query = "select * from Usuario where idUsuario= '"+user+"'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                resultName=rs.getString("idUsuario");
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        if(resultName.equals(user)){
            return true;
        }
        else
        {
            return false;
        }

    }

    public Boolean insert(Usuario newuser) {
        try {
            String query = "insert into Usuario "
                    + " (idUsuario,tipoUsuario,nombre,apellido,email,nacimiento,contrasena)"
                    + " values (?,?,?,?,?,?,md5(?))";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1, newuser.getIdUsuario());
            st.setInt(2, newuser.getTipoUsuario());
            st.setString(3, newuser.getNombre());
            st.setString(4, newuser.getApellido());
            st.setString(5, newuser.getEmail());
            st.setDate(6, newuser.getNacimiento());
            st.setString(7, newuser.getContraseña());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Usuario fetchbyid(String idUsuario){
        Usuario users=null;
        try {
            String query = "select * from Usuario where idUsuario= '"+idUsuario+"'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()) {
                users = new Usuario(
                        rs.getString("idUsuario"),rs.getString("nombre"),
                        rs.getString("apellido"),rs.getString("email"),
                        rs.getDate("nacimiento"),rs.getString("contrasena"),rs.getInt("tipoUsuario")
                );
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return users;
    }


    public Boolean validUser(String username, String password) {
        ResultSet rs = null;
        int total=0;
        Usuario e = null;
        try {
            String query = "SELECT count(*) as total from Usuario where idUsuario = '" + username + "'" +
                    " and contrasena = md5('"+ password +"')";
            Statement st = conn.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            total=rs.getInt("total");

            if(total==1)
            {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return (total>=1)? true:false;
    }


    public ObservableList<Usuario> fetchAll() {
        ObservableList<Usuario> users = FXCollections.observableArrayList();
        try {
            String query = "select * from Usuario order by idUsuario asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Usuario p = null;
            while(rs.next()) {
                p = new Usuario(
                        rs.getString("idUsuario"),rs.getString("nombre"),
                        rs.getString("apellido"),rs.getString("email"),
                        rs.getDate("nacimiento"),rs.getString("contrasena"),rs.getInt("tipoUsuario")
                );
                users.add(p);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return users;
    }


    public Boolean delete(String idUsuario) {
        try {
            String query = "delete from Usuario where idUsuario = '" + idUsuario +"'";
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Imposible borrar\n"+idUsuario);
            alert.show();

        }
        return false;
    }


    public Boolean updateNombre(String nombre,String newnombre,String idUsuario) {
        try {
            String query = "update Usuario "
                    + " set nombre = ? "
                    + " where nombre =? and idUsuario=?";
            PreparedStatement st =  conn.prepareStatement(query);

            st.setString(1, newnombre);
            st.setString(2, nombre);
            st.setString(3, idUsuario);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }


    public Boolean updateApellido(String apellido,String newapellido,String idUsuario) {
        try {
            String query = "update Usuario "
                    + " set apellido = ? "
                    + " where apellido =? and idUsuario=?";
            PreparedStatement st =  conn.prepareStatement(query);

            st.setString(1, newapellido);
            st.setString(2, apellido);
            st.setString(3, idUsuario);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }



    public Boolean updateEmail(String Email,String newEmail, String idUsuario) {
        try {
            String query = "update Usuario "
                    + " set email = ? "
                    + " where email =? and idUsuario=?";
            PreparedStatement st =  conn.prepareStatement(query);

            st.setString(1, newEmail);
            st.setString(2, Email);
            st.setString(3, idUsuario);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }




    public Boolean updateNacimiento(Date nacimiento,Date newnacimiento,String idUsuario) {
        try {
            String query = "update Usuario "
                    + " set nacimiento = ? "
                    + " where nacimiento =? and idUsuario=?";
            PreparedStatement st =  conn.prepareStatement(query);
            System.out.println(newnacimiento);

            st.setDate(1, newnacimiento);
            st.setDate(2, nacimiento);
            st.setString(3, idUsuario);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }


    public Boolean updateContrasena(String contrasena,String newcontrasena,String idUsuario) {
        try {
            String query = "update Usuario "
                    + " set contrasena = md5(?) "
                    + " where contrasena =md5(?) and idUsuario=?";
            PreparedStatement st =  conn.prepareStatement(query);

            st.setString(1, newcontrasena);
            st.setString(2, contrasena);
            st.setString(3, idUsuario);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }


    public Boolean updatetipoUsuario(int tipo,int newtipo,String idUsuario) {
        try {
            String query = "update Usuario "
                    + " set tipoUsuario = ? "
                    + " where tipoUsuario =? and idUsuario=?";
            PreparedStatement st =  conn.prepareStatement(query);

            st.setInt(1, newtipo);
            st.setInt(2, tipo);
            st.setString(3, idUsuario);
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }





}
