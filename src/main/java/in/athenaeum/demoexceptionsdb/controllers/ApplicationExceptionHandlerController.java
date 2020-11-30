package in.athenaeum.demoexceptionsdb.controllers;

import in.athenaeum.demoexceptionsdb.exceptions.AlreadyDeletedException;
import in.athenaeum.demoexceptionsdb.exceptions.ApplicationGeneralException;
import in.athenaeum.demoexceptionsdb.exceptions.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandlerController {
    @ExceptionHandler
    public ResponseEntity<String> handleNotFound(RecordNotFoundException recordNotFoundException) {
        //  log the error
        return new ResponseEntity<>("We could not find!!!", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleAlreadyDeleted(AlreadyDeletedException alreadyDeletedException) {
        //  log the error
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleGeneralException(ApplicationGeneralException applicationGeneralException) {
        //  log the error
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleOtherExceptions(Exception exception) {
        //  log the error
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
}
