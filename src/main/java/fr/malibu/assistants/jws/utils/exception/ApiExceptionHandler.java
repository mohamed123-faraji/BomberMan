package fr.malibu.assistants.jws.utils.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApiExceptionHandler implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException e) {
        // Check the type of the exception and delegate to the specific exception mapper
        if (e instanceof NotFoundException) {
            return new NotFoundExceptionMapper().toResponse((NotFoundException) e);
        } else if (e instanceof BadRequestException) {
            return new BadRequestExceptionMapper().toResponse((BadRequestException) e);
        } else if (e instanceof InternalServerErrorException) {
            throw (InternalServerErrorException) e;
        } else {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiExceptionResponse(e.getMessage(), false,
                            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                            ZonedDateTime.now(ZoneId.of("Africa/Casablanca"))))
                    .build();
        }
    }
}
