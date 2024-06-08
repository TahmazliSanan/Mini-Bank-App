package org.pronet.app.services;

import org.pronet.app.payloads.*;

public interface UserService {
    BankResponse createAccount(UserRequest request);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transfer(TransferRequest request);
}
