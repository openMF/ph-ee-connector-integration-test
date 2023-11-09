package org.mifos.integrationtest.common.dto.kong;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeycloakUpdateRequest {

    private String type, value;
    private boolean temporary;
}
