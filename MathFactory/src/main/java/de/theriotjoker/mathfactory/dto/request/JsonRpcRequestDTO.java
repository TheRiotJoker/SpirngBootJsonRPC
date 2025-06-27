package de.theriotjoker.mathfactory.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JsonRpcRequestDTO {
    private String jsonrpc = "2.0";
    private String id;
    private String method;
    private Object params;

}
