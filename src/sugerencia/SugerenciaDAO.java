package sugerencia;

import conexion.MySQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
public class SugerenciaDAO {

    //CORREGIR CUANDO ESTE USUARIO

    Connection conn;
    public SugerenciaDAO(Connection conn)
    {
        this.conn = conn;
    }

    public ObservableList<Sugerencia> fetchAll() {
        ObservableList<Sugerencia> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Sugerencia order by idSugerencia desc";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            Statement st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                Sugerencia vo = new Sugerencia(
                        rs.getInt("idSugerencia"),
                        rs.getString("idUsuario"),
                        rs.getString("tituloLibro"),
                        rs.getString("autorLibro"),
                        rs.getString("contenido"),
                        rs.getBytes("pdf")
                );
                list.add(vo);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return list;
    }

    public ObservableList<Sugerencia> fetchByUser(String idUsuario) {
        ObservableList<Sugerencia> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Sugerencia where idLibro = '" + idUsuario + "'";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            Statement st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                Sugerencia vo = new Sugerencia(
                        rs.getInt("idSugerencia"),
                        rs.getString("idUsuario"),
                        rs.getString("tituloLibro"),
                        rs.getString("autorLibro"),
                        rs.getString("contenido"),
                        rs.getBytes("pdf")
                );
                list.add(vo);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return list;
    }

    public boolean delete(Sugerencia sugerencia) {
        try {
            String query = "delete from Archivo where idSugerencia = " + sugerencia.getIdSugerencia() +" and idUsuario = '" + sugerencia.getIdUsuario() + "'";
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void insertSugerencia(Sugerencia sugerencia) {
        String sql = "insert into Sugerencia(idSugerencia, idUsuario, tituloLibro, autorLibro, contenido, pdf) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps =  conn.prepareStatement(sql);
            ps.setInt(1, sugerencia.getIdSugerencia());
            ps.setString(2, sugerencia.getIdUsuario());
            ps.setString(3, sugerencia.getTituloLibro());
            ps.setString(4, sugerencia.getAutorLibro());
            ps.setString(5, sugerencia.getContenido());
            ps.setBytes(6,  sugerencia.getPdf());
            ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void insertArchivo(int idSugerencia, String idUsuario, String tituloLibro, String autorLibro, String contenido, String ruta) {
        String sql = "insert into Sugerencia(idSugerencia, idUsuario, tituloLibro, autorLibro, contenido, pdf) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            Path pdfPath = Paths.get(ruta);
            byte[] pdf = Files.readAllBytes(pdfPath);

            PreparedStatement ps =  conn.prepareStatement(sql);
            ps.setInt(1, idSugerencia);
            ps.setString(2, idUsuario);
            ps.setString(3, tituloLibro);
            ps.setString(4,autorLibro);
            ps.setString(5,contenido);
            ps.setBytes(6,pdf);
            ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void insertArchivo(int idSugerencia, String idUsuario, String tituloLibro, String autorLibro, String contenido) {
        String sql = "insert into Sugerencia(idSugerencia, idUsuario, tituloLibro, autorLibro, contenido) VALUES(?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps =  conn.prepareStatement(sql);
            ps.setInt(1, idSugerencia);
            ps.setString(2, idUsuario);
            ps.setString(3, tituloLibro);
            ps.setString(4,autorLibro);
            ps.setString(5,contenido);
            ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public int NoSugerencia(String idUsuario){
        try {
            //String query = "select max(idSugerencia) as no from Sugerencia where idUsuario =  '" + idUsuario + "'";
            String query = "select max(idSugerencia) as no from Sugerencia";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next())
                return rs.getInt("no")+1;
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 1;
    }
}
