package fr.malibu.assistants.jws.presentation.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovePlayerRequest {
   

    @NotNull
    @NotBlank(message = "The posX of the player must not be blank")
    private int posX;

    @NotNull
    @NotBlank(message = "The posY of the player must not be blank")
    private int posY;
}
