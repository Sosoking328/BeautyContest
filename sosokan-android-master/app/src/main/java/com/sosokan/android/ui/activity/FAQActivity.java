package com.sosokan.android.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.sosokan.android.BuildConfig;
import com.sosokan.android.R;
import com.sosokan.android.adapter.FAQAdapter;

import com.sosokan.android.models.FAQ;

import java.util.ArrayList;
import java.util.List;


import butterknife.InjectView;
import io.fabric.sdk.android.Fabric;

public class FAQActivity extends Activity implements View.OnClickListener {

    @InjectView(R.id.lvFaq)
    ListView lvFaq;
    FAQAdapter faqAdapter;

    ImageButton ibBackFAQ;

    String[] questions;
    String[] answers;
    List<FAQ> faqs,faqsOrgigin;
    EditText  edtSearchFAQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        Fabric.with(this, new Crashlytics());
        faqs = new ArrayList<>();
        questions = getResources().getStringArray(R.array.list_faq_question);
        answers = getResources().getStringArray(R.array.list_faq_answer);
        for (int i = 0; i <questions.length; i ++)
        {
            FAQ a = new FAQ(questions[i], answers[i]);
            faqs.add(a);

        }

        faqsOrgigin = faqs;
        faqAdapter = new FAQAdapter(this, R.layout.item_faq, faqs);
        lvFaq.setAdapter(faqAdapter);
        lvFaq.setTextFilterEnabled(true);

        faqAdapter.notifyDataSetChanged();

        ibBackFAQ =(ImageButton) findViewById(R.id.ibBackFAQ);
        edtSearchFAQ = (EditText) findViewById(R.id.edtSearchFAQ);
        ibBackFAQ.setOnClickListener(this);
        edtSearchFAQ.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               // Search();
                faqAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }
        });
        edtSearchFAQ.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String searchValue = edtSearchFAQ.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (actionId == KeyEvent.KEYCODE_ENTER)) {

                    faqAdapter.getFilter().filter(searchValue);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtSearchFAQ.getApplicationWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }


    public void gotoProfile()
    {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("Fragment", "Profile");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, 0);
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() ==R.id.ibBackFAQ)
        {
            gotoProfile();
        }
    }
}
