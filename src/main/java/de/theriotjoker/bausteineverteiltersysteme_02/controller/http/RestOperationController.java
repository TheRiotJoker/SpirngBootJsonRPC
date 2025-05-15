package de.theriotjoker.bausteineverteiltersysteme_02.controller.http;

import de.theriotjoker.bausteineverteiltersysteme_02.dto.request.OperationChangeDTO;
import de.theriotjoker.bausteineverteiltersysteme_02.dto.response.OperationResponseDTO;
import de.theriotjoker.bausteineverteiltersysteme_02.service.OperationService;
import de.theriotjoker.bausteineverteiltersysteme_02.utils.OperationEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestOperationController {

    private final OperationService operationService;

    public RestOperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping("admin/changeOperation")
    public ResponseEntity<String> changeOperation(@RequestBody OperationChangeDTO dto) {
        try {
            OperationEnum opToChange = OperationEnum.fromString(dto.getOperationName());
            operationService.changeOperation(opToChange, dto.isEnabled(), dto.getNewCost());
            return ResponseEntity.ok("Changes accepted.");
        } catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("calculate")
    public ResponseEntity<?> executeOperation(@RequestParam("operationName") String operationName,
                                                                 @RequestParam("a") Double a,
                                                                 @RequestParam(value = "b", required = false) Double b) {
        try {
            OperationEnum opToExecute = OperationEnum.fromString(operationName);

            List<Double> parameters = b == null ? List.of(a) : List.of(a,b);

            OperationResponseDTO dto = operationService.executeOperation(opToExecute, parameters);
            return ResponseEntity.ok(dto);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
