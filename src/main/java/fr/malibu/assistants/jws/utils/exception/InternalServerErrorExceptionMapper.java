package fr.malibu.assistants.jws.utils.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InternalServerErrorExceptionMapper implements ExceptionMapper<InternalServerErrorException> {

    @Override
    public Response toResponse(InternalServerErrorException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiExceptionResponse(e.getMessage(), false,
                            Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                            ZonedDateTime.now(ZoneId.of("Africa/Casablanca"))))
                    .build();
    }

}

