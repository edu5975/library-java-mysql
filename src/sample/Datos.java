package sample;

public class Datos {

    private static int idLibro = 1;
    private static int idTipoU = 1;
    private static int idUsuario;
    private static String nombreUsuario = "edu5975";
    private static int tema;
    private static String css = "css/theme.css";
    private static int width=1500, height=950;
    private static boolean lista = true;

    public static boolean isLista() {
        return lista;
    }

    public static void setLista(boolean lista) {
        Datos.lista = lista;
    }

    public static String getCss() {
        return css;
    }

    public static void setCss(String css) {
        Datos.css = css;
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        Datos.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        Datos.height = height;
    }

    public static int getTema() {
        return tema;
    }

    public static void setTema(int tema) {
        Datos.tema = tema;
    }

    public static String getNombreUsuario() {
        return nombreUsuario;
    }

    public static void setNombreUsuario(String nombreUsuario) {
        Datos.nombreUsuario = nombreUsuario;
    }

    public static int getIdLibro() {
        return idLibro;
    }

    public static void setIdLibro(int idLibro) {
        Datos.idLibro = idLibro;
    }

    public static int getIdTipoU() {
        return idTipoU;
    }

    public static void setIdTipoU(int idTipoU) {
        Datos.idTipoU = idTipoU;
    }

    public static int getIdUsuario() {
        return idUsuario;
    }

    public static void setIdUsuario(int idUsuario) {
        Datos.idUsuario = idUsuario;
    }
}
