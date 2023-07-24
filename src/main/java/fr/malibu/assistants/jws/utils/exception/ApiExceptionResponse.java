package fr.malibu.assistants.jws.utils.exception;

import lombok.*;

import java.time.ZonedDateTime;

@Data
public class ApiExceptionResponse {

     private String message;

     private int statusCode;
     private Boolean status;
     private ZonedDateTime time;

     public ApiExceptionResponse(String message,
               Boolean status,
               int statusCode, ZonedDateTime timestamp) {
          this.message = message;
          this.status = status;
          this.statusCode = statusCode;
          this.time = timestamp;
     }

     public ApiExceptionResponse() {
     }
}
