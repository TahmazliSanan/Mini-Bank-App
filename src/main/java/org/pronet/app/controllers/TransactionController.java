package org.pronet.app.controllers;

import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pronet.app.entities.Transaction;
import org.pronet.app.services.implementations.BankStatement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@Tag(name = "TransactionController")
@RestController
@RequestMapping("/api/bank-statement")
public class TransactionController {
    private final BankStatement bankStatement;

    public TransactionController(BankStatement bankStatement) {
        this.bankStatement = bankStatement;
    }

    @GetMapping("/list")
    public List<Transaction> generateBankStatement(
            @RequestParam String accountNumber,
            @RequestParam String startDate,
            @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatement.generateStatement(accountNumber, startDate, endDate);
    }
}
