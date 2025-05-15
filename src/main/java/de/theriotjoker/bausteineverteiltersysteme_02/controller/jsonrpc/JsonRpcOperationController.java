package de.theriotjoker.bausteineverteiltersysteme_02.controller.jsonrpc;

import de.theriotjoker.bausteineverteiltersysteme_02.dto.request.JsonRpcRequestDTO;
import de.theriotjoker.bausteineverteiltersysteme_02.dto.response.JsonRpcErrorDTO;
import de.theriotjoker.bausteineverteiltersysteme_02.dto.response.JsonRpcResponseDTO;
import de.theriotjoker.bausteineverteiltersysteme_02.dto.response.OperationResponseDTO;
import de.theriotjoker.bausteineverteiltersysteme_02.exception.InvalidJsonRpcVersionException;
import de.theriotjoker.bausteineverteiltersysteme_02.exception.InvalidParametersException;
import de.theriotjoker.bausteineverteiltersysteme_02.exception.MethodUnavailableException;
import de.theriotjoker.bausteineverteiltersysteme_02.service.OperationService;
import de.theriotjoker.bausteineverteiltersysteme_02.utils.OperationEnum;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static de.theriotjoker.bausteineverteiltersysteme_02.utils.OperationEnum.fromString;
import static org.springframework.http.ResponseEntity.*;

@RestController
public class JsonRpcOperationController {

    private final OperationService operationService;

    private final BodyBuilder BAD_REQUEST = badRequest();

    public JsonRpcOperationController(OperationService operationService) {
        this.operationService = operationService;
    }


    @PostMapping(value = "/rpc", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> rpcCall(@RequestBody JsonRpcRequestDTO jsonRpcRequestDTO) {
        String requestID = jsonRpcRequestDTO.getId();


        if(Objects.isNull(requestID) || requestID.isEmpty()) {
            // The request sent is a notification
            // No json response allowed, only http
            // set requestID to empty
            return noContent().build();
        }

        try {
            if(!jsonRpcRequestDTO.getJsonrpc().contentEquals("2.0")) {
                throw new InvalidJsonRpcVersionException("The field \"jsonrpc\" MUST be exactly  \"2.0\"");
            }
            OperationEnum operationName = fromString(jsonRpcRequestDTO.getMethod());
            // Input must always be 2 numbers, the 2nd number can only be missing in the case of factorial
            // The params must be cast-able to a List of Doubles, since that is what all methods require
            List<Double> parameters = captureParameters(jsonRpcRequestDTO.getParams());
            OperationResponseDTO dto = operationService.executeOperationTracked(operationName, parameters, requestID);
            return ok(JsonRpcResponseDTO.builder()
                    .id(requestID)
                    .result(dto)
                    .build());
        } catch(MethodUnavailableException | IllegalArgumentException e ) {
            // Thrown if chosen method is correctly called but unavailable
            // or if method name doesn't exist (String cannot be mapped to corresponding enum)
            return BAD_REQUEST.body(JsonRpcErrorDTO.getMethodUnavailableDTO(requestID, e.getMessage()));
        } catch (InvalidParametersException e) {
            return BAD_REQUEST.body(JsonRpcErrorDTO.getInvalidParametersDTO(requestID, e.getMessage()));
        } catch (InvalidJsonRpcVersionException e) {
            return BAD_REQUEST.body(JsonRpcErrorDTO.getInvalidRequestObjectDTO(requestID, e.getMessage()));
        } catch (Exception e) {
            return BAD_REQUEST.body(JsonRpcErrorDTO.getInternalErrorDTO(requestID));
        }
    }
    private List<Double> captureParameters(Object params) {
        if(!(params instanceof List<?> rawParamList)) {
            throw new InvalidParametersException("Parameters must be a list.");
        }

        if(rawParamList.isEmpty()) {
            throw new InvalidParametersException("Parameters must not be an empty list.");
        }

        List<Double> capturedParameters = new ArrayList<>();

        for(Object param : rawParamList) {
            if(!(param instanceof Number)) {
                throw new InvalidParametersException("All parameters must be numbers.");
            }
            capturedParameters.add(((Number)param).doubleValue());
        }

        return capturedParameters;
    }
}
