package de.theriotjoker.bausteineverteiltersysteme_02.dto.response;

import de.theriotjoker.bausteineverteiltersysteme_02.dto.AbstractJsonRpcResponseMessage;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class JsonRpcResponseDTO extends AbstractJsonRpcResponseMessage {
    private Object result;
}
