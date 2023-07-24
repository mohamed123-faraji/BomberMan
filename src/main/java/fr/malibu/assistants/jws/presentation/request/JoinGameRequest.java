package fr.malibu.assistants.jws.presentation.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinGameRequest {
    
    @NotNull
    @NotBlank(message = "The name of the player must not be blank")
    private String name;
}
