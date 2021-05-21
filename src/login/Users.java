package login;

public class Users {

    String iduser, contraseña, tipoUsuario;

    public Users(String iduser, String contraseña) {
        this.iduser = iduser;
        this.contraseña = contraseña;
    }

    public Users(String iduser, String contraseña, String tipoUsuario) {
        this.iduser = iduser;
        this.contraseña = contraseña;
        this.tipoUsuario = tipoUsuario;
    }

    public Users() {
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @Override
    public String toString() {
        return "login{" +
                "iduser='" + iduser + '\'' +
                ", contraseña='" + contraseña + '\'' +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                '}';
    }
}
