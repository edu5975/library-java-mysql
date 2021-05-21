package calificacion;

public class Calificacion {

    int idCalificacion;
    String descripcion;

    Calificacion(int idCalificacion, String descripcion) {
        this.idCalificacion = idCalificacion;
        this.descripcion = descripcion;
    }

    public int getIdCalificacion() {
        return idCalificacion;
    }

    public void setIdCalificacion(int idCalificacion) {
        this.idCalificacion = idCalificacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
