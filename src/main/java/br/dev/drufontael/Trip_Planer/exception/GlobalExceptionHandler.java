package br.dev.drufontael.Trip_Planer.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourseNotFoundException(ResourceNotFoundException ex, WebRequest request){
        ErrorResponse errorDatails=new ErrorResponse(new Date(), HttpStatus.NOT_FOUND.toString(),
                ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDatails,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidArgumentFormatException.class)
    public ResponseEntity<?> invalidArgumentFormatException(InvalidArgumentFormatException ex,WebRequest request){
        ErrorResponse errorDatails=new ErrorResponse(new Date(), HttpStatus.BAD_REQUEST.toString(),
                ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDatails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<?> resourceAlreadyExistsException(ResourceAlreadyExistsException ex,WebRequest request){
        ErrorResponse errorDetails=new ErrorResponse(new Date(),HttpStatus.BAD_REQUEST.toString(),
                ex.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentException(IllegalArgumentException ex,WebRequest request){
        ErrorResponse errorDetails=new ErrorResponse(new Date(),HttpStatus.BAD_REQUEST.toString(),
                ex.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> MethodArgumentNotValidException(MethodArgumentNotValidException ex,WebRequest request){
        List<String> errors=new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error->errors.add(error.getDefaultMessage()));
        ErrorResponse errorDetails=new ErrorResponse(new Date(),HttpStatus.BAD_REQUEST.toString(),
                errors.toString(),request.getDescription(false));
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<?> invalidDataException(InvalidDataException ex,WebRequest request){
        ErrorResponse errorDetails=new ErrorResponse(new Date(),HttpStatus.BAD_REQUEST.toString(),
                ex.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }



}
