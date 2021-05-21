package archivo;

import idioma.IdiomaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import conexion.MySQL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class ArchivoDAO {

    Connection conn;
    public ArchivoDAO(Connection conn)
    {
        this.conn = conn;
    }

    public ObservableList<Archivo> fetchAll(int idLibro) {
        ObservableList<Archivo> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Archivo where idLibro = " + idLibro;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            Statement st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                IdiomaDAO idiomaDAO = new IdiomaDAO(MySQL.getConnection());
                Archivo vo = new Archivo(
                        rs.getInt("idArchivo"),
                        rs.getInt("idLibro"),
                        rs.getBytes("pdf"),
                        idiomaDAO.fetch(rs.getString("idIdioma"))
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

    public Archivo fetch(Archivo archivo) {
        Archivo vo = null;
        String sql = "SELECT * FROM Archivo where idLibro = " + archivo.getIdLibro() + " idArchivo = " + archivo.getIdArchivo();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            Statement st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                IdiomaDAO idiomaDAO = new IdiomaDAO(MySQL.getConnection());
                vo = new Archivo(
                        rs.getInt("idArchivo"),
                        rs.getInt("idLibro"),
                        rs.getBytes("pdf"),
                        idiomaDAO.fetch(rs.getString("idIdioma"))
                );
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return vo;
    }

    public boolean delete(Archivo archivo) {
        try {
            String query = "delete from Archivo where idArchivo = " + archivo.getIdArchivo() +" and idLibro = " + archivo.idLibro;
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean delete(int idLibro) {
        try {
            String query = "delete from Archivo where idLibro = " + idLibro;
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void insertArchivo(Archivo archivo) {
        String sql = "INSERT INTO Archivo (idArchivo, idLibro, pdf, idIdioma) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement ps =  conn.prepareStatement(sql);
            ps.setInt(1, archivo.getIdArchivo());
            ps.setInt(2, archivo.getIdLibro());
            ps.setBytes(3, archivo.getPdf());
            ps.setString(4,archivo.getIdioma().getIdIdioma());
            ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void insertArchivo(int idArchivo, int idLibro, String ruta, String idIdioma) {
        String sql = "INSERT INTO Archivo (idArchivo, idLibro, pdf, idIdioma) VALUES(?, ?, ?, ?)";
        try {
            Path pdfPath = Paths.get(ruta);
            byte[] pdf = Files.readAllBytes(pdfPath);

            PreparedStatement ps =  conn.prepareStatement(sql);
            ps.setInt(1, idArchivo);
            ps.setInt(2, idLibro);
            ps.setBytes(3, pdf);
            ps.setString(4,idIdioma);
            ps.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public int noarchivo(int idLibro){
        try {
            String query = "select max(idArchivo) as no from Archivo where idLibro =  " + idLibro;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next())
                return rs.getInt("no");
            rs.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 0;
    }
}
