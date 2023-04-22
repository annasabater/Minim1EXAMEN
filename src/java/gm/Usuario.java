package gm;

public class Usuario extends Equipo {
    private String idUsuario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private int dsaCoins;
    private Equipo equipo;

    public Usuario(String idUsuario, String nombreUsuario, String apellidoUsuario, int dsaCoins){
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.dsaCoins = 25;

    }
    public Usuario(){
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public int getDsaCoins() {
        return dsaCoins;
    }

    public void setDsaCoins(int dsaCoins) {
        this.dsaCoins = dsaCoins;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    @Override
    public String toString() {
        return "Usuario{" + "identificador:'" + idUsuario + ", nombre:" + nombreUsuario +" apellidos:'" + apellidoUsuario + " dsaCoins:" + dsaCoins + " equipo:" + equipo + '}';
    }
}
