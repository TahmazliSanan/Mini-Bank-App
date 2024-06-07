package org.pronet.app.services;

import org.pronet.app.payloads.BankResponse;
import org.pronet.app.payloads.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest request);
}
