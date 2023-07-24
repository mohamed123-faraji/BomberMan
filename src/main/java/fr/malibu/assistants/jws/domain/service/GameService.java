package fr.malibu.assistants.jws.domain.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import fr.malibu.assistants.jws.converter.ConvertToModel;
import fr.malibu.assistants.jws.data.model.Game;
import fr.malibu.assistants.jws.data.model.Player;
import fr.malibu.assistants.jws.data.repository.GameRepository;
import fr.malibu.assistants.jws.data.repository.PlayerRepository;
import fr.malibu.assistants.jws.domain.entity.GameEntity;
import fr.malibu.assistants.jws.domain.entity.PlayerEntity;
import fr.malibu.assistants.jws.errors.InvalidDataError;
import fr.malibu.assistants.jws.errors.NotFoundError;
import fr.malibu.assistants.jws.utils.errors.UnknownDatabaseError;
import fr.malibu.assistants.jws.utils.types.PaginationResult;
import fr.malibu.assistants.jws.utils.types.Result;

@ApplicationScoped
public class GameService {

    @Inject
    private GameRepository gameRepository;

    @Inject
    private PlayerRepository playerRepository;

    @Inject
    @ConfigProperty(name = "jws.map.path")
    String mapUrl;

    @Inject
    @ConfigProperty(name = "JWS.DELAY.BOMB", defaultValue = "5000") // 5000 is the default value if JWS_DELAY_BOMB i //
                                                                    // not defined in properties
    private int jwsDelayBomb;

    @ConfigProperty(name = "jws.delay.movement")
    int delayMovement;

    // lists all the games registered in the database
    public Optional<Result<PaginationResult<Game>>> listGames() {
        try {
            List<GameEntity> gamesResult = gameRepository.listAll();
            List<Game> gamesData = new ArrayList<>();

            for (GameEntity game : gamesResult) {
                gamesData.add(ConvertToModel.convertToModel(game, Game.class));
            }

            PaginationResult<Game> games = new PaginationResult<>();
            games.setData(gamesData);
            Result<PaginationResult<Game>> successResult = Result.success(games);

            return Optional.of(successResult);
        } catch (Error e) {
            return Optional.of(Result.error(UnknownDatabaseError.create()));
        }
    }

    // creates a new game in the database
    @Transactional
    public Optional<Result<Game>> createGame(String playerName) {
        try {
            if (playerName == null || playerName.isEmpty()) {
                return Optional.of(Result.error(InvalidDataError.create("Bad request (request or name is null)")));
            }
            // List<GameEntity> games = gameRepository.listAll();
            // for (GameEntity game : games) {
            // for (PlayerEntity player : game.getPlayers()) {
            // if (player.getName().equals(playerName)) {
            // return Optional.of(Result.error(AlreadyExistsError.create(playerName)));
            // }
            // }
            // }
            GameEntity game = new GameEntity();
            game.setStarttime(new Timestamp(System.currentTimeMillis()));
            game.setState("STARTING");

            Player player = new Player();
            player.setLastbomb(null);
            player.setLastmovement(null);
            player.setLives(3);
            player.setName(playerName);
            player.setPosx(1); // Set initial positions as per the requirement
            player.setPosy(1);
            player.setPosition(null);
            player.setGame(game);
            game.getPlayers().add(player);

            String map = this.readRleMapFromFile();
            game.setMap(map);

            gameRepository.persist(game);
            playerRepository.persist(player);

            Game gameModel = ConvertToModel.convertToModel(game, Game.class);
            return Optional.of(Result.success(gameModel));
        } catch (Error e) {
            e.printStackTrace();
            return Optional.of(Result.error(UnknownDatabaseError.create()));
        } catch (IOException e) {
            return null;
        }
    }

    // get a game by its id
    public Optional<Result<Game>> getGame(Long gameId) {
        try {
            GameEntity game = gameRepository.findById(gameId);
            if (game == null) {
                return Optional.of(Result.error(NotFoundError.create()));
            }
            Game gameModel = ConvertToModel.convertToModel(game, Game.class);
            return Optional.of(Result.success(gameModel));
        } catch (Exception e) {
            return Optional.of(Result.error(UnknownDatabaseError.create()));
        }
    }

    @Transactional
    public Optional<Result<Game>> joinGame(Long gameId, String playerName) {
        try {
            if (playerName == null || playerName.isEmpty()) {
                return Optional
                        .of(Result.error(InvalidDataError.create("The request is null, or the player name is null")));
            }
            GameEntity game = gameRepository.findById(gameId);
            // if game state is not starting, return error
            if (!game.getState().equals("STARTING")) {
                return Optional.of(Result.error(InvalidDataError.create("the game\\\r\n" + //
                        " \\ cannot be started (already started, too many players)")));
            }

            // if game not found, return error
            if (game == null) {
                return Optional.of(Result.error(NotFoundError.create()));
            }

            Player player = new Player();
            player.setLastbomb(null);
            player.setLastmovement(null);
            player.setLives(3);
            player.setName(playerName);
            // Set initial positions based on game rules
            player.setPosx(1);
            player.setPosy(1);
            player.setPosition(null);

            player.setGame(game);
            game.getPlayers().add(player);

            playerRepository.persist(player);
            // return the game
            Game gameModel = ConvertToModel.convertToModel(game, Game.class);
            return Optional.of(Result.success(gameModel));

        } catch (Error e) {
            e.printStackTrace();
            return Optional.of(Result.error(UnknownDatabaseError.create()));
        }
    }

    // starts the game
    @Transactional
    public Optional<Result<Game>> startGame(Long gameId) {
        try {
            GameEntity game = gameRepository.findById(gameId);
            if (game == null) {
                return Optional.of(Result.error(NotFoundError.create()));
            }
            if (game.getState().equals("STARTING")) {
                game.setState("RUNNING");
                gameRepository.persist(game);
                Game gameModel = ConvertToModel.convertToModel(game, Game.class);
                return Optional.of(Result.success(gameModel));
            } else {
                return Optional.of(Result.error(InvalidDataError.create("The game cannot be started")));
            }
        } catch (Error e) {
            e.printStackTrace();
            return Optional.of(Result.error(UnknownDatabaseError.create()));
        }
    }

    // moves a player
    @Transactional
    public Optional<Result<Game>> movePlayer(Long gameId, long playerId, int posX, int posY) {
        try {
            System.out.println("move player");
            GameEntity game = gameRepository.findById(gameId);
            if (game == null) {
                return Optional.of(Result.error(NotFoundError.create()));
            }
            if (game.getState().equals("STARTING")) {
                return Optional.of(Result.error(InvalidDataError.create("The game is not running")));
            }
            PlayerEntity player = playerRepository.findById(playerId);
            if (player == null) {
                return Optional.of(Result.error(NotFoundError.create("the player is not found")));
            }
            if (player.getGame().getId() != game.getId()) {
                return Optional.of(Result.error(InvalidDataError.create("The player is not in the game")));
            }
            if (player.getLives() == 0) {
                return Optional.of(Result.error(InvalidDataError.create("The player is dead")));
            }
            if (posX < 1 || posX > 15 || posY < 1 || posY > 13) {
                return Optional.of(Result.error(InvalidDataError.create("The position is out of bounds")));
            }
            if (game.getState().equals("FINISHED")) {
                return Optional.of(Result.error(InvalidDataError.create("The game is finished")));
            }

            // Get the map
            String map = game.getMap();
            if (map == null) {
                return Optional.of(Result.error(InvalidDataError.create("The map is null")));
            }

            // Calculate the new position based on the movement
            int currentX = player.getPosx();
            int currentY = player.getPosy();
            int movementDirectionX = posX - currentX;
            int movementDirectionY = posY - currentY;
            int newX = currentX + movementDirectionX;
            int newY = currentY + movementDirectionY;

            // Split the map by newline character to get rows
            String[] mapRows = map.split("\n");

            // Remove the first line (size of the map)
            String[] dataRows = Arrays.copyOfRange(mapRows, 1, mapRows.length);

            // Check if the new position is within the bounds of the map
            if (newY < 0 || newY >= dataRows.length || newX < 0 || newX >= dataRows[newY].length()) {
                return Optional.of(Result.error(InvalidDataError.create("The player cannot move outside the map")));
            }

            // Get the character at the new position
            char mapContent = dataRows[newY].charAt(newX);

            // Check if the character at the new position is metal or wood
            if (mapContent == 'M' || mapContent == 'W') {
                return Optional.of(Result
                        .error(InvalidDataError.create("The player cannot move to this position (metal or wood)")));
            }

            // If all checks pass, update the player's position and other details
            if (game.getState().equals("RUNNING")) {
                Thread.sleep(delayMovement);

                player.setPosx(newX);
                player.setPosy(newY);
                player.setPosition(null);
                player.setLastmovement(new Timestamp(System.currentTimeMillis()));
                playerRepository.persist(player);

                Game gameModel = ConvertToModel.convertToModel(game, Game.class);
                return Optional.of(Result.success(gameModel));
            } else {
                return Optional.of(Result.error(InvalidDataError.create("The game is not running")));
            }

        } catch (Error | InterruptedException e) {
            e.printStackTrace();
            return Optional.of(Result.error(UnknownDatabaseError.create()));
        }
    }

    // place a bomb
    @Transactional
    public Optional<Result<Game>> placeBomb(Long gameId, long playerId, int posX, int posY) {
        try {
            GameEntity game = gameRepository.findById(gameId);
            if (game == null) {
                return Optional.of(Result.error(NotFoundError.create()));
            }
            if (game.getState().equals("STARTING")) {
                return Optional.of(Result.error(InvalidDataError.create("The game is not running")));
            }
            PlayerEntity player = playerRepository.findById(playerId);
            if (player == null) {
                return Optional.of(Result.error(NotFoundError.create("the player is not found")));
            }
            if (player.getGame().getId() != game.getId()) {
                return Optional.of(Result.error(InvalidDataError.create("The player is not in the game")));
            }
            if (player.getLives() == 0) {
                return Optional.of(Result.error(InvalidDataError.create("The player is dead")));
            }
            if (posX < 0 || posX > 15 || posY < 0 || posY > 13) {
                return Optional.of(Result.error(InvalidDataError.create("The position is out of bounds")));
            }
            if (game.getState().equals("FINISHED")) {
                return Optional.of(Result.error(InvalidDataError.create("The game is finished")));
            }

            if (player.getPosx() != posX || player.getPosy() != posY) {
                return Optional
                        .of(Result.error(InvalidDataError.create("You can only place a bomb where you currently are")));
            }

            this.checkIfAllPlayersAreDead(game);
            // time)
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.schedule(() -> explodeBomb(player, game), jwsDelayBomb, TimeUnit.MILLISECONDS);
            if (game.getState().equals("RUNNING")) {
                player.setLastbomb(new Timestamp(System.currentTimeMillis()));
                playerRepository.persist(player);
                Game gameModel = ConvertToModel.convertToModel(game, Game.class);
                return Optional.of(Result.success(gameModel));
            } else {
                return Optional.of(Result.error(InvalidDataError.create("The game is not running")));
            }
        } catch (Error e) {
            e.printStackTrace();
            return Optional.of(Result.error(UnknownDatabaseError.create()));
        }
    }

    private String readRleMapFromFile() throws IOException {
        try {
            URL url = Paths.get(mapUrl).toUri().toURL();
            StringBuilder content = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                return content.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }

    private void explodeBomb(PlayerEntity player, GameEntity game) {
        int explosionRadius = 1; // You can adjust the explosion radius as needed

        // Get the position of the bomb and player
        int bombPosX = player.getPosx();
        int bombPosY = player.getPosy();

        // Schedule a task to execute the explosion logic after JWS_DELAY_BOMB ticks
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(() -> {
            // Handle the explosion logic here
            // Iterate over all players in the game to check for the ones within the
            // explosion radius
            for (PlayerEntity otherPlayer : game.getPlayers()) {
                if (otherPlayer.getId() != player.getId()) { // Skip the player who placed the bomb
                    int otherPlayerPosX = otherPlayer.getPosx();
                    int otherPlayerPosY = otherPlayer.getPosy();

                    // Calculate the Manhattan distance between the bomb and the other player
                    int distanceX = Math.abs(otherPlayerPosX - bombPosX);
                    int distanceY = Math.abs(otherPlayerPosY - bombPosY);

                    // If the other player is within the explosion radius, reduce their lives
                    if (distanceX <= explosionRadius && distanceY <= explosionRadius) {
                        int newLives = otherPlayer.getLives() - 1;
                        if (newLives <= 0) {
                            // The player is dead, you can handle this accordingly (e.g., remove them from
                            // the game or set a flag)
                            otherPlayer.setLives(0);
                        } else {
                            otherPlayer.setLives(newLives);
                        }
                        // Persist the changes to the player entity
                        playerRepository.persist(otherPlayer);
                    }
                }
            }

            // Handle destruction of wooden blocks
            String map = game.getMap();
            String[] mapArray = map.split("\n");
            String[] mapRow = mapArray[bombPosY - 1].split("");
            StringBuilder updatedMapRow = new StringBuilder(mapArray[bombPosY - 1]);

            for (int x = bombPosX - explosionRadius; x <= bombPosX + explosionRadius; x++) {
                char mapContent = mapRow[x - 1].charAt(0);
                if (mapContent == 'W') {
                    // If the block is wooden, destroy it
                    updatedMapRow.setCharAt(x - 1, ' ');
                }
            }

            // Update the game map with the modified row
            mapArray[bombPosY - 1] = updatedMapRow.toString();
            String updatedMap = String.join("\n", mapArray);
            game.setMap(updatedMap);

            // Optionally, you can remove the bomb from the game after it explodes
            // game.getBombs().remove(player.getBomb());
            // player.setBomb(null);

            // Persist the changes to the game entity
            gameRepository.persist(game);
        }, jwsDelayBomb, TimeUnit.MILLISECONDS);
    }

    // check if the all the players are dead so update the game state to finished
    private void checkIfAllPlayersAreDead(GameEntity game) {
        boolean allPlayersDead = true;
        for (PlayerEntity player : game.getPlayers()) {
            if (player.getLives() > 0) {
                allPlayersDead = false;
                break;
            }
        }
        if (allPlayersDead) {
            game.setState("FINISHED");
            gameRepository.persist(game);
        }
    }

}
