package fr.malibu.assistants.jws.presentation.response;

import java.util.ArrayList;


import fr.malibu.assistants.jws.data.model.Game;
import lombok.Data;

@Data
class GameListDto {
    private Long id;
    private Integer players;
    private GameState state;

}

enum GameState {
    FINISHED,
    RUNNING,
    STARTING
}

@Data
public class GameListResponse {
    private ArrayList<GameListDto> games = new ArrayList<>();

    public static GameListResponse fromModel(Game[] games) {
        GameListResponse response = new GameListResponse();

        for (Game game : games) {

            GameListDto gameListDto = new GameListDto();
            gameListDto.setId(game.getId());
            gameListDto.setPlayers(game.getPlayers().size());
            gameListDto.setState(GameState.valueOf(game.getState()));
            response.getGames().add(gameListDto);
        }
        return response;
    }

    // ... (getter and setter for 'games', if needed)
}