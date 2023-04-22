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
        this.gm.crearUsuario("1", "Kevin", "Gomez");
        this.gm.crearUsuario("2", "David", "Sabater");
    }

    @After
    public void tearDown() {
        //Tareas a realizar después de cada test
        gm.clear();
        gm = null;
    }

    @Test
    public void testCrearJuego() {
        logger.info("----- Empezamos con el test CrearJuego ------");
        logger.info("Condiciones iniciales: ");

        logger.info("ceeamos un juego con 4 equipos de 5 personas");
        this.gm.crearJuego(4, 5);
        assertEquals(1, this.gm.sizeJuegos());
    }

    @Test
    public void testCrearUsuario(){
        logger.info("----- Empezamos con el test CrearUsuario ------");
        logger.info("Condiciones iniciales: ");
        assertEquals(2,this.gm.sizeUsuarios());

        logger.info("Agregamos un usuario nuevo");
        this.gm.crearUsuario("3", "Anna", "Sabater");
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

        this.gm.crearUsuario("2", "David", "Sabater");
        this.gm.crearProducto("2", "Sudadera", 65);
        logger.info("compamos el producto al usuario con insuficientes DSaCoins");
        this.gm.comprarProducto("1", "1");
        logger.info("agregamso DsaCoins");
        this.gm.getUser("2").setDsaCoins(30);
        logger.info("compramos el producto al usuario con suficientes DSaCoins");
        this.gm.comprarProducto("3", "2");
    }
}

