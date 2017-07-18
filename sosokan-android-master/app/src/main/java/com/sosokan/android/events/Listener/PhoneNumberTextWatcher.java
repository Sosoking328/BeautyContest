/*
 * Copyright (c) 2014 Amberfog.
 *
 * This source code is Amberfog Confidential Proprietary
 * This software is protected by copyright. All rights and titles are reserved.
 * You shall not use, copy, distribute, modify, decompile, disassemble or reverse
 * engineer the software. Otherwise this violation would be treated by law and
 * would be subject to legal prosecution. Legal use of the software provides
 * receipt of a license from the right holder only.
 */

package com.sosokan.android.events.Listener;

import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;

import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.sosokan.android.ui.fragment.PhoneFragment;

import java.util.Locale;

public class PhoneNumberTextWatcher implements TextWatcher {

    /**
     * Indicates the change was caused by ourselves.
     */
    private boolean mSelfChange = false;
    /**
     * Indicates the formatting has been stopped.
     */
    private boolean mStopFormatting;
    private String countryCode;
    private AsYouTypeFormatter mFormatter;
    private OnPhoneChangedListener mOnPhoneChangedListener;
    private PhoneFragment.UpdatePhone listener;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (listener != null) {
                listener.updatePhone();
            }
        }
    };

    /**
     * The formatting is based on the current system locale and future locale changes
     * may not take effect on this instance.
     */
    public PhoneNumberTextWatcher(OnPhoneChangedListener listener) {
        this(Locale.getDefault().getCountry());
        mOnPhoneChangedListener = listener;
    }

    /**
     * The formatting is based on the given <code>countryCode</code>.
     *
     * @param countryCode the ISO 3166-1 two-letter country code that indicates the country/region
     *                    where the phone number is being entered.
     * @hide
     */
    public PhoneNumberTextWatcher(String countryCode) {
        if (countryCode == null) throw new IllegalArgumentException();
        mFormatter = PhoneNumberUtil.getInstance().getAsYouTypeFormatter(countryCode);
    }

    public boolean isSelfChange() {
        return mSelfChange;
    }

    public void setSelfChange(boolean mSelfChange) {
        this.mSelfChange = mSelfChange;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        if (mSelfChange || mStopFormatting) {
            return;
        }
        // If the user manually deleted any non-dialable characters, stop formatting
        if (count > 0 && hasSeparator(s, start, count)) {
            stopFormatting();
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mSelfChange || mStopFormatting) {
            return;
        }
        // If the user inserted any non-disable characters, stop formatting
        if (count > 0 && hasSeparator(s, start, count)) {
            stopFormatting();
        }
        handler.removeCallbacks(runnable);
    }

    @Override
    public synchronized void afterTextChanged(Editable s) {
        try {
            if (mStopFormatting) {
                // Restart the formatting when all texts were clear.
                mStopFormatting = !(s.length() == 0);
                return;
            }
            if (mSelfChange) {
                // Ignore the change caused by s.replace().
                return;
            }
            Log.e("phoneNumber", "before format " + s);
            String formatted = reformat(countryCode + s, Selection.getSelectionEnd(s));
            //if (formatted.startsWith("+1 246")) {
            if (countryCode.length() > 4) {
                formatted = formatted.replace(" ", "");
            }
            Log.e("phoneNumber", "after format " + formatted);
            if (formatted != null) {
                int rememberedPos = mFormatter.getRememberedPosition();
                mSelfChange = true;
                s.replace(0, s.length(), formatted.replace(countryCode, ""), 0, formatted.length() - countryCode.length());
                // The text could be changed by other TextWatcher after we changed it. If we found the
                // text is not the one we were expecting, just give up calling setSelection().
                if (formatted.equals(s.toString())) {
                    Selection.setSelection(s, rememberedPos);
                }
                mSelfChange = false;
            }
            handler.postDelayed(runnable, 1200);
        } catch (Exception e) {
            mSelfChange = false;
            e.printStackTrace();
        }
    }

    /**
     * Generate the formatted number by ignoring all non-dialable chars and stick the cursor to the
     * nearest dialable char to the left. For instance, if the number is  (650) 123-45678 and '4' is
     * removed then the cursor should be behind '3' instead of '-'.
     */
    private String reformat(CharSequence s, int cursor) {
        // The index of char to the leftward of the cursor.
        int curIndex = cursor - 1;
        String formatted = null;
        mFormatter.clear();
        char lastNonSeparator = 0;
        boolean hasCursor = false;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (PhoneNumberUtils.isNonSeparator(c)) {
                if (lastNonSeparator != 0) {
                    formatted = getFormattedNumber(lastNonSeparator, hasCursor);
                    hasCursor = false;
                }
                lastNonSeparator = c;
            }
            if (i == curIndex) {
                hasCursor = true;
            }
        }
        if (lastNonSeparator != 0) {
            formatted = getFormattedNumber(lastNonSeparator, hasCursor);
        }
        return formatted;
    }

    private String getFormattedNumber(char lastNonSeparator, boolean hasCursor) {
        return hasCursor ? mFormatter.inputDigitAndRememberPosition(lastNonSeparator)
                : mFormatter.inputDigit(lastNonSeparator);
    }

    private void stopFormatting() {
        mStopFormatting = true;
        mFormatter.clear();
    }

    private boolean hasSeparator(final CharSequence s, final int start, final int count) {
        for (int i = start; i < start + count; i++) {
            char c = s.charAt(i);
            if (!PhoneNumberUtils.isNonSeparator(c)) {
                return true;
            }
        }
        return false;
    }

    public void setListener(PhoneFragment.UpdatePhone listener) {
        this.listener = listener;
    }
}
