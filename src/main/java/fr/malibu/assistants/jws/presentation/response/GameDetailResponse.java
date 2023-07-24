package fr.malibu.assistants.jws.presentation.response;

import java.util.List;

import fr.malibu.assistants.jws.data.model.Game;
import fr.malibu.assistants.jws.domain.entity.PlayerEntity;
import lombok.Data;


@Data
public class GameDetailResponse {
    private String startTime;
    private String state;
    private List<PlayerEntity> players;
    private String[] map;
    private Long id;

    public static GameDetailResponse fromModel(Game game) {
        GameDetailResponse response = new GameDetailResponse();
        response.setStartTime(game.getStarttime().toString());
        response.setState(game.getState());
        response.setPlayers(game.getPlayers());
        response.setMap(game.getMap().split("\n"));
        response.setId(game.getId());
        return response;
    }
}
