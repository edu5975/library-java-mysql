package imagen;

import javafx.scene.image.Image;

public class Imagen {

    int idImagen;
    int idLibro;
    Image imagen;
    String descripcion;
    String ruta;

    public Imagen(int idImagen, int idLibro, Image imagen, String descripcion, String ruta) {
        this.idImagen = idImagen;
        this.idLibro = idLibro;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.ruta = ruta;
    }

    public Imagen(int idImagen, int idLibro, Image imagen, String descripcion) {
        this.idImagen = idImagen;
        this.idLibro = idLibro;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public Imagen(int idLibro, Image imagen, String descripcion) {
        this.idLibro = idLibro;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public Imagen() {
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(int idImagen) {
        this.idImagen = idImagen;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
