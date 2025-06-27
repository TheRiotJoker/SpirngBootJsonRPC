package de.theriotjoker.mathfactory.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Builder
public class WebSocketPayload {
    private String type;
    private String uuid;
    private Object payload;
}
