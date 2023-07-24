package fr.malibu.assistants.jws.utils.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    @Override
    public Response toResponse(BadRequestException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ApiExceptionResponse(e.getMessage(), false, Response.Status.BAD_REQUEST.getStatusCode(),
                        ZonedDateTime.now(ZoneId.of("Africa/Casablanca"))))
                .build();
    }

}