package categorias;

import categoria.Subcategorias;
import libro.Libro;

public class Categorias {
    Libro libro;
    Subcategorias subcategorias;

    public Categorias(Libro libro, Subcategorias subcategorias) {
        this.libro = libro;
        this.subcategorias = subcategorias;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Subcategorias getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(Subcategorias subcategorias) {
        this.subcategorias = subcategorias;
    }
}
