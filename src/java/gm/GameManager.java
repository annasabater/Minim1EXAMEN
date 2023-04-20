package gm;

public interface GameManager {
  //  public void addJuego (Juego juego);
    public Juego crearJuego(int N, int P);
    public void crearUsuario(String id, String nombre, String apellidos);
    public void crearProducto(String id, String descripcion, int precio);
    public void comprarProducto(String idProducto, String idUsuario);
    public void iniciarPartida(String idUsuario);
    public String consultarEstado();
    public void disminuirVida(String idUsuario, int cantidad);
    public int consultarVida(String idUsuario);
    public int consultarVidaEquipo(int numEquipo);
    public void finalizarJuego();


    public Usuario getUser(String idUser);
    public Producto getProducto(String idProducto);
    public Usuario addUsuario(String idUser);
    public int sizeUsuarios();
    public int sizeJuegos();
    public int sizeProducto();
    public void clear();

}