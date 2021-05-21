package favorito;

import autores.Autores;
import imagen.Imagen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import libro.Libro;

import java.io.ByteArrayInputStream;
import java.sql.*;

/*historial fetch por usuario e insert

favorito fech por usuario, insert y delete*/
public class FavoritoDAO {
    Connection conn;
    public FavoritoDAO(Connection conn){
        this.conn=conn;
    }

    public ObservableList<Libro> fetchFavoritoUsuario(String idUsuario){
        ObservableList<Libro> librosFavoritos= FXCollections.observableArrayList();
        try {
            String query="select * "+
                            "from Libro L " +
                            "join Imagen I on L.idLibro = I.idLibro "+
                            "where I.idImagen = 1 and L.idLibro in(select idLibro "+
                                                "from Favorito "+
                                                "where idUsuario in(select idUsuario "+
                                                                    "from Usuario "+
                                                                    "where idUsuario='"+idUsuario+"')) " +
                            "order by L.titulo asc ";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Libro f=null;
            while (rs.next()){
                Blob blob = rs.getBlob("I.imagen");

                // obtener la columna imagen, luego el arreglo de bytes
                byte byteImage[] = null;
                byteImage = blob.getBytes(1, (int) blob.length());

                // crear el Image y mostrarlo en el ImageView
                Image img = new Image(new ByteArrayInputStream(byteImage));
                f = new Libro(
                        rs.getInt("L.idLibro"),
                        rs.getString("L.titulo"),
                        new Imagen(
                                1,
                                rs.getInt("L.idLibro"),
                                img,
                                "portada"
                        )
                );
                librosFavoritos.add(f);
            }


        }catch (SQLException ex){
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return librosFavoritos;
    }

    public Boolean insert(Favorito fav) {
        try {
            String query = "insert into Favorito "
                    + " (idLibro,idUsuario,fecha) "
                    + " values (?, ?, ?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setInt(   1, fav.idLibro);
            st.setString(  2, fav.idUsuario);
            st.setDate(3,fav.fecha);

            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean delete(int idLibro, String idUsuario) {
        try {
            String query = "delete from Favorito " +
                    "where idLibro = " + idLibro + "  and idUsuario = '" + idUsuario + "'";
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean delete(int idLibro) {
        try {
            String query = "delete from Favorito " +
                    "where idLibro = " + idLibro;
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean delete(String idUsuario) {
        try {
            String query = "delete from Favorito " +
                    "where idUsuario = '" + idUsuario + "'";
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean noFavot(String idUsuario, int idLibro){
        try {
            String query = "select count(*) as no " +
                            "from Favorito " +
                            "where idUsuario = '"+idUsuario+"' " +
                            "and idLibro = " + idLibro;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()) {
                System.out.println(rs.getInt("no"));
                if (rs.getInt("no") > 0)
                    return true;
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return false;
    }
}
