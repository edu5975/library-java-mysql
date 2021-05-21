package historial;

import imagen.Imagen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import libro.Libro;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.Date;

public class HistoriaDAO {
    Connection conn;
    public HistoriaDAO(Connection conn){
        this.conn=conn;
    }

    public ObservableList<Libro> fetchHistorialUsuario(String idUsuario){
        ObservableList<Libro> libroFavorito= FXCollections.observableArrayList();
        try {
            String query="select * " +
                    "from Libro L " +
                    "join Imagen I on L.idLibro = I.idLibro " +
                    "join Historial H on L.idLibro = H.idLibro " +
                    "where I.idImagen = 1 " +
                    "and H.idUsuario = '"+idUsuario+"' " +
                    "order by H.fechahora desc";
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
                f.setOtros(rs.getObject("H.fechahora"));
                libroFavorito.add(f);
            }


        }catch (SQLException ex){
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return libroFavorito;
    }

    public Boolean insert(Historial historial) {
        try {
            String query = "insert into Historial "
                    + " (idLibro,idUsuario,fechahora) "
                    + " values (?, ?, ?)";

            java.util.Date date = new Date();
            Object param = new java.sql.Timestamp(date.getTime());
            PreparedStatement st =  conn.prepareStatement(query);
            st.setInt(   1, historial.idLibro);
            st.setString(  2, historial.idUsuario);
            st.setObject(3,param);
            System.out.println(param.toString());
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
            String query = "delete from Historial " +
                    "where idLibro = " + idLibro;
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


}
