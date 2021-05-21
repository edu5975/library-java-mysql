package idioma;

public class Idioma {

    String idIdioma, descripcion;

    public Idioma(String idIdioma, String descripcion) {
        this.idIdioma = idIdioma;
        this.descripcion = descripcion;
    }

    public Idioma() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdIdioma() {
        return idIdioma;
    }

    public void setIdIdioma(String idIdioma) {
        this.idIdioma = idIdioma;
    }

    @Override
    public String toString() {
        return idIdioma + "-" + descripcion;
    }
}
