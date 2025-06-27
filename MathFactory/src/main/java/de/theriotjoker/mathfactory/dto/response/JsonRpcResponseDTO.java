package de.theriotjoker.mathfactory.dto.response;

import de.theriotjoker.mathfactory.dto.AbstractJsonRpcResponseMessage;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class JsonRpcResponseDTO extends AbstractJsonRpcResponseMessage {
    private Object result;
}
