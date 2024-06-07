package org.pronet.app.utils;

import java.time.Year;

public class AccountUtil {
    public static final String ACCOUNT_EXIST_CODE = "001";
    public static final String ACCOUNT_EXIST_MESSAGE = "Account is already exist!";
    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account is created successfully!";

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
