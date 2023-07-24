package fr.malibu.assistants.jws.presentation.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* 
 * This class is used to map the request body of the POST /games endpoint
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGameRequest {

    @NotNull
    @NotBlank(message = "The name of the player must not be blank")
    private String name;

}