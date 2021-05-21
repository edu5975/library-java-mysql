package autores;

import autor.Autor;
import libro.Libro;

public class Autores {
    Autor autor;
    Libro libro;

    public Autores(Autor autor, Libro libro) {
        this.autor = autor;
        this.libro = libro;
    }

    public Autores() {
    }

    public Autores(Autor autor) {
        this.autor = autor;

    }

    public Autores(Libro libro) {
        this.libro = libro;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}
