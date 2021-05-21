package historial;

import libro.Libro;
import usuario.Usuario;

import java.sql.Date;

public class Historial {
    Libro libro;
    Usuario usuario;
    Date fecha;

    int idLibro;
    String idUsuario;

    public Historial() {
    }

    public Historial(int idLibro, String usuario) {
        this.idLibro = idLibro;
        idUsuario = usuario;
    }

    public Historial(Libro libro, Usuario usuario, Date fecha) {
        this.libro = libro;
        this.usuario = usuario;
        this.fecha = fecha;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public void setUsuario(String usuario) {
        idUsuario = usuario;
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
