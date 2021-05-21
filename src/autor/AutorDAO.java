package autor;

import conexion.MySQL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nacionalidad.Nacionalidad;
import nacionalidad.NacionalidadDAO;

import java.sql.*;

public class AutorDAO {
    Connection conn;
    public AutorDAO(Connection conn){
        this.conn=conn;
    }

    public ObservableList<Autor> fetchAll() {
        ObservableList<Autor> list = FXCollections.observableArrayList();
        String sql = "select * " +
                "from Autor " +
                "join Nacionalidad N on Autor.nacionalidad = N.idNacionalidad " +
                "order by Autor.nombre asc";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            Statement st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {                 //CAMBIE CONSULTA Y CREE EL NACIONALIDAD
                Autor vo = new Autor(
                        rs.getInt("idAutor"),
                        rs.getString("nombre"),
                        new Nacionalidad(rs.getString("idNacionalidad"),
                                rs.getString("descripcion"))
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

    public Autor fetch(Autor autor) {
        Autor vo = null;
        String sql = "select * from Autor join Nacionalidad N on Autor.nacionalidad = N.idNacionalidad where " + autor.getIdAutor();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            Statement st = conn.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                vo = new Autor(
                        rs.getInt("idAutor"),
                        rs.getString("nombre"),
                        new Nacionalidad(rs.getString("idNacionalidad"),
                                rs.getString("descripcion"))
                );
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return vo;
    }

    public boolean delete(int idAutor) {
        try {
            String query = "delete from Autor where idAutor = " + idAutor;
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean insertAutor(Autor autor) {           //EL ID ES AUTOINCREMENT ASI QUE NO SE LE DEBE DE DAR UNO
        String sql = "INSERT INTO Autor (nombre, nacionalidad) VALUES(?, ?)";
        try {
            PreparedStatement ps =  conn.prepareStatement(sql);
            ps.setString(1, autor.getNombre());
            ps.setString(2,autor.getNacionalidad().getIdNacionalidad());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public Boolean update(Autor autor) {
        try {
            String query = "update Autor "
                    + " set nombre = ? "
                    + " where idAutor =?";
            PreparedStatement st =  conn.prepareStatement(query);

            st.setString(1, autor.getNombre());
            st.setInt(2, autor.getIdAutor());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public int noAutor(){
        try {
            String query = "select max(idAutor) as no from Autor";
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
