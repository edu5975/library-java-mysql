package tipoUsuario;

public class TipoUsuario {
    int idTipoU;
    String descripcion;

    public TipoUsuario(int idTipoU, String descripcion) {
        this.idTipoU = idTipoU;
        this.descripcion = descripcion;
    }

    public TipoUsuario() {
    }

    public int getIdTipoU() {
        return idTipoU;
    }

    public void setIdTipoU(int idTipoU) {
        this.idTipoU = idTipoU;
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
