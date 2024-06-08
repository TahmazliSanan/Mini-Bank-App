package org.pronet.app.services;

import org.pronet.app.payloads.BankResponse;
import org.pronet.app.payloads.CreditDebitRequest;
import org.pronet.app.payloads.EnquiryRequest;
import org.pronet.app.payloads.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest request);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
}
