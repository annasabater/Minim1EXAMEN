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
        if (gm.sizeJuegos()==0){
            this.gm.crearJuego(1, 5);
            this.gm.crearJuego(2, 3);
            this.gm.crearJuego(1, 20);
        }
        //Si no hay usuarios, crea algunos
        if (gm.sizeUsuarios()==0){
            this.gm.addUsuario("Sara");
            this.gm.addUsuario("Anna");
        }
        //Si no hay productos, crea algunos
        if (gm.sizeProducto() == 0) {
            this.gm.crearProducto("1", "Camisera", 25);
            this.gm.crearProducto("2", "Pantalon", 50);
            this.gm.crearProducto("3", "Chaleco", 80);
        }
    }
    @POST
    @ApiOperation(value = "Create Game", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Game created", response = Juego.class),
            @ApiResponse(code = 400, message = "Invalid request"),
    })
    @Path("/juegos/crearJuego")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearJuego(@QueryParam("N") int N, @QueryParam("P") int P) {
        Juego juego = gm.crearJuego(N, P);
        if (juego == null) {
            return Response.status(400).build();
        }
        return Response.status(201).entity(juego).build();
    }


    @GET
    @ApiOperation(value = "crear usuario", notes = " ")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created", response = Usuario.class),
            @ApiResponse(code = 404, message = "Invalid request"),
    })
    @Path("/usuario/crearUsuario")
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearUsuario(Usuario request) {
        Usuario usuario = new Usuario(request.getIdUsuario(), request.getNombreUsuario(), request.getApellidoUsuario(), request.getDsaCoins());
        this.gm.crearUsuario(usuario.getIdUsuario(), usuario.getNombreUsuario(), usuario.getApellidoUsuario());
        logger.info("Usuario creado con id " + usuario.getIdUsuario());
        return Response.status(201).entity(usuario).build();
    }

    @PUT
    @ApiOperation(value = "Create product", notes = " ")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Product created", response = Producto.class),
            @ApiResponse(code = 404, message = "Invalid request"),
    })
    @Path("/productos/crearProducto")
    public Response crearProducto(@QueryParam("id") String id, @QueryParam("descripcion") String descripcion, @QueryParam("precio") int precio) {
        if (this.gm.getProducto(id) != null) {
            logger.warn("Ya existe un producto con id " + id);
            return Response.status(400).build();
        }
        this.gm.crearProducto(id, descripcion, precio);
        logger.info("Producto creado con id " + id);
        return Response.status(201).entity(this.gm.getProducto(id)).build();
    }
/*
    @GET
    @ApiOperation(value = "get actual points", notes = "Get the actual points of a player")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "All good", response = String.class, responseContainer="List"),
            @ApiResponse(code = 404, message = "Player or game does not exists"),
    })
    @Path("/usuario/{idU}/getNumNivelActual")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNumNivelActual(@PathParam("idU") String idU) {
        int lvl = this.gm.getNumNivellActual(idU);
        if (lvl>=0){
            return Response.status(201).entity(lvl).build();
        }
        return Response.status(404).build();
    }

    @GET
    @ApiOperation(value = "get actual points", notes = "Get the actual points of a player")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "All good", response = String.class, responseContainer="List"),
            @ApiResponse(code = 404, message = "Player or game does not exists"),
    })
    @Path("/usuario/{idU}/getNumPuntos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNumPuntos(@PathParam("idU") String idU) {
        int puntos = Integer.parseInt(this.gm.getNumPuntos(idU));
        if (puntos >=0){
            return Response.status(201).entity(puntos).build();
        }
        return Response.status(404).build();
    }

    @PUT
    @ApiOperation(value = "Advance lvl", notes = "Make the user move 1 lvl")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Next lvl"),
            @ApiResponse(code = 202, message = "Game ended"),
            @ApiResponse(code = 404, message = "Player/game does not exist")
    })
    @Path("/usuario/pasarNivel/¨{idU}/{puntosAcumulados}/{fechaInicio}")
    public Response pasarNivel(@PathParam("idU") String idU, @PathParam("puntosAcumulados") int puntosAcumulados, @PathParam("fechaInicio")String fechaInicio) {
        Usuario u = this.gm.pasarNivel(idU,puntosAcumulados,fechaInicio);
        if(u==null){
            return Response.status(404).build();
        }
        if (u.getJugando()==true){
            return Response.status(200).build();
        }
        return Response.status(202).build();
    }

    @PUT
    @ApiOperation(value = "End a match", notes = "End a match")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Match ended"),
            @ApiResponse(code = 404, message = "Player/match does not exist"),
    })
    @Path("/usuario/finalizarPartida/{idU}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response finalizarPartida(@PathParam("idU") String id) {
        Usuario u = this.gm.finalizarPartida(id);
        if (u!=null){
            return Response.status(201).build();
        }
        return Response.status(404).build();
    }

    @GET
    @ApiOperation(value = "get users by game", notes = "Get the users that have played a specific game")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "All good", response = Usuario.class, responseContainer="List"),
            @ApiResponse(code = 404, message = "Game does not exists"),
    })
    @Path("/juego/{juego}/usuarios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarUsuariosPorJuego(@PathParam("juego") String juego) {
        Juego juegoObj = new Juego(); // create the game object from the string parameter
        List<Usuario> usuarios = (List<Usuario>) this.gm.consultarUsuariosPorJuego(juegoObj);
        if (usuarios == null) {
            return Response.status(404).build();
        }
        return Response.status(201).entity(usuarios).build();
    }

    @GET
    @ApiOperation(value = "Get player's match", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "All good", response = Producto.class, responseContainer="List"),
            @ApiResponse(code = 404, message = "Something went wrong")
    })
    @Path("/usuario/{nomUsuario}/partidaUsuario")
    @Produces(MediaType.APPLICATION_JSON)
    public Response partidaUsuario(@PathParam("nomUsuario") String id) {
        List<Producto> matches = this.gm.partidaUsuario(id);
        if(matches.size()!=0){
            GenericEntity<List<Producto>> entity = new GenericEntity<List<Producto>>(matches){};

            return Response.status(201).entity(entity).build();
        }
        return Response.status(404).build();
    }

    @GET
    @ApiOperation(value = "get activity information", notes = "Get the activity information of a player in a specific game")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "All good", response = String.class, responseContainer="List"),
            @ApiResponse(code = 404, message = "Player or game does not exists"),
    })
    @Path("/usuario/{nomUsuario}/juego/{juego}/infoActividad")
    @Produces(MediaType.APPLICATION_JSON)
    public Response infoActividad(@PathParam("nomUsuario") String nomUsuario, @PathParam("juego") String juego) {
        Juego juegoObj = new Juego();
        List<String> infoActividad = this.gm.infoActividad(nomUsuario, juegoObj);
        if (infoActividad == null) {
            return Response.status(404).build();
        }
        return Response.status(201).entity(infoActividad).build();
    }
*/
}