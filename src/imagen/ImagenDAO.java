package imagen;

import com.mysql.cj.result.BinaryStreamValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;

public class ImagenDAO {

    Connection conn;
    Statement st;
    public ImagenDAO(Connection conn)
    {
        this.conn = conn;
    }

    public boolean guardarImagen(int idImagen, int idLibro, String ruta,String descripcion){
        String insert = "insert into Imagen(idImagen, idLibro, imagen, descripcion) values (?,?,?,?)";
        FileInputStream fis = null;
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            File file = new File(ruta);
            fis = new FileInputStream(file);

            ps = conn.prepareStatement(insert);
            ps.setInt(1,idImagen);
            ps.setInt(2,idLibro);
            ps.setBinaryStream(3,fis,(int)file.length());
            ps.setString(4, descripcion);
            ps.executeUpdate();
            conn.commit();
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
        }finally{
            try {
                ps.close();
                fis.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        return false;
    }

    public boolean borrarImagenes(int idLibro){
        String delete = "delete from Imagen where idLibro = ?";
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(delete);
            ps.setInt(1,idLibro);
            ps.executeUpdate();
            conn.commit();
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
        }finally{
            try {
                ps.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        return false;
    }

    public boolean borrarImagen(int idLibro, int idImagen){
        String delete = "delete from Imagen where idLibro = ? and idImagen = ?";
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(delete);
            ps.setInt(1,idLibro);
            ps.setInt(2,idImagen);
            ps.executeUpdate();
            conn.commit();
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
        }finally{
            try {
                ps.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        return false;
    }

    public ObservableList<Imagen> imagenesLibro(int idLibro){
        ObservableList<Imagen> lista = FXCollections.observableArrayList();
        try {
            String query = "select * from Imagen where idLibro = " + idLibro;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next())
            {
                Imagen ima = new Imagen();

                int idImagen = rs.getInt("idImagen");
                String descripcion = rs.getString("descripcion");
                Blob blob = rs.getBlob("imagen");

                // obtener la columna imagen, luego el arreglo de bytes
                byte byteImage[] = null;
                byteImage = blob.getBytes(1, (int) blob.length());

                // crear el Image y mostrarlo en el ImageView
                Image img = new Image(new ByteArrayInputStream(byteImage));

                ima.setDescripcion(descripcion);
                ima.setIdImagen(idImagen);
                ima.setIdLibro(idLibro);
                ima.setImagen(img);

                lista.add(ima);
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return lista;
    }

    public Imagen portada(int idLibro){
        Imagen ima = new Imagen();
        try {
            String query = "select * from Imagen where idLibro = " + idLibro + " limit 1";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next())
            {

                int idImagen = rs.getInt("idImagen");
                String descripcion = rs.getString("descripcion");
                Blob blob = rs.getBlob("imagen");

                // obtener la columna imagen, luego el arreglo de bytes
                byte byteImage[] = null;
                byteImage = blob.getBytes(1, (int) blob.length());

                // crear el Image y mostrarlo en el ImageView
                Image img = new Image(new ByteArrayInputStream(byteImage));

                ima.setDescripcion(descripcion);
                ima.setIdImagen(idImagen);
                ima.setIdLibro(idLibro);
                ima.setImagen(img);
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return ima;
    }

    public int noimagen(int idLibro){
        try {
            String query = "select max(idImagen) as no from Imagen where idLibro = " + idLibro;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next())
                return rs.getInt("no") + 1;
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 1;
    }

}
