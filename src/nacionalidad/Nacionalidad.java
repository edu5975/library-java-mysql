package nacionalidad;

public class Nacionalidad {
    String idNacionalidad, descripcion;

    public Nacionalidad() {
    }

    public Nacionalidad(String idNacionalidad) {
        this.idNacionalidad = idNacionalidad;
    }

    public Nacionalidad(String idNacionalidad, String descripcion) {
        this.idNacionalidad = idNacionalidad;
        this.descripcion = descripcion;
    }

    public String getIdNacionalidad() {
        return idNacionalidad;
    }

    public void setIdNacionalidad(String idNacionalidad) {
        this.idNacionalidad = idNacionalidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return idNacionalidad + "-" + descripcion;
    }
}
