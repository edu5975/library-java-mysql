package archivo;

import idioma.Idioma;

public class Archivo {

    int idArchivo;
    int idLibro;
    byte[] pdf;
    Idioma idioma;

    public Archivo(int idArchivo, int idLibro, byte[] pdf, Idioma idioma) {
        this.idArchivo = idArchivo;
        this.idLibro = idLibro;
        this.pdf = pdf;
        this.idioma = idioma;
    }

    public Archivo(int idLibro, byte[] pdf, Idioma idioma) {
        this.idLibro = idLibro;
        this.pdf = pdf;
        this.idioma = idioma;
    }

    public Archivo() {
    }

    public int getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }
}
