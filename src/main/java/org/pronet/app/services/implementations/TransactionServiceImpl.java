package org.pronet.app.services.implementations;

import org.pronet.app.entities.Transaction;
import org.pronet.app.payloads.TransactionDto;
import org.pronet.app.repositories.TransactionRepository;
import org.pronet.app.services.TransactionService;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void saveTransaction(TransactionDto dto) {
        Transaction transaction = Transaction
                .builder()
                .accountNumber(dto.getAccountNumber())
                .amount(dto.getAmount())
                .type(dto.getType())
                .status("Success")
                .build();

        transactionRepository.save(transaction);
    }
}
