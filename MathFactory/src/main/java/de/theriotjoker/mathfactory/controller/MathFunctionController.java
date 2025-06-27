package de.theriotjoker.mathfactory.controller;

import de.theriotjoker.mathfactory.dto.request.JsonRpcRequestDTO;
import de.theriotjoker.mathfactory.dto.request.MathFunctionChangeDTO;
import de.theriotjoker.mathfactory.dto.response.JsonRpcErrorDTO;
import de.theriotjoker.mathfactory.dto.response.JsonRpcResponseDTO;
import de.theriotjoker.mathfactory.dto.response.OperationResponseDTO;
import de.theriotjoker.mathfactory.exception.InvalidJsonRpcVersionException;
import de.theriotjoker.mathfactory.exception.InvalidParametersException;
import de.theriotjoker.mathfactory.exception.MethodUnavailableException;
import de.theriotjoker.mathfactory.service.OperationService;
import de.theriotjoker.mathfactory.utils.OperationEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.*;

@RestController
public class MathFunctionController {

    private final OperationService operationService;

    private final BodyBuilder BAD_REQUEST = badRequest();

    public MathFunctionController(OperationService operationService) {
        this.operationService = operationService;
    }

    @PutMapping("/admin")
    public ResponseEntity<String> changeMathFunction(@RequestBody MathFunctionChangeDTO dto) {
        try {
            operationService.changeOperation(dto);
            String enabled = dto.isEnabled() ? "enabled" : "disabled";
            return ResponseEntity.ok("Successfully changed the function: "+dto.getFunctionName()+": New Cost: "+dto.getNewCost() + " and the function has been "+enabled);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong: "+e.getMessage());
        }
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
            OperationEnum operationName = OperationEnum.fromString(jsonRpcRequestDTO.getMethod());
            // Input must always be 2 numbers, the 2nd number can only be missing in the case of factorial
            // The params must be cast-able to a List of Doubles, since that is what all methods require
            List<Double> parameters = captureParameters(jsonRpcRequestDTO.getParams());
            OperationResponseDTO dto = operationService.executeOperation(operationName, parameters, requestID);
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
            e.printStackTrace();
            System.out.println(e.getMessage());
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
