package gm.services;

import gm.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static gm.GameManagerImpl.logger;

/**
 * Root resource (exposed at "myresource" path)
 */
@Api(value = "/juego", description = "Endpoint to Game Service")
@Path("game")
public class GameService {
    private GameManager gm;

    public GameService() {
        this.gm = GameManagerImpl.getInstance();
        //Si no hay juegos, crea algunos
        if (gm.sizeJuegos() == 0) {
            this.gm.crearJuego(1, 5);
            this.gm.crearJuego(2, 3);
        }
        //Si no hay usuarios, crea algunos
        if (gm.sizeUsuarios() == 0) {
            this.gm.addUsuario("Kevin");
            this.gm.addUsuario("David");
        }
        //Si no hay productos, crea algunos
        if (gm.sizeProducto() == 0) {
            this.gm.crearProducto("1", "Camisera", 20);
            this.gm.crearProducto("2", "Sudadera", 23);
        }
    }

    @POST
    @ApiOperation(value = "Crear juego", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Juego creado", response = Juego.class),
            @ApiResponse(code = 400, message = "No creado"),
    })
    @Path("/juego/crearJuego/{N}/{P}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearJuego(@PathParam("N") int N, @PathParam("P") int P) {
        Juego juego = this.gm.crearJuego(N, P);
        if (juego == null) {
            return Response.status(400).build();
        }
        return Response.status(200).entity(juego).build();
    }

    @POST
    @ApiOperation(value = "Crear usuario", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario creado", response = Usuario.class),
            @ApiResponse(code = 400, message = "No creado"),
    })
    @Path("/usuario/crearUsuario/{id}/{nombre}/{apellidos}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearUsuario(@PathParam("id") String id, @PathParam("nombre") String nombre, @PathParam("apellidos") String apellidos) {
        this.gm.crearUsuario(id,nombre, apellidos);
        Usuario usuario = this.gm.getUser(id);
        if (usuario == null) {
            return Response.status(400).build();
        }
        return Response.status(200).entity(usuario).build();
    }

    @PUT
    @ApiOperation(value = "Crear producto", notes = " ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Producto creado", response = Producto.class),
            @ApiResponse(code = 400, message = "No creado"),
    })
    @Path("/productos/crearProducto/{id}/{descripcion}/{precio}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearProducto(@PathParam("id") String id, @PathParam("descripcion") String descripcion, @PathParam("precio") int precio) {
        if (this.gm.getProducto(id) != null) {
            logger.warn("Ya existe un producto con id " +id);
            return Response.status(400).build();
        }
        this.gm.crearProducto(id, descripcion, precio);
        logger.info("Producto creado con id" + id+ "descripcon" +descripcion+ "precio" +precio);
        return Response.status(200).entity(this.gm.getProducto(id)).build();
    }

    @PUT
    @ApiOperation(value = "Comprar producto", notes = " ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Compra exha"),
            @ApiResponse(code = 400, message = "BAD_REQUEST"),
            @ApiResponse(code = 404, message = "NOT_FOUND")
    })
    @Path("/productos/comprarProducto/{idProducto}/{idUsuario}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response comprarProducto(@PathParam("idProducto") String idProducto, @PathParam("idUsuario") String idUsuario) {
        if (this.gm.getProducto(idProducto)==null||this.gm.getUser(idUsuario)==null) {
            logger.warn("No encontrdo");
            return Response.status(404).build();
        }
        //comprovamos si el usuario tiene suficientes DSACOINS para comprar el producto
        Usuario usuario = this.gm.getUser(idUsuario);
        Producto producto = this.gm.getProducto(idProducto);
        int precio = producto.getPrecio();
        int dsaCoins= usuario.getDsaCoins();
        if (dsaCoins>=precio) {
            usuario.setDsaCoins(dsaCoins-precio);
            logger.info("Compra echa");
            return Response.status(200).build();
        } else {
            logger.warn("insuficiente DsaCoins");
            return Response.status(400).build();
        }
    }

    @POST
    @ApiOperation(value = "Iniciar partida", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Partida iniciada", response = Juego.class),
            @ApiResponse(code = 400, message = "BAD_REQUEST"),
            @ApiResponse(code = 404, message = "NOT_FOUND")
    })
    @Path("/juego/iniciarPartida/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response iniciarPartida(@PathParam("idUsuario") String idUsuario) {
        Juego juego= this.gm.iniciarPartida(idUsuario); //cridem al metodo iniciarParitda del tipo Juego
        if (juego == null) {
            return Response.status(404).build();
        } else if (juego.getEstado()==Juego.INICIADO_EN_FUNCIONAMIENTO) { //Juego iniciado
            return Response.status(400).entity("El juego" + juego.getIdJuego() + "funciona").build();
        }
        return Response.status(200).build();
    }

    @GET
    @ApiOperation(value = "Consultar estado", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Estado consultado"),
            @ApiResponse(code = 404, message = "NOT_FOUND")
    })
    @Path("/juegos/consultarEstado")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarEstado() {
        if (this.gm.sizeJuegos()== 0) {//miramos si hay algun juego creado
            return Response.status(404).entity("No se crra ningun juego").build();
        } else {
            String estado=this.gm.consultarEstado(); //devuelve el estado el juego
            return Response.status(200).entity("Estado juego:" + estado).build();
        }
    }

    @PUT
    @ApiOperation(value = "Disminuir vida usuario", notes = " ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "NOT_FOUND")
    })
    @Path("/usuario/disminuirVida/{idUsuario}/{cantidad}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response disminuirVida(@PathParam("idUsuario") String idUsuario, @PathParam("cantidad") int cantidad) {
        Usuario usuario= this.gm.getUser(idUsuario);
        if (usuario == null) {
            logger.warn("No existe el usuario con ID " + idUsuario);
            return Response.status(404).build();
        }
        this.gm.disminuirVida(idUsuario, cantidad);
        int nuevaVida=usuario.getVida() - cantidad;
        if (nuevaVida<=0) { //establecemos la vida del usuario en 0
            usuario.setVida(0);
            logger.info("Usuario " + usuario.getNombreUsuario() + "muerto");
        } else {
            usuario.setVida(nuevaVida); //actualiza vida usuario
            logger.info("Vida usuario " +usuario.getNombreUsuario()+"actualizada" +nuevaVida);
        }
        return Response.status(200).build();
    }

    @GET
    @ApiOperation(value = "Consultar vida", notes = " ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario encontrado"),
            @ApiResponse(code = 404, message = "NOT_FOUND")
    })
    @Path("/usuario/consultarVida/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarVida(@PathParam("idUsuario") String idUsuario) {
        int vida = this.gm.consultarVida(idUsuario); //obtenemos la vida del usuario
        if (vida == -1) {// si no existe NOTFOUND
            return Response.status(404).build();
        }
        return Response.status(200).entity(vida).build();
    }

    @GET
    @ApiOperation(value = "Consultar vida  equipo", notes = " ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "NOT_FOUND")
    })
    @Path("/equipo/consultarVida/{idEquipo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarVidaEquipo(@PathParam("idEquipo") int idEquipo) {
        int vidaEquipo=this.gm.consultarVidaEquipo(idEquipo);//obtenemos la vida del equipo
        if (vidaEquipo==0) {
            logger.warn("No se ha encontrado el equipo" + idEquipo);
            return Response.status(404).build();
        }
        logger.info("El valor de vida del equipo " + idEquipo + "es" + vidaEquipo);
        return Response.status(200).entity(vidaEquipo).build();
    }

    @PUT
    @ApiOperation(value = "Finalizar juego", notes = " ")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "NOT_FOUND")
    })
    @Path("/juego/finalizarJuego")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response finalizarJuego(Juego juego) {
        Juego juegoExistente = this.gm.buscarJuego(juego.getIdJuego());
        if (juegoExistente == null) {
            logger.warn("No se ha encontrado el juego " +juego.getIdJuego());
            return Response.status(404).build();
        }
        this.gm.finalizarJuego(juego);
        logger.info("El juego "+juego.getIdJuego()+"ha finalizado");
        return Response.status(200).build();
    }
}
