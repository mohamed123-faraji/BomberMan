package fr.malibu.assistants.jws.utils.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ApiExceptionResponse(e.getMessage(), false, Response.Status.NOT_FOUND.getStatusCode(),
                        ZonedDateTime.now(ZoneId.of("Africa/Casablanca"))))
                .build();
    }

}
