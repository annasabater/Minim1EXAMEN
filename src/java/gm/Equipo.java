package gm;

import java.util.ArrayList;
import java.util.List;

public class Equipo {
    private int id;
    private List<Usuario> jugadores;
    private int vida;

    public Equipo(int id, int p) {
        this.id = id;
        this.jugadores = new ArrayList<>();
        this.vida = 0;
    }
    public Equipo() {
    }

    public int getId() {
        return id;
    }

    public List<Usuario> getJugadores() {
        return jugadores;
    }

    public void addJugador(Usuario jugador) {
        jugadores.add(jugador);
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    @Override
    public String toString() {
        return "Equipo{" + "id=" + id + ", jugadores=" + jugadores + ", vida=" + vida + '}';
    }
}
