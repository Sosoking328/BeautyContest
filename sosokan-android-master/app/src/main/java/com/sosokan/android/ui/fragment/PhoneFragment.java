package com.sosokan.android.ui.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.sosokan.android.R;
import com.sosokan.android.adapter.CountryAdapter;
import com.sosokan.android.events.Listener.OnPhoneChangedListener;
import com.sosokan.android.events.Listener.PhoneNumberTextWatcher;
import com.sosokan.android.models.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;

public class PhoneFragment extends Fragment implements OnPhoneChangedListener,
        AdapterView.OnItemSelectedListener {
    protected static final TreeSet<String> CANADA_CODES = new TreeSet<>();

    static {
        CANADA_CODES.add("204");
        CANADA_CODES.add("236");
        CANADA_CODES.add("249");
        CANADA_CODES.add("250");
        CANADA_CODES.add("289");
        CANADA_CODES.add("306");
        CANADA_CODES.add("343");
        CANADA_CODES.add("365");
        CANADA_CODES.add("387");
        CANADA_CODES.add("403");
        CANADA_CODES.add("416");
        CANADA_CODES.add("418");
        CANADA_CODES.add("431");
        CANADA_CODES.add("437");
        CANADA_CODES.add("438");
        CANADA_CODES.add("450");
        CANADA_CODES.add("506");
        CANADA_CODES.add("514");
        CANADA_CODES.add("519");
        CANADA_CODES.add("548");
        CANADA_CODES.add("579");
        CANADA_CODES.add("581");
        CANADA_CODES.add("587");
        CANADA_CODES.add("604");
        CANADA_CODES.add("613");
        CANADA_CODES.add("639");
        CANADA_CODES.add("647");
        CANADA_CODES.add("672");
        CANADA_CODES.add("705");
        CANADA_CODES.add("709");
        CANADA_CODES.add("742");
        CANADA_CODES.add("778");
        CANADA_CODES.add("780");
        CANADA_CODES.add("782");
        CANADA_CODES.add("807");
        CANADA_CODES.add("819");
        CANADA_CODES.add("825");
        CANADA_CODES.add("867");
        CANADA_CODES.add("873");
        CANADA_CODES.add("902");
        CANADA_CODES.add("905");
    }

    protected SparseArray<ArrayList<Country>> mCountriesMap = new SparseArray<>();
    protected PhoneNumberUtil mPhoneNumberUtil = PhoneNumberUtil.getInstance();
    protected Spinner mSpinner;
    protected String mLastEnteredPhone;
    protected EditText mPhoneEdit;
    protected CountryAdapter mAdapter;
    private PhoneNumberTextWatcher mTextWatcher;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCodes(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initUI(view);
        return view;
    }

    private void initUI(View rootView) {
        mSpinner = (Spinner) rootView.findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(this);
        mTextWatcher = new PhoneNumberTextWatcher(this);
        mAdapter = new CountryAdapter(getActivity());
        mAdapter.setIsSpinner(true);
        mSpinner.setAdapter(mAdapter);
        mPhoneEdit = (EditText) rootView.findViewById(R.id.phone);
        mPhoneEdit.addTextChangedListener(mTextWatcher);
        mPhoneEdit.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    private void initCodes(Context context) {
        new AsyncPhoneInitTask(context).execute();
    }

    public String validate() {
        String phone = null;
        Phonenumber.PhoneNumber p = null;
        try {
            Country c = (Country) mSpinner.getSelectedItem();
            mLastEnteredPhone = c.getCountryCodeStr() + mPhoneEdit.getText().toString();
            p = mPhoneNumberUtil.parse(mLastEnteredPhone, null);
            phone = "+" + p.getCountryCode() + p.getNationalNumber();
        } catch (Exception ignore) {
        }
        if (p != null && mPhoneNumberUtil.isValidNumber(p)) {
            return phone;
        } else {
            return null;
        }
    }

    public String getText() {
        return mPhoneEdit.getText().toString();
    }

    public View getEditView() {
        return mPhoneEdit;
    }

    /*protected void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }*/

    /*protected void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }*/

    private String getCountryRegionFromPhone(Context paramContext) {
        TelephonyManager service = null;
        int res = paramContext.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE");
        if (res == PackageManager.PERMISSION_GRANTED) {
            service = (TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE);
        }
        String code = null;
        if (service != null) {
            String str = service.getLine1Number();
            if (!TextUtils.isEmpty(str) && !str.matches("^0*$")) {
                code = parseNumber(str);
            }
        }
        if (code == null) {
            if (service != null) {
                code = service.getNetworkCountryIso();
            }
            if (code == null) {
                code = paramContext.getResources().getConfiguration().locale.getCountry();
            }
        }
        if (code != null) {
            return code.toUpperCase();
        }
        return null;
    }

    private String parseNumber(String paramString) {
        if (paramString == null) {
            return null;
        }
        PhoneNumberUtil numberUtil = PhoneNumberUtil.getInstance();
        String result;
        try {
            Phonenumber.PhoneNumber localPhoneNumber = numberUtil.parse(paramString, null);
            result = numberUtil.getRegionCodeForNumber(localPhoneNumber);
            if (result == null) {
                return null;
            }
        } catch (NumberParseException localNumberParseException) {
            return null;
        }
        return result;
    }

    @Override
    public void onPhoneChanged(String phone) {
        /*if (listener != null) {
            listener.updatePhone();
        }*/
    }

    public void setListener(UpdatePhone listener) {
        mTextWatcher.setListener(listener);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Country c = (Country) mSpinner.getItemAtPosition(position);
        if (mLastEnteredPhone != null && mLastEnteredPhone.startsWith(c.getCountryCodeStr())) {
            return;
        }
        mPhoneEdit.getText().clear();
        mPhoneEdit.setSelection(mPhoneEdit.length());
        mLastEnteredPhone = null;
        mTextWatcher.setCountryCode(c.getCountryCodeStr());
        setFilter(c);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setFilter(final Country c) {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dStart, int dEnd) {
                try {
                    if (c.getCountryISO().equalsIgnoreCase("US") && dest.toString().replace(" ", "").replace("-", "").trim().length() > 10) {
                        return dest.subSequence(0, dest.length() - 1);
                    }
                    for (int i = start; i < end; i++) {
                        char c = source.charAt(i);
                        if (dStart > 0 && !Character.isDigit(c)) {
                            return "";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        mPhoneEdit.setFilters(new InputFilter[]{filter});
    }

    public void clear() {
        mPhoneEdit.getText().clear();
    }

    private class AsyncPhoneInitTask extends AsyncTask<Void, Void, ArrayList<Country>> {

        private int mSpinnerPosition = -1;
        private Context mContext;

        AsyncPhoneInitTask(Context context) {
            mContext = context;
        }

        @Override
        protected ArrayList<Country> doInBackground(Void... params) {
            ArrayList<Country> data = new ArrayList<>(233);
            String countryCode = "1";
            int code = 1;
            try {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open("countries.dat"), "UTF-8"));
                    // do reading, usually loop until end of file reading
                    String line;
                    int i = 0;
                    while ((line = reader.readLine()) != null) {
                        Country c = new Country(mContext, line, i);
                        if (c.getCountryISO().equalsIgnoreCase(countryCode)) {
                            code = c.getCountryCode();
                        }
                        data.add(c);
                        ArrayList<Country> list = mCountriesMap.get(c.getCountryCode());
                        if (list == null) {
                            list = new ArrayList<>();
                            mCountriesMap.put(c.getCountryCode(), list);
                        }
                        list.add(c);
                        i++;
                    }
                } catch (IOException e) {
                    //log the exception
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            //log the exception
                        }
                    }
                }
                //String countryRegion = getCountryRegionFromPhone(mContext);
                /*if (countryCode != null && countryCode.equals("BB")) {
                    code = 1246;
                }*/
                ArrayList<Country> list = mCountriesMap.get(code);
                if (list != null) {
                    for (Country c : list) {
                        if (c.getCountryISO().equalsIgnoreCase(countryCode)) {
                            mSpinnerPosition = c.getNum();
                            mTextWatcher.setCountryCode(c.getCountryCodeStr());
                            setFilter(c);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<Country> data) {
            mAdapter.addAll(data);
            if (mSpinnerPosition > 0) {
                mSpinner.setSelection(mSpinnerPosition);
            }
        }
    }

    public interface UpdatePhone {
        void updatePhone();
    }
}
