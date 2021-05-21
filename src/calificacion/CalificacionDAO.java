package calificacion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CalificacionDAO {
    Connection conn;

    public CalificacionDAO(Connection conn)
    {
        this.conn = conn;
    }

    public ObservableList<Calificacion> fetchAll() {
        ObservableList<Calificacion> calificacions = FXCollections.observableArrayList();
        try {
            String query = "select * from Calificacion order by idCalificacion asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Calificacion p = null;
            while(rs.next()) {
                p = new Calificacion(
                        rs.getInt("idCalificacion"), rs.getString("descripcion")
                );
                calificacions.add(p);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return calificacions;
    }

    public Calificacion fetchByid(int idCalificacion) {
        Calificacion calificacion=null;
        try {
            String query = "select * from Calificacion where idCalificacion='"+idCalificacion+"'";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()) {
                calificacion = new Calificacion(
                        rs.getInt("idCalificacion"), rs.getString("descripcion")
                );
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return calificacion;
    }
}
