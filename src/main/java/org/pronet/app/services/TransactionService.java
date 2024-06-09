package org.pronet.app.services;

import org.pronet.app.payloads.TransactionDto;

public interface TransactionService {
    void saveTransaction(TransactionDto dto);
}
