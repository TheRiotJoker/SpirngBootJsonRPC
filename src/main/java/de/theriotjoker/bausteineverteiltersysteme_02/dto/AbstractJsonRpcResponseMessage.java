package de.theriotjoker.bausteineverteiltersysteme_02.dto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.theriotjoker.bausteineverteiltersysteme_02.dto.response.JsonRpcErrorDTO;
import de.theriotjoker.bausteineverteiltersysteme_02.dto.response.JsonRpcResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        visible = false,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = JsonRpcErrorDTO.class, name = "error"),
        @JsonSubTypes.Type(value = JsonRpcResponseDTO.class, name = "response")
})
@SuperBuilder
@Getter
@NoArgsConstructor
public abstract class AbstractJsonRpcResponseMessage {
    @Builder.Default
    protected String jsonrpc = "2.0";
    protected String id;
}
