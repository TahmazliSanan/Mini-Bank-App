package org.pronet.app.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.pronet.app.payloads.*;
import org.pronet.app.services.UserService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserController")
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-account")
    public BankResponse createAccount(@RequestBody UserRequest request) {
        return userService.createAccount(request);
    }

    @GetMapping("/balance-enquiry")
    public BankResponse getBalanceEnquiry(@RequestBody EnquiryRequest request) {
        return userService.balanceEnquiry(request);
    }

    @GetMapping("/name-enquiry")
    public String getNameEnquiry(@RequestBody EnquiryRequest request) {
        return userService.nameEnquiry(request);
    }

    @PostMapping("/credit")
    public BankResponse processCredit(@RequestBody CreditDebitRequest request) {
        return userService.creditAccount(request);
    }

    @PostMapping("/debit")
    public BankResponse processDebit(@RequestBody CreditDebitRequest request) {
        return userService.debitAccount(request);
    }

    @PostMapping("/transfer")
    public BankResponse processTransfer(@RequestBody TransferRequest request) {
        return userService.transfer(request);
    }
}
