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
        Juego juego = new Juego(N, P);
        juego.setEstado(Juego.INICIADO_EN_PREPARACION);
        juegosList.add(juego);
        logger.info("Juego creado con "+ N+ " equipos de" + P + "personas");
        return null;
    }

    //Añadir un usuario en el sistema
    public void crearUsuario(String id, String nombre, String apellidos){
        if (!this.usuarioHashMap.containsKey(id)) {
            this.usuarioHashMap.put(id, new Usuario(id, nombre, apellidos, 25));
            logger.info("Creamos usario con id " +id + "nombre:" +nombre+ "apellidos");
        } else {
            logger.warn("Ya existe el usuario con id " +id);
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

    //Compra de un producto por parte de un usuario
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

    // Inicio de una partida por parte de un usuario
    public Juego iniciarPartida(String idUsuario) {
        Juego juego = buscarJuego(idUsuario);
        if (juego == null) {
            logger.warn("No se ha encontrado el juego "+ idUsuario);
            return null;
        }
        if (juego.getEstado() == juego.INICIADO_EN_FUNCIONAMIENTO) {
            logger.warn("El juego"+ juego.getIdJuego()+ "INICIADO_EN_FUNCIONAMIENTO");
            return juego;
        }
        Usuario usuario = buscarUsuario(idUsuario);
        if (usuario == null) {
            logger.warn("No se ha encontrado el usuario" +idUsuario);
            return null;
        }
        int equipoCompleto = juego.getDimensionEquipo();
        if (juego.getDimensionEquipo()!=0) {
            logger.info("Se ha añadido una partida para el usuario" +idUsuario+"del juego"+juego.getIdJuego());
            return juego;
        }
        iniciarJuego(juego);
        return juego;
    }
    private void iniciarJuego(Juego juego) {
        juego.setEstado(Juego.INICIADO_EN_FUNCIONAMIENTO);
        logger.info(juego.getIdJuego() + "INICIADO_EN_FUNCIONAMIENTO");
    }

    // Consulta el estado del juego
    public String consultarEstado(){
        if (juegosList.isEmpty()) {
            return "No se ha creado ningun juegp";
        } else {
            Juego juego = juegosList.get(juegosList.size()-1);
            return "estado juego:" + juego.getEstado();
        }
    }

    //Actualización del valor de vida de un usuario
    public Usuario disminuirVida(String idUsuario, int cantidad){
        Juego juegoActual = juegosList.get(juegosList.size()- 1);
        if (juegoActual == null) {
            logger.warn("No hay juego en curso");
            return null;
        }
        Usuario usuario = usuarioHashMap.get(idUsuario);
        if (usuario == null) {
            logger.warn("No existe el usuario con ID "+ idUsuario);
            return usuario;
        }
        int nuevaVida = usuario.getVida() - cantidad;
        if (nuevaVida <= 0) {
            usuario.setVida(0);
            logger.info("Usuario "+ usuario.getNombreUsuario() +"muerto");
        } else {
            usuario.setVida(nuevaVida);
            logger.info("Vida del usuario" + usuario.getNombreUsuario()+" actualizada a " +nuevaVida);
        }
        return usuario;
    }

    // Consulta el valor de vida actual de un usuario
    public int consultarVida(String idUsuario){
        Usuario usuario = usuarioHashMap.get(idUsuario);
        if (usuario == null) {
            logger.warn("Noexiste el usario con id" + idUsuario);
            return -1;
        }
        Equipo eq = usuario.getEquipo();
        if (eq == null) {
            logger.warn("Noexiste el usario con id" + idUsuario);
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
            logger.warn("No hay un juego en marcha");
            return -1;
        }
        int vidaUsuario = eq.getVida();
        return vidaUsuario;
    }

    // Consulta el valor de vida de un equipo
    public int consultarVidaEquipo(int idEquipo) {
        Equipo equipo = buscarEquipo(String.valueOf(idEquipo));
        if (equipo == null) {
            logger.warn("equipo no encontrado" + idEquipo);
            return 0;
        }
        int vidaEquipo = 0;
        for (Equipo eq : equipo.getJugadores()) {
            vidaEquipo += eq.getVida();
        }
        logger.info("Vida equipo" + idEquipo +vidaEquipo);
        return vidaEquipo;
    }

    //Finalizar el juego
    public void finalizarJuego(Juego juego){
        juego.setEstado(Juego.FINALIZADO);
        logger.info(juego.getIdJuego() + "FINALIZADO");
    }

    @Override
    public Usuario addUsuario(String idUsuario){
        if(getUser(idUsuario)==null){
            logger.warn("El usuario no existe, lo añadimos");
            return getUser(idUsuario);
        }
        return null;
    }

    public Usuario addProducto(String idProducto){
        if(getUser(idProducto)==null){
            logger.warn("El producto no existe, lo añadimos");
            return getUser(idProducto);
        }
        return null;
    }

    @Override
    public Usuario getUser(String idUsuario) {
        logger.info("Buscando un jugador con el siguiente id: " + idUsuario);
        if(this.usuarioHashMap.get(idUsuario)==null){
            logger.warn("Jugador no encontrado");
            return null;
        }
        return this.usuarioHashMap.get(idUsuario);
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
    public Juego getJuego(String idJuego){
        logger.info("Buscando Juego con el siguiente id: " + idJuego);
        if(this.juegosList.get(Integer.parseInt(idJuego))==null){
            logger.warn("Juego no encontrado");
            return null;
        }
        return this.juegosList.get(Integer.parseInt(idJuego));
    }

    public Juego buscarJuego(String identificadorJuego) {
        for (Juego juego : juegosList) {
            if (juego.getIdJuego().equals(identificadorJuego)) {
                return juego;
            }
        }
        return null; // Si no se encuentra el juego, se devuelve null
    }

    public Usuario buscarUsuario(String identificadorUsuario){
        return usuarioHashMap.get(identificadorUsuario);
    }
    public Equipo buscarEquipo(String identificadorEquipo){
        return equipoHashMap.get(identificadorEquipo);
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