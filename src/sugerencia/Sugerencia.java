package sugerencia;

public class Sugerencia {

    int idSugerencia;
    String idUsuario;
    String tituloLibro;
    String autorLibro;
    String contenido;
    byte[] pdf;

    public Sugerencia() {
    }

    public Sugerencia(int idSugerencia, String idUsuario, String tituloLibro, String autorLibro, String contenido, byte[] pdf) {
        this.idSugerencia = idSugerencia;
        this.idUsuario = idUsuario;
        this.tituloLibro = tituloLibro;
        this.autorLibro = autorLibro;
        this.contenido = contenido;
        this.pdf = pdf;
    }

    public int getIdSugerencia() {
        return idSugerencia;
    }

    public void setIdSugerencia(int idSugerencia) {
        this.idSugerencia = idSugerencia;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getAutorLibro() {
        return autorLibro;
    }

    public void setAutorLibro(String autorLibro) {
        this.autorLibro = autorLibro;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }
}
