package org.pronet.app.utils;

import java.time.Year;

public class AccountUtil {
    public static final String ACCOUNT_EXIST_CODE = "001";
    public static final String ACCOUNT_EXIST_MESSAGE = "Account is already exist!";
    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account is created successfully!";
    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "Account is not exist!";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account is found!";
    public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "Account is credited successfully!";
    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance!";
    public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Account is debited successfully!";
    public static final String TRANSFER_SUCCESS_CODE = "008";
    public static final String TRANSFER_SUCCESS_MESSAGE = "Transfer is completed successfully!";
    public static final String LOGIN_SUCCESS_CODE = "009";

    public static String generateAccountNumber() {
        Year currentYear = Year.now();

        int beginningNumber = 100000;
        int endingNumber = 999999;
        int randomNumberInt = (int) Math.floor(Math.random() * (endingNumber - beginningNumber + 1) + beginningNumber);

        String currentYearStr = String.valueOf(currentYear);
        String randomNumberStr = String.valueOf(randomNumberInt);

        return currentYearStr + randomNumberStr;
    }
}
