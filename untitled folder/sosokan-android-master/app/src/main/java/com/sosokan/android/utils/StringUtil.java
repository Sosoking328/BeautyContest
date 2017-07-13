package com.sosokan.android.utils;

/**
 * Created by AnhZin on 9/11/2016.
 */
public class StringUtil {
    public static boolean isValidateUserName(String userName) {
        // UserName khong the toan so
        // UserName chi chua cac ky tu a-z, A-Z, 0-9
        Boolean isValid = false;
        int length = userName.length();
        for (int i = 0; i < length; i++) {
            char ch = userName.charAt(i);
            if (((ch >= 0x30 && ch <= 0x39) || (ch >= 0x41 && ch <= 0x5a) || (ch >= 0x61 && ch <= 0x7a))) {
                isValid = true;
            } else {
                isValid = false;
                break;
            }
        }
        return isValid;
    }


    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isPhoneNumberValid(CharSequence number)
    {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }
}
