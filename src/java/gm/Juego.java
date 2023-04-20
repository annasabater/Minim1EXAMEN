package gm;

public class Juego{
    // Estados del juego
    private static final int NO_INICIADO = 0;
    private static final int INICIADO_EN_PREPARACION = 1;
    public static final int INICIADO_EN_FUNCIONAMIENTO = 2;
    private static final int FINALIZADO = 3;

    // Variables de instancia
    private int nEquipos;
    private int dimensionEquipo;
    private int estado;

    public Juego(int nEquipos, int dimensionEquipo) {
        this.nEquipos = nEquipos;
        this.dimensionEquipo = dimensionEquipo;
        this.estado = NO_INICIADO;
    }
    public Juego (){
    }

    public int getnEquipos() {
        return nEquipos;
    }

    public void setnEquipos(int nEquipos) {
        this.nEquipos = nEquipos;
    }

    public int getDimensionEquipo() {
        return dimensionEquipo;
    }

    public void setDimensionEquipo(int dimensionEquipo) {
        this.dimensionEquipo = dimensionEquipo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
