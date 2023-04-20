import gm.GameManager;
import gm.GameManagerImpl;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class GameManagerTest {
    private GameManager gm;
    public static final Logger logger = Logger.getLogger(GameManager.class);

    @Before
    public void setUp() {
        //Inicialización de variables antes de cada Test
        gm = GameManagerImpl.getInstance();
        //gm.addUsuario(String.valueOf(new Usuario("1", "Sara", "1", 15)));
        //gm.addUsuario(String.valueOf(new Usuario("2", "Anna", "2", 10)));

    }
    @After
    public void tearDown() {
        //Tareas a realizar después de cada test
        gm.clear();
        gm = null;
    }
    @Test
    public void testCrearJuego() {
        logger.info("----- Empezamos con el test crearJuego ------");
        logger.info("Condiciones iniciales: ");

        logger.info("ceeamos un juego con 4 equipos de 5 pers");
        this.gm.crearJuego(4, 5);
        assertEquals(1, this.gm.sizeJuegos());
    }

    @Test
    public void testCrearUsuario(){
        logger.info("----- Empezamos con el test AddUser ------");
        logger.info("Condiciones iniciales: ");
        assertEquals(2,this.gm.sizeUsuarios());

        logger.info("Agregamos un usuario nuevo");
        this.gm.crearUsuario("1", "Anna", "Sabater");
        assertEquals(3,this.gm.sizeUsuarios());
    }

    @Test
    public void testCrearProducto() {
        logger.info("----- Empezamos con el test CrearProducto ------");
        logger.info("Condiciones iniciales: ");
        assertEquals(0, this.gm.sizeProducto());

        logger.info("Agregamos un producto nuevo");
        this.gm.crearProducto("1", "Camiseta", 20);
        assertEquals(1, this.gm.sizeProducto());

        logger.info("Agregamos otro producto");
        this.gm.crearProducto("2", "Sudadera", 23);
        assertEquals(2, this.gm.sizeProducto());
    }
    @Test
    public void testComprarProducto() {
        logger.info("----- Empezamos con el test ComprarProducto ------");
        logger.info("Condiciones iniciales: ");

        this.gm.crearUsuario("2", "Carles", "Sabater");
        this.gm.crearProducto("3", "Pantalones", 65);

        logger.info("compamos el producto al usuario con insuficientes DSaCoins");
        this.gm.comprarProducto("1", "1");
        // Verificamos que no se realizó la compra
        assertEquals(0, this.gm.getUser("2").getDsaCoins());

        logger.info("agregamso DsaCoins");
        this.gm.getUser("2").setDsaCoins(30);

        logger.info("compamos el producto al usuario con suficientes DSaCoins");
        this.gm.comprarProducto("3", "2");
        assertEquals(10, this.gm.getUser("2").getDsaCoins()); //nos aseguramos q la compra esta echa
    }
}

