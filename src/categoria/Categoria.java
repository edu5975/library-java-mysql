package categoria;

public class Categoria {
    int idCat;
    String Nombre,Descripcion;

    public Categoria(int idCat, String descripcion) {
        this.idCat = idCat;
        Descripcion = descripcion;
    }

    public Categoria(){

    }

    public int getIdCat() {
        return idCat;
    }

    public void setIdCat(int idCat) {
        this.idCat = idCat;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    @Override
    public String toString() {
        return Descripcion;
    }
}
