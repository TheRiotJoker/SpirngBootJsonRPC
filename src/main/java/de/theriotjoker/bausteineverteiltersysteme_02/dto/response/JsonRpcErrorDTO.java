package de.theriotjoker.bausteineverteiltersysteme_02.dto.response;

import de.theriotjoker.bausteineverteiltersysteme_02.dto.AbstractJsonRpcResponseMessage;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
public class JsonRpcErrorDTO extends AbstractJsonRpcResponseMessage {
    private final static int METHOD_NOT_FOUND_CODE = -32601;
    private final static int INVALID_PARAMETERS_CODE = -32602;
    private final static int INVALID_REQUEST_OBJECT_CODE = -32600;
    private final static int JSON_UNREADABLE_CODE = -32700;
    private final static int INTERNAL_SERVER_ERROR = -32603;

    private int code;
    private String message;

    public static JsonRpcErrorDTO getMethodUnavailableDTO(String id, String message) {
        return JsonRpcErrorDTO.builder()
                .code(METHOD_NOT_FOUND_CODE)
                .message(message)
                .id(id)
                .build();

    }

    public static JsonRpcErrorDTO getInvalidParametersDTO(String id, String message) {
        return JsonRpcErrorDTO.builder()
                .id(id)
                .message(message)
                .code(INVALID_PARAMETERS_CODE)
                .build();
    }
    public static JsonRpcErrorDTO getInvalidRequestObjectDTO(String id, String message) {
        return JsonRpcErrorDTO.builder()
                .id(id)
                .message(message)
                .code(INVALID_REQUEST_OBJECT_CODE)
                .build();
    }

    public static JsonRpcErrorDTO getUnreadableRequestJsonDTO() {
        return JsonRpcErrorDTO.builder()
                .message("The JSON payload sent was not readable.")
                .code(JSON_UNREADABLE_CODE)
                .build();
    }

    public static JsonRpcErrorDTO getInternalErrorDTO(String id) {
        return JsonRpcErrorDTO.builder()
                .id(id)
                .message("An internal server error has occurred.")
                .code(INTERNAL_SERVER_ERROR)
                .build();
    }
}
