package libro;

import autor.Autor;
import autores.Autores;
import autores.AutoresDAO;
import conexion.MySQL;
import editorial.Editorial;
import imagen.Imagen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import nacionalidad.Nacionalidad;

import java.io.ByteArrayInputStream;
import java.sql.*;

public class LibroDAO {

    Connection conn;
    Statement st;

    public LibroDAO(Connection conn)
    {
        this.conn = conn;
    }


    public ObservableList<Libro> fetchAll() {
        ObservableList<Libro> libros = FXCollections.observableArrayList();
        try {
            String query = "select L.idLibro, L.titulo " +
                    "from Libro L " +
                    "order by L.titulo asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Libro l = null;
            while(rs.next()) {
                l = new Libro(
                        rs.getInt("L.idLibro"),
                        rs.getString("L.titulo")
                );
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

    public ObservableList<Libro> fetchAllInfo() {
        ObservableList<Libro> libros = FXCollections.observableArrayList();
        try {
            String query = "select * " +
                    "from Libro L " +
                    "join Editorial E on L.idEditorial = E.idEditorial " +
                    "order by L.titulo asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Libro l = null;
            while(rs.next()) {
                l = new Libro();
                l.setIdLibro(rs.getInt("L.idLibro"));
                l.setTitulo(rs.getString("L.titulo"));
                l.setFechaPublicacion(rs.getDate("L.fechaPublicacion"));
                l.setEditorial(new Editorial(
                        rs.getInt("E.idEditorial"),
                        rs.getString("E.nombre")
                ));
                l.setPaginas(rs.getInt("L.paginas"));
                l.setContenido(rs.getString("L.contenido"));

                AutoresDAO autoresDAO = new AutoresDAO(MySQL.getConnection());
                l.setAutor(autoresDAO.fetchOneAutorByBook(l.getIdLibro()));
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

    public ObservableList<Libro> fetchAllImage() {
        ObservableList<Libro> libros = FXCollections.observableArrayList();
        try {
            String query = "select L.idLibro, L.titulo, I.imagen " +
                    "from Libro L " +
                    "join Imagen I on L.idLibro = I.idLibro " +
                    "where I.idImagen = 1 " +
                    "order by L.titulo asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Libro l = null;
            while(rs.next()) {
                Blob blob = rs.getBlob("I.imagen");

                // obtener la columna imagen, luego el arreglo de bytes
                byte byteImage[] = null;
                byteImage = blob.getBytes(1, (int) blob.length());

                // crear el Image y mostrarlo en el ImageView
                Image img = new Image(new ByteArrayInputStream(byteImage));
                l = new Libro(
                        rs.getInt("L.idLibro"),
                        rs.getString("L.titulo"),
                        new Imagen(
                                1,
                                rs.getInt("L.idLibro"),
                                img,
                                "portada"
                        )
                );
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

    public ObservableList<Libro> fetchByCat(int idCategoria) {
        ObservableList<Libro> libros = FXCollections.observableArrayList();
        try {
            String query = "select * " +
                    "from Libro L " +
                    "join Imagen I on L.idLibro = I.idLibro " +
                    "join Categorias C on L.idLibro = C.idLibro " +
                    "join SubCategoria SC on C.idSubCategoria = SC.idSubCategoria and C.idCategoria = SC.idCategoria " +
                    "where I.idImagen = 1 " +
                    "and SC.idCategoria = " + idCategoria + " " +
                    "group by L.idLibro " +
                    "order by L.titulo asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Libro l = null;
            while(rs.next()) {
                Blob blob = rs.getBlob("I.imagen");

                // obtener la columna imagen, luego el arreglo de bytes
                byte byteImage[] = null;
                byteImage = blob.getBytes(1, (int) blob.length());

                // crear el Image y mostrarlo en el ImageView
                Image img = new Image(new ByteArrayInputStream(byteImage));
                l = new Libro(
                        rs.getInt("L.idLibro"),
                        rs.getString("L.titulo"),
                        new Imagen(
                                1,
                                rs.getInt("L.idLibro"),
                                img,
                                "portada"
                        )
                );
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

    public ObservableList<Libro> fetchBySubCat(int idCategoria, int idSubCategoria) {
        ObservableList<Libro> libros = FXCollections.observableArrayList();
        try {
            String query = "select * " +
                    "from Libro L " +
                    "join Imagen I on L.idLibro = I.idLibro " +
                    "join Categorias C on L.idLibro = C.idLibro " +
                    "join SubCategoria SC on C.idSubCategoria = SC.idSubCategoria and C.idCategoria = SC.idCategoria " +
                    "where I.idImagen = 1 " +
                    "and SC.idCategoria = " + idCategoria + " " +
                    "and SC.idSubCategoria = " + idSubCategoria + " " +
                    "group by L.idLibro " +
                    "order by L.titulo asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Libro l = null;
            while(rs.next()) {
                Blob blob = rs.getBlob("I.imagen");

                // obtener la columna imagen, luego el arreglo de bytes
                byte byteImage[] = null;
                byteImage = blob.getBytes(1, (int) blob.length());

                // crear el Image y mostrarlo en el ImageView
                Image img = new Image(new ByteArrayInputStream(byteImage));
                l = new Libro(
                        rs.getInt("L.idLibro"),
                        rs.getString("L.titulo"),
                        new Imagen(
                                1,
                                rs.getInt("L.idLibro"),
                                img,
                                "portada"
                        )
                );
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

    public ObservableList<Libro> fetchBusqueda(String busqueda) {
        ObservableList<Libro> libros = FXCollections.observableArrayList();
        try {
            String query = "select * " +
                    "from Libro L " +
                    "join Imagen I on L.idLibro = I.idLibro " +
                    "join Autores A on L.idLibro = A.idLibro " +
                    "join Autor A2 on A.idAutor = A2.idAutor " +
                    "where I.idImagen = 1 " +
                    "and (L.titulo like '%"+busqueda+"%' " +
                    "or A2.nombre like  '%"+busqueda+"%') " +
                    "group by L.idLibro " +
                    "order by L.titulo asc;";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            Libro l = null;
            while(rs.next()) {
                Blob blob = rs.getBlob("I.imagen");

                // obtener la columna imagen, luego el arreglo de bytes
                byte byteImage[] = null;
                byteImage = blob.getBytes(1, (int) blob.length());

                // crear el Image y mostrarlo en el ImageView
                Image img = new Image(new ByteArrayInputStream(byteImage));
                l = new Libro(
                        rs.getInt("L.idLibro"),
                        rs.getString("L.titulo"),
                        new Imagen(
                                1,
                                rs.getInt("L.idLibro"),
                                img,
                                "portada"
                        )
                );
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

    public Libro fetch(int idLibro) {
        ResultSet rs = null;
        Libro l = null;
        try {
            String query = "select L.idLibro, L.titulo, A2.idAutor,A2.nombre,E.idEditorial, E.nombre, L.fechaPublicacion, L.paginas, L.contenido " +
                    "from Libro L " +
                    "join Editorial E on L.idEditorial = E.idEditorial " +
                    "join Autores A on L.idLibro = A.idLibro " +
                    "join Autor A2 on A.idAutor = A2.idAutor " +
                    "where idLibro = " + idLibro ;
            Statement st = conn.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            l = new Libro(
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
                    rs.getInt("L.paginas"),
                    rs.getString("L.contenido")
            );
            System.out.println(l);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return l;
    }

    public Libro fetchWithImage(int idLibro) {
        ResultSet rs = null;
        Libro l = null;
        try {
            String query = "select distinct L.idLibro, L.titulo, A2.idAutor,A2.nombre,E.idEditorial, E.nombre, " +
                    "L.fechaPublicacion, L.paginas, L.contenido, I.imagen " +
                    "from Libro L " +
                    "join Editorial E on L.idEditorial = E.idEditorial " +
                    "join Autores A on L.idLibro = A.idLibro " +
                    "join Autor A2 on A.idAutor = A2.idAutor " +
                    "join Imagen I on L.idLibro = I.idLibro " +
                    "where L.idLibro = " + idLibro +
                    " limit 1";
            Statement st = conn.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            Blob blob = rs.getBlob("I.imagen");

            // obtener la columna imagen, luego el arreglo de bytes
            byte byteImage[] = null;
            byteImage = blob.getBytes(1, (int) blob.length());

            // crear el Image y mostrarlo en el ImageView
            Image img = new Image(new ByteArrayInputStream(byteImage));
            l = new Libro(
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
                    rs.getInt("L.paginas"),
                    rs.getString("L.contenido"),
                    new Imagen(
                            1,
                            rs.getInt("L.idLibro"),
                            img,
                            "portada"
                    )
            );
            System.out.println(l);
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("Error al recuperar información...");
        }
        return l;
    }

    public Boolean delete(int idLibro) {
        try {
            String query = "delete from Libro where idLibro = " + idLibro ;
            PreparedStatement st = conn.prepareStatement(query);
            st.execute();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean insert(Libro libro) {
        try {
            String query = "insert into Libro(titulo, fechaPublicacion, idEditorial, paginas, contenido) " +
                    "values (?,?,?,?,?);";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1,libro.getTitulo());
            st.setDate(2,libro.getFechaPublicacion());
            st.setInt(3,libro.getEditorial().getIdEditorial());
            st.setInt(4,libro.getPaginas());
            st.setString(5,libro.getContenido());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }

    public Boolean update(Libro libro) {
        try {
            String query = "update Libro "
                    + " set titulo = ?, fechaPublicacion = ?, idEditorial = ?, paginas = ?, contenido = ? "
                    + " where idLibro = ?";
            PreparedStatement st =  conn.prepareStatement(query);
            st.setString(1,libro.getTitulo());
            st.setDate(2,libro.getFechaPublicacion());
            st.setInt(3,libro.getEditorial().getIdEditorial());
            st.setInt(4,libro.getPaginas());
            st.setString(5,libro.getContenido());
            st.setInt(6,libro.getIdLibro());
            st.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int noLibro(){
        try {
            String query = "select max(idLibro) as no from Libro";
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
