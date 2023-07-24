package fr.malibu.assistants.jws.data.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import fr.malibu.assistants.jws.domain.entity.GameEntity;
import fr.malibu.assistants.jws.domain.entity.PlayerEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Game extends GameEntity {

    Long id;
    Timestamp starttime;
    String state;
    List<PlayerEntity> players = new ArrayList<>();
    String map;
}
