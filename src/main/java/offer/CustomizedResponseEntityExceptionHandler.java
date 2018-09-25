package offer;

import offer.exception.OfferExpiredException;
import offer.exception.OfferNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler {


    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        ErrorDetails errorDetails = new ErrorDetails("VALIDATION_ERROR", ex.getBindingResult().getAllErrors()
                .stream().map(
                        error -> {
                            String errorField = FieldError.class.isAssignableFrom(error.getClass()) ? ((FieldError) error).getField() : error.getObjectName();
                            return String.format("%s %s", errorField, error.getDefaultMessage());
                        }
                ).collect(Collectors.toList()));
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({OfferExpiredException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleOfferExpiredErrors(OfferExpiredException ex) {
        ErrorDetails errorDetails = new ErrorDetails("OFFER_EXPIRED", Collections.singletonList(ex.getMessage()));

        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({OfferNotFound.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundErrors(OfferNotFound ex) {
        ErrorDetails errorDetails = new ErrorDetails("OFFER_NOT_FOUND", Collections.singletonList(ex.getMessage()));
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }

}
