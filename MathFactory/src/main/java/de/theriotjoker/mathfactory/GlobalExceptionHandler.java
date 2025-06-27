package de.theriotjoker.mathfactory;


import de.theriotjoker.mathfactory.dto.response.JsonRpcErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({HttpMessageNotReadableException.class, Exception.class, java.lang.IllegalStateException.class})
    public ResponseEntity<?> handleDeserializationError(Exception ex, HttpServletRequest request) {

        if(!request.getRequestURI().contains("rpc")) {
            // Something non-json-rpc related has failed
            // So no json rpc object gets returned
            return ResponseEntity.badRequest().build();
        }

        JsonRpcErrorDTO error = JsonRpcErrorDTO.getUnreadableRequestJsonDTO();
        return ResponseEntity.badRequest().body(error);
    }

}
