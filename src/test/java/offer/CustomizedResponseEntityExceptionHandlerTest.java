package offer;


import offer.exception.OfferExpiredException;
import offer.exception.OfferNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomizedResponseEntityExceptionHandlerTest {

    private CustomizedResponseEntityExceptionHandler customizedResponseEntityExceptionHandler = new CustomizedResponseEntityExceptionHandler();

    @Test
    public void handleValidationErrors() {
        MethodArgumentNotValidException methodArgumentNotValidException = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        ObjectError objectError = mock(ObjectError.class);
        when(objectError.getObjectName()).thenReturn("error1");
        when(objectError.getDefaultMessage()).thenReturn("errorMessage1");
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(objectError));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Object> responseEntity = customizedResponseEntityExceptionHandler.handleValidationErrors(methodArgumentNotValidException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertThat(errorDetails.getTag(), is("VALIDATION_ERROR"));
        assertThat(errorDetails.getMessage().get(0), is("error1 errorMessage1"));
    }

    @Test
    public void handleOfferExpiredErrors() {
        OfferExpiredException offerExpiredException = new OfferExpiredException("offer expired message");
        ResponseEntity<Object> responseEntity = customizedResponseEntityExceptionHandler.handleOfferExpiredErrors(offerExpiredException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertThat(errorDetails.getTag(), is("OFFER_EXPIRED"));
        assertThat(errorDetails.getMessage().get(0), is("offer expired message"));
    }


    @Test
    public void handleNotFoundErrors() {
        OfferNotFoundException offerNotFoundException = new OfferNotFoundException("offer not found");
        ResponseEntity<Object> responseEntity = customizedResponseEntityExceptionHandler.handleNotFoundErrors(offerNotFoundException);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));

        ErrorDetails errorDetails = (ErrorDetails) responseEntity.getBody();
        assertThat(errorDetails.getTag(), is("OFFER_NOT_FOUND"));
        assertThat(errorDetails.getMessage().get(0), is("offer not found"));
    }
}