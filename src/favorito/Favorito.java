package favorito;

import libro.Libro;
import usuario.Usuario;

import java.sql.Date;

public class Favorito {
    Libro libro;
    Usuario usuario;
    Date fecha;

    String idUsuario;
    int idLibro;

    public Favorito(Date fecha, String idUsuario, int idLibro) {
        this.fecha = fecha;
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
    }

    public Favorito() {
    }


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
