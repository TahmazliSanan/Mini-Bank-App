package org.pronet.app.services.implementations;

import org.pronet.app.entities.User;
import org.pronet.app.payloads.*;
import org.pronet.app.repositories.UserRepository;
import org.pronet.app.services.EmailService;
import org.pronet.app.services.UserService;
import org.pronet.app.utils.AccountUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public BankResponse createAccount(UserRequest request) {
        boolean isExistUser = userRepository.existsByEmail(request.getEmail());
        if (isExistUser) {
            return BankResponse
                    .builder()
                    .responseCode(AccountUtil.ACCOUNT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_EXIST_MESSAGE)
                    .accountDetails(null)
                    .build();
        }

        User newUser = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .gender(request.getGender())
                .address(request.getAddress())
                .accountNumber(AccountUtil.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .status("Active")
                .build();

        User savedUser = userRepository.save(newUser);

        EmailDetails emailDetails = EmailDetails
                .builder()
                .recipient(savedUser.getEmail())
                .subject("Account Creation Confirmation")
                .textBody("Hi, dear! Your account created successfully!\n" +
                        "Account name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + "\n" +
                        "Account number: " + savedUser.getAccountNumber() + "\n" +
                        "Account balance: " + savedUser.getAccountBalance())
                .build();

        emailService.sendEmail(emailDetails);

        return BankResponse
                .builder()
                .responseCode(AccountUtil.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtil.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountDetails(AccountDetails
                        .builder()
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountBalance(savedUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        boolean isExistAccount = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isExistAccount) {
            return BankResponse
                    .builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountDetails(null)
                    .build();
        }

        User foundedUser = userRepository.findByAccountNumber(request.getAccountNumber());

        return BankResponse
                .builder()
                .responseCode(AccountUtil.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtil.ACCOUNT_FOUND_MESSAGE)
                .accountDetails(AccountDetails
                        .builder()
                        .accountName(foundedUser.getFirstName() + " " + foundedUser.getLastName())
                        .accountNumber(request.getAccountNumber())
                        .accountBalance(foundedUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isExistAccount = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isExistAccount) {
            return AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE;
        }

        User foundedUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundedUser.getFirstName() + " " + foundedUser.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        boolean isExistAccount = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isExistAccount) {
            return BankResponse
                    .builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountDetails(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        return BankResponse
                .builder()
                .responseCode(AccountUtil.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtil.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountDetails(AccountDetails
                        .builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountNumber(request.getAccountNumber())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        boolean isExistAccount = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isExistAccount) {
            return BankResponse
                    .builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountDetails(null)
                    .build();
        }

        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());

        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();

        if (availableBalance.intValue() < debitAmount.intValue()) {
            return BankResponse
                    .builder()
                    .responseCode(AccountUtil.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtil.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountDetails(null)
                    .build();
        }

        userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(userToDebit);

        return BankResponse
                .builder()
                .responseCode(AccountUtil.ACCOUNT_DEBITED_SUCCESS_CODE)
                .responseMessage(AccountUtil.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                .accountDetails(AccountDetails
                        .builder()
                        .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                        .accountNumber(request.getAccountNumber())
                        .accountBalance(userToDebit.getAccountBalance())
                        .build())
                .build();
    }
}
