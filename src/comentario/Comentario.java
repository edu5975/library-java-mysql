package comentario;

public class Comentario {

    int idLibro,idUsuario,idCalificacion;
    String comentario;

    public Comentario(int idLibro, int idUsuario, int idCalificacion, String comentario) {
        this.idLibro = idLibro;
        this.idUsuario = idUsuario;
        this.idCalificacion = idCalificacion;
        this.comentario = comentario;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdCalificacion() {
        return idCalificacion;
    }

    public void setIdCalificacion(int idCalificacion) {
        this.idCalificacion = idCalificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
