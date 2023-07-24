package fr.malibu.assistants.jws.presentation.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import fr.malibu.assistants.jws.data.model.Game;
import fr.malibu.assistants.jws.domain.service.GameService;
import fr.malibu.assistants.jws.errors.InvalidDataError;
import fr.malibu.assistants.jws.errors.NotFoundError;
import fr.malibu.assistants.jws.presentation.request.CreateGameRequest;
import fr.malibu.assistants.jws.presentation.request.JoinGameRequest;
import fr.malibu.assistants.jws.presentation.request.MovePlayerRequest;
import fr.malibu.assistants.jws.presentation.response.GameDetailResponse;
import fr.malibu.assistants.jws.presentation.response.GameListResponse;
import fr.malibu.assistants.jws.utils.exception.BadRequestException;
import fr.malibu.assistants.jws.utils.exception.InternalServerErrorException;
import fr.malibu.assistants.jws.utils.exception.NotFoundException;
import fr.malibu.assistants.jws.utils.types.PaginationResult;
import fr.malibu.assistants.jws.utils.types.Result;

@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameEndpoint {

    // inject the service here gameService

    @Inject
    private GameService gameService;

    // get the list of games
    @GET()
    public Response getGames() {
        Result<PaginationResult<Game>> result = this.gameService.listGames().get();

        if (result.isFailure()) {
            if (result.getError() instanceof NotFoundError) {
                throw new NotFoundException(result.getError());
            } else if (result.getError() instanceof InvalidDataError) {
                throw new BadRequestException(result.getError());
            } else {
                throw new InternalServerErrorException();
            }
        }
        ;

        Game[] games = result.getData().getData().toArray(new Game[0]);

        return Response.ok(GameListResponse.fromModel(games)).build();

    }

    // give me code create a new game using CreateGameRequest
    @POST()
    public Response createGame(CreateGameRequest request) {
        Result<Game> result = this.gameService.createGame(request.getName()).get();
        if (result.isFailure()) {
            if (result.getError() instanceof NotFoundError) {
                throw new NotFoundException(result.getError());
            } else if (result.getError() instanceof InvalidDataError) {
                throw new BadRequestException(result.getError());
            } else {
                throw new InternalServerErrorException();
            }
        }
        Game game = result.getData();
        // response wiith game detail response and message "Game created"
        return Response.status(Response.Status.CREATED).entity(GameDetailResponse.fromModel(game)).build();
    }

    // get the detail of a game
    @GET
    @Path("/{gameId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGame(@PathParam("gameId") Long gameId) {
        Result<Game> result = this.gameService.getGame(gameId).get();
        if (result.isFailure()) {
            if (result.getError() instanceof NotFoundError) {
                throw new NotFoundException(result.getError());
            } else if (result.getError() instanceof InvalidDataError) {
                throw new BadRequestException(result.getError());
            } else {
                throw new InternalServerErrorException();
            }
        }

        return Response.ok(GameDetailResponse.fromModel(result.getData())).build();
    }

    // join a game
    @POST
    @Path("/{gameId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response joinGame(@PathParam("gameId") Long gameId, JoinGameRequest request) {
        Result<Game> result = this.gameService.joinGame(gameId, request.getName()).get();
        if (result.isFailure()) {
            if (result.getError() instanceof NotFoundError) {
                throw new NotFoundException(result.getError());
            } else if (result.getError() instanceof InvalidDataError) {
                throw new BadRequestException(result.getError());
            } else {
                throw new InternalServerErrorException();
            }
        }

        return Response.ok(GameDetailResponse.fromModel(result.getData())).build();
    }

    // move a player
    @POST
    @Path("/{gameId}/players/{playerId}/move")
    @Produces(MediaType.APPLICATION_JSON)
    public Response movePlayer(@PathParam("gameId") Long gameId, @PathParam("playerId") Long playerId,
            MovePlayerRequest request) {
        Result<Game> result = this.gameService.movePlayer(gameId, playerId, request.getPosX(), request.getPosY()).get();
        if (result.isFailure()) {
            if (result.getError() instanceof NotFoundError) {
                throw new NotFoundException(result.getError());
            } else if (result.getError() instanceof InvalidDataError) {
                throw new BadRequestException(result.getError());
            } else {
                throw new InternalServerErrorException();
            }
        }

        return Response.status(Response.Status.ACCEPTED).entity(GameDetailResponse.fromModel(result.getData())).build();
    }

    // put a bomb
    @POST
    @Path("/{gameId}/players/{playerId}/bomb")
    @Produces(MediaType.APPLICATION_JSON)
    public Response putBomb(@PathParam("gameId") Long gameId, @PathParam("playerId") Long playerId,
            MovePlayerRequest request) {
        Result<Game> result = this.gameService.placeBomb(gameId, playerId, request.getPosX(), request.getPosY()).get();
        if (result.isFailure()) {
            if (result.getError() instanceof NotFoundError) {
                throw new NotFoundException(result.getError());
            } else if (result.getError() instanceof InvalidDataError) {
                throw new BadRequestException(result.getError());
            } else {
                throw new InternalServerErrorException();
            }
        }

        return Response.status(Response.Status.ACCEPTED).entity(GameDetailResponse.fromModel(result.getData())).build();
    }

    // start a game
    @PATCH
    @Path("/{gameId}/start")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startGame(@PathParam("gameId") Long gameId) {
        Result<Game> result = this.gameService.startGame(gameId).get();
        if (result.isFailure()) {
            if (result.getError() instanceof NotFoundError) {
                throw new NotFoundException(result.getError());
            } else if (result.getError() instanceof InvalidDataError) {
                throw new BadRequestException(result.getError());
            } else {
                throw new InternalServerErrorException();
            }
        }

        return Response.ok(GameDetailResponse.fromModel(result.getData())).build();
    }

}
