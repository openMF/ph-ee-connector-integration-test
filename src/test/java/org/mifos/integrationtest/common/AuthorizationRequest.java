package org.mifos.integrationtest.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationRequest {

    private String batchId;

    private String payerIdentifier;

    private String currency;

    private String amount;

}
