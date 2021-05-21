package categoria;

public class Subcategorias {
    int idsubcat = -1,idcat;
    String Nombre,descripcion;
    String categoria;

    public Subcategorias(int idsubcat, String descripcion, String categoria, int idcat) {
        this.idsubcat = idsubcat;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.idcat=idcat;
    }

    public Subcategorias(int idsubcat, String descripcion) {
        this.idsubcat = idsubcat;
        this.descripcion = descripcion;
    }

    public Subcategorias( int idcat, int idsubcat, String descripcion) {
        this.idsubcat = idsubcat;
        this.idcat = idcat;
        this.descripcion = descripcion;
    }



    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getIdsubcat() {
        return idsubcat;
    }

    public void setIdsubcat(int idsubcat) {
        this.idsubcat = idsubcat;
    }

    public int getIdcat() {
        return idcat;
    }

    public void setIdcat(int idcat) {
        this.idcat = idcat;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
