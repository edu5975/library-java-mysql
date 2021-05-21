package libro;
import autor.Autor;
import categorias.Categorias;
import editorial.Editorial;
import imagen.Imagen;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.util.List;

public class Libro {

    int idLibro;
    String titulo;
    Autor autor;
    Date fechaPublicacion;
    Editorial editorial;
    int paginas;
    String contenido;
    Imagen portada;
    Object otros;

    ObservableList<Autor> autores;
    ObservableList<Categorias> categorias;
    ObservableList<Imagen> imagenes;


    public Libro(int idLibro, String titulo,Imagen portada) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.portada = portada;
    }

    public Libro(int idLibro, String titulo, Autor autor, Date fechaPublicacion, Editorial editorial, int paginas, String contenido, Imagen portada) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.autor = autor;
        this.fechaPublicacion = fechaPublicacion;
        this.editorial = editorial;
        this.paginas = paginas;
        this.contenido = contenido;
        this.portada = portada;
    }

    public Libro(int idLibro, String titulo, Autor autor, Date fechaPublicacion, Editorial editorial, int paginas, String contenido) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.autor = autor;
        this.fechaPublicacion = fechaPublicacion;
        this.editorial = editorial;
        this.paginas = paginas;
        this.contenido = contenido;
    }

    public Libro(int idLibro, String titulo, Autor autor, Date fechaPublicacion, Editorial editorial, int paginas) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.autor = autor;
        this.fechaPublicacion = fechaPublicacion;
        this.editorial = editorial;
        this.paginas = paginas;
    }

    public Libro(int idLibro, String titulo, Date fechaPublicacion, Editorial editorial, int paginas, String contenido) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.editorial = editorial;
        this.paginas = paginas;
        this.contenido = contenido;
    }

    public Libro(int idLibro, String titulo) {
        this.idLibro = idLibro;
        this.titulo = titulo;
    }

    public Libro(int idLibro) {
        this.idLibro = idLibro;
    }

    public Libro() {
    }

    public Object getOtros() {
        return otros;
    }

    public void setOtros(Object otros) {
        this.otros = otros;
    }

    public ObservableList<Autor> getAutores() {
        return autores;
    }

    public void setAutores(ObservableList<Autor> autores) {
        this.autores = autores;
    }

    public ObservableList<Categorias> getCategorias() {
        return categorias;
    }

    public void setCategorias(ObservableList<Categorias> categorias) {
        this.categorias = categorias;
    }

    public ObservableList<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ObservableList<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public Imagen getPortada() {
        return portada;
    }

    public void setPortada(Imagen portada) {
        this.portada = portada;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    @Override
    public String toString() {
        return idLibro + "-" + titulo;
    }
}
