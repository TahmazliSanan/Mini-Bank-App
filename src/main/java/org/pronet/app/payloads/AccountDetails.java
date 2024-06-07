package org.pronet.app.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetails {
    private String accountName;
    private String accountNumber;
    private BigDecimal accountBalance;
}
