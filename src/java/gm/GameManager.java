package gm;

public interface GameManager {
    public Juego crearJuego(int N, int P);
    public void crearUsuario(String id, String nombre, String apellidos);
    public void crearProducto(String id, String descripcion, int precio);
    public void comprarProducto(String idProducto, String idUsuario);
    public Juego iniciarPartida(String idUsuario);
    public String consultarEstado();
    public Usuario disminuirVida(String idUsuario, int cantidad);
    public int consultarVida(String idUsuario);
    public int consultarVidaEquipo(int numEquipo);
    public void finalizarJuego(Juego juego);


    public Usuario getUser(String idUsuario);
    public Producto getProducto(String idProducto);
    public Juego getJuego(String idJuego);
    public Usuario addUsuario(String idUsuario);
    public Usuario addProducto(String idProducto);
    public int sizeUsuarios();
    public int sizeJuegos();
    public int sizeProducto();
    public void clear();
    public Juego buscarJuego(String identificadorJuego);
    public Usuario buscarUsuario(String identificadorUsuario);
    public Equipo buscarEquipo(String identificadorEquipo);
}