package gm;

import org.apache.log4j.Logger;
import java.util.*;

public class GameManagerImpl implements GameManager {
    private HashMap<String, Usuario> usuarioHashMap;
    private HashMap<String, Equipo> equipoHashMap;
    private HashMap<String,Producto> productoHashMap;
    private List<Juego> juegosList;

    public static GameManagerImpl instance = null;
    public static final Logger logger = Logger.getLogger(GameManagerImpl.class);

    private GameManagerImpl() {
        usuarioHashMap = new HashMap<>();
        equipoHashMap = new HashMap<>();
        productoHashMap = new HashMap<>();
        juegosList = new ArrayList<>();
    }

    public static GameManagerImpl getInstance() {
        if (instance == null) {
            instance = new GameManagerImpl();
        }
        return instance;
    }

    public static void setInstance(GameManagerImpl instance) {
        GameManagerImpl.instance = instance;
    }

    @Override
    //Creación del juego basado en N equipos de P personas.
    public Juego crearJuego(int N, int P) {
        if (!juegosList.isEmpty()) {
            logger.warn("Ya hay un juego en marcha");
            return null;
        }
        for (int i = 0; i < N; i++) {
            equipoHashMap.put(String.valueOf(i), new Equipo(i, P));
        }
        juegosList.add(new Juego(N, P));
        logger.info("Juego creado con " + N + " equipos de " + P + " personas cada uno");
        return null;
    }

    //Añadir un usuario en el sistema
    public void crearUsuario(String id, String nombre, String apellidos){
        if (!this.usuarioHashMap.containsKey(id)) {
            this.usuarioHashMap.put(id, new Usuario(id, nombre, apellidos, 25));
            logger.info("Creamos usario con id " + id );
        } else {
            logger.warn("Ya existe el usuario con id " + id);
        }
    }

    //Añadir un producto (objecto) en la tienda.
    public void crearProducto(String id, String descripcion, int precio){
        if (!this.productoHashMap.containsKey(id)) {
            this.productoHashMap.put(id, new Producto(id, descripcion, precio));
            logger.info("Se ha creado un producto con la sguiente id " + id);
        } else {
            logger.warn("Ya existe producto con id " + id);
        }
    }
    public void comprarProducto(String idProducto, String idUsuario) {
        if (productoHashMap.containsKey(idProducto) && usuarioHashMap.containsKey(idUsuario)) {
            Usuario usuario = usuarioHashMap.get(idUsuario);
            Producto producto = productoHashMap.get(idProducto);
            int precio = producto.getPrecio();
            int dsaCoins = usuario.getDsaCoins();
            if (dsaCoins >= precio) {
                usuario.setDsaCoins(dsaCoins - precio);
                logger.info("Compra con exito");
            } else {
                logger.warn("No hay  suficientes DsaCoins");
            }
        }else {
            logger.warn("No se enccontro el producto o el usuario");
        }
    }
    public void iniciarPartida(String idUsuario) {
        //FALTA
    }
    public String consultarEstado(){
        if (juegosList.isEmpty()) {
            return "No se ha creado ningun juegp";
        } else {
            Juego juego = juegosList.get(juegosList.size() - 1);
            return "estado juego: " + juego.getEstado();
        }
    }

    public void disminuirVida(String idUsuario, int cantidad){
        Usuario usuario = usuarioHashMap.get(idUsuario);
        if (usuario == null) {
            logger.warn("no existe eñ usuario con ID " + idUsuario);
        }
    }
    public int consultarVida(String idUsuario){ //MIRAR
        Usuario usuario = usuarioHashMap.get(idUsuario);
        if (usuario == null) {
            logger.warn("No existe usuario " + idUsuario);
            return -1;
        }
        Equipo equipoUsuario = null;
        if (equipoUsuario == null) {
            logger.warn("Noexiste el usario con id " + idUsuario);
            return -1;
        }
        Juego juegoActivo = null;
        for (Juego juego : juegosList) {
            if (juego.getEstado() == Juego.INICIADO_EN_FUNCIONAMIENTO) {
                juegoActivo = juego;
                break;
            }
        }
        if (juegoActivo == null) {
            logger.warn("No hay un juego activo.");
            return -1;
        }
        int vidaUsuario = equipoUsuario.getVida(usuario);
        return vidaUsuario;
    }
    public int consultarVidaEquipo(int numEquipo){ //MIRAR
        return numEquipo;
    }
    public void finalizarJuego(){
        //FALTA
    }


    @Override
    public Usuario getUser(String idUser) {
        logger.info("Buscando un jugador con el siguiente id: " + idUser);
        if(this.usuarioHashMap.get(idUser)==null){
            logger.warn("Jugador no encontrado");
            return null;
        }
        return this.usuarioHashMap.get(idUser);
    }
    @Override
    public Usuario addUsuario(String idUser){
        if(getUser(idUser)==null){
            logger.warn("El usuario no existe, lo añadimos");
            return getUser(idUser);
        }
        return null;
    }

    @Override
    public Producto getProducto(String idProducto) {
        logger.info("Buscando producto con el siguiente id: " + idProducto);
        if(this.productoHashMap.get(idProducto)==null){
            logger.warn("Producto no encontrado");
            return null;
        }
        return this.productoHashMap.get(idProducto);
    }


    @Override
    public void clear(){
        equipoHashMap.clear();
        productoHashMap.clear();
        usuarioHashMap.clear();
        juegosList.clear();
    }

    @Override
    public int sizeUsuarios(){
        int ret = this.usuarioHashMap.size();
        logger.info(ret + " usuario en la lista");
        return ret;
    }
    @Override
    public int sizeJuegos(){
        int ret = this.juegosList.size();
        logger.info(ret + " juegos en la lista");
        return ret;
    }
    public int sizeProducto(){
        int ret = this.productoHashMap.size();
        logger.info(ret + " productos en la lista");
        return ret;
    }
}