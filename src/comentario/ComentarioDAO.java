package comentario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ComentarioDAO {

    Connection conn;

    public ComentarioDAO(Connection conn)
    {
        this.conn = conn;
    }

    public ObservableList<Comentario> fetchAll() {
        ObservableList<Comentario> comentarios = FXCollections.observableArrayList();
        try {
            String query = "select * from Comentario order by idUsuario asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Comentario p = null;
            while(rs.next()) {
                p = new Comentario(
                        rs.getInt("idLibro"),rs.getInt("idUsuario"),rs.getInt("idCalificacion"), rs.getString("comentario")
                );
                comentarios.add(p);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return comentarios;
    }

    public ObservableList<Comentario> fetchByLibro(int idLibro) {
        ObservableList<Comentario> comentarios = FXCollections.observableArrayList();
        try {
            String query = "select * from Comentario where idLibro='"+idLibro+"'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Comentario p = null;
            while(rs.next()) {
                p = new Comentario(
                        rs.getInt("idLibro"),rs.getInt("idUsuario"),rs.getInt("idCalificacion"), rs.getString("comentario")
                );
                comentarios.add(p);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return comentarios;
    }

    public Boolean insert(Comentario comentario) {
        try {
            String query = "insert into Comentario "
                    + " (idLibro,idUsuario,idCalificacion,comentario)"
                    + " values (?,?,?,?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setInt(1, comentario.getIdLibro());
            st.setInt(2, comentario.getIdUsuario());
            st.setInt(3, comentario.getIdCalificacion());
            st.setString(4, comentario.getComentario());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }
}
