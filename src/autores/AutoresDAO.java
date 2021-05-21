package autores;

import autor.Autor;
import editorial.Editorial;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import libro.Libro;
import nacionalidad.Nacionalidad;

import java.sql.*;

public class AutoresDAO {
    Connection conn;
    public AutoresDAO(Connection conn){
        this.conn=conn;
    }

    public ObservableList<Autor>fetchAutorByBook(String titulo){
        ObservableList<Autor>autores= FXCollections.observableArrayList();
       try {
            String query="select * "+
                            "from Autor a inner join Autores A2 on a.idAutor = A2.idAutor "+
                               "inner join Libro l on A2.idLibro = l.idLibro "+
                                "inner join Nacionalidad n on a.nacionalidad=n.idNacionalidad "+
                                "where titulo='"+titulo+"'";

           Statement st = conn.createStatement();
           ResultSet rs = st.executeQuery(query);
           Autor p = null;
           while(rs.next()) {
               p = new Autor(
                               rs.getInt("idAutor"),
                               rs.getString("nombre"),
                               new Nacionalidad(rs.getString("nacionalidad"),
                                       rs.getString("descripcion"))
                       );
               System.out.println(p);
               autores.add(p);
           }

        }catch (SQLException ex){
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }

       return autores;
    }

    public ObservableList<Autor>fetchAutorByBook(int idLibro){
        ObservableList<Autor>autores= FXCollections.observableArrayList();
        try {
            String query="select * "+
                    "from Autor a inner join Autores A2 on a.idAutor = A2.idAutor "+
                    "inner join Libro l on A2.idLibro = l.idLibro "+
                    "inner join Nacionalidad n on a.nacionalidad=n.idNacionalidad "+
                    "where l.idLibro='"+idLibro+"'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Autor p = null;
            while(rs.next()) {
                p = new Autor(
                        rs.getInt("idAutor"),
                        rs.getString("nombre"),
                        new Nacionalidad(rs.getString("nacionalidad"),
                                rs.getString("descripcion"))
                );
                System.out.println(p);
                autores.add(p);
            }

        }catch (SQLException ex){
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }

        return autores;
    }

    public ObservableList<Autores>fetchAutoresByBook(int idLibro){
        ObservableList<Autores>autores= FXCollections.observableArrayList();
        try {
            String query="select * "+
                    "from Autor a inner join Autores A2 on a.idAutor = A2.idAutor "+
                    "inner join Libro l on A2.idLibro = l.idLibro "+
                    "inner join Nacionalidad n on a.nacionalidad=n.idNacionalidad "+
                    "where l.idLibro='"+idLibro+"'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Autores p = null;
            while(rs.next()) {
                p = new Autores(
                        new Autor(
                                rs.getInt("idAutor"),
                                rs.getString("nombre"),
                                new Nacionalidad(rs.getString("nacionalidad"),
                                        rs.getString("descripcion"))
                        ),
                        new Libro(
                                idLibro
                        )
                );
                System.out.println(p);
                autores.add(p);
            }

        }catch (SQLException ex){
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }

        return autores;
    }

    public Autor fetchOneAutorByBook(int idLibro){
        try {
            String query="select * "+
                    "from Autor a inner join Autores A2 on a.idAutor = A2.idAutor "+
                    "inner join Libro l on A2.idLibro = l.idLibro "+
                    "inner join Nacionalidad n on a.nacionalidad=n.idNacionalidad "+
                    "where l.idLibro='"+idLibro+"' " +
                    "limit 1";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Autor p = null;
            while(rs.next()) {
                p = new Autor(
                        rs.getInt("idAutor"),
                        rs.getString("nombre"),
                        new Nacionalidad(rs.getString("nacionalidad"),
                                rs.getString("descripcion"))
                );
                System.out.println(p);
                return p;
            }

        }catch (SQLException ex){
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return null;
    }



    public ObservableList<Autor>fetchAutorNotInBook(String titulo){
        ObservableList<Autor>autores= FXCollections.observableArrayList();
        try {
            String query="select * "+
            "from Autor a "+
            "inner join Nacionalidad n on n.idNacionalidad=a.nacionalidad "+
            "where idAutor not in (select idAutor "+
                                    "from Autores "+
                                    "where idLibro in(SELECT idLibro "+
                                                        "from Libro "+
                                                        "where titulo='"+titulo+"'))";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Autor p = null;
            while(rs.next()) {
                p = new Autor(
                                rs.getInt("idAutor"),
                                rs.getString("nombre"),
                                new Nacionalidad(rs.getString("nacionalidad"),
                                        rs.getString("descripcion"))
                        );
                autores.add(p);
            }

        }catch (SQLException ex){
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }

        return autores;
    }

    public ObservableList<Autores>fetchLibro(){
        ObservableList<Autores>libros=FXCollections.observableArrayList();
        try {
            String query = "select distinct L.idLibro, L.titulo, A2.idAutor,A2.nombre,E.idEditorial, E.nombre, L.fechaPublicacion, L.paginas " +
                    "from Libro L " +
                    "join Editorial E on L.idEditorial = E.idEditorial " +
                    "join Autores A on L.idLibro = A.idLibro " +
                    "join Autor A2 on A.idAutor = A2.idAutor ";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Autores l = null;
            while(rs.next()) {
                l = new Autores(
                        new Libro(
                            rs.getInt("L.idLibro"),
                            rs.getString("L.titulo"),
                            new Autor(
                                rs.getInt("A2.idAutor"),
                                rs.getString("A2.nombre")
                            ),
                            rs.getDate("L.fechaPublicacion"),
                            new Editorial(
                                    rs.getInt("E.idEditorial"),
                                    rs.getString("E.nombre")
                        ),
                        rs.getInt("L.paginas")
                ));
                libros.add(l);
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return libros;
    }

    public Boolean delete(int idAutor, int idLibro) {
        try {
            String query = "delete from Autores " +
                    "where idAutor = " + idAutor + "  and idLibro = '" + idLibro + "'";
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
            String query = "delete from Autores " +
                    "where idLibro = '" + idLibro + "'";
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean insert(Autores info) {
        try {
            String query = "insert into Autores "
                    + " (idAutor,idLibro) "
                    + " values (?, ?)";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setInt(   1, info.getAutor().getIdAutor());
            st.setInt(  2, info.getLibro().getIdLibro());

            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }
}
