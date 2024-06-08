package org.pronet.app.services.implementations;

import org.pronet.app.entities.User;
import org.pronet.app.payloads.AccountDetails;
import org.pronet.app.payloads.BankResponse;
import org.pronet.app.payloads.EmailDetails;
import org.pronet.app.payloads.UserRequest;
import org.pronet.app.repositories.UserRepository;
import org.pronet.app.services.EmailService;
import org.pronet.app.services.UserService;
import org.pronet.app.utils.AccountUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
}
