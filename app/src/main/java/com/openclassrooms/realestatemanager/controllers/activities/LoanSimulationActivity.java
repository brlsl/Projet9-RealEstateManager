package com.openclassrooms.realestatemanager.controllers.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;

import static com.openclassrooms.realestatemanager.controllers.activities.SettingsActivity.SettingsFragment.PREFERENCES_CURRENCY;

public class LoanSimulationActivity extends AppCompatActivity {

    // FOR UI
    private RadioGroup mRadioGroup;
    private TextView mTxtViewAmountOrTerms, mTxtViewSimulationResult, mTxtViewCurrency;
    private Button mBtnCalculate;
    private EditText mEdtTxtAmountOrTerms, mEdtTxtYears, mEdtTxtMonths, mEdtTxtRate;
    private ConstraintLayout mConstraintLayout;


    // FOR DATA
    private String mStrRadioChecked;
    private String actualCurrency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_simulation);

        configureViews();
        radioGroupListener();
        configureCurrency();

        editTextListeners();
        onClickButtonCalculate();

    }

    private void configureCurrency() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDollar = preferences.getBoolean(PREFERENCES_CURRENCY,false);
        if (isDollar){
            actualCurrency = getString(R.string.dollar);
        } else{
            actualCurrency = getString(R.string.euro);
        }
        mTxtViewCurrency.setText(actualCurrency);

    }

    // ------ METHODS CONFIGURATION ------

    private void configureViews() {
        mConstraintLayout = findViewById(R.id.loan_activity_constraint);
        mRadioGroup = findViewById(R.id.loan_activity_radioGroup_simulation_type);
        mTxtViewAmountOrTerms = findViewById(R.id.loan_activity_textView_amount_or_terms);
        mEdtTxtAmountOrTerms = findViewById(R.id.editTextAmountOrTermsWanted);
        mEdtTxtYears = findViewById(R.id.loan_activity_editText_duration_years);
        mEdtTxtMonths = findViewById(R.id.loan_activity_editText_duration_months);
        mEdtTxtRate = findViewById(R.id.loan_activity_editText_rate_loan);
        mBtnCalculate = findViewById(R.id.loan_activity_calculation);
        mTxtViewSimulationResult = findViewById(R.id.loan_activity_simulation_textView_result);
        mTxtViewCurrency = findViewById(R.id.loan_activity_currency);

    }

    // ------ LISTENERS ------

    private void radioGroupListener() {
        mStrRadioChecked =  getApplicationContext().getString(R.string.loan_amount);

        mRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            RadioButton checkedRatioButton = radioGroup.findViewById(checkedId);
            if (checkedRatioButton.isChecked())
            {
                if (checkedRatioButton.getText().toString().equals(getApplicationContext().getString(R.string.loan_amount)))
                {
                    mStrRadioChecked = getApplicationContext().getString(R.string.loan_amount);
                    mTxtViewAmountOrTerms.setText(getApplicationContext().getString(R.string.loan_terms));
                    mEdtTxtAmountOrTerms.setText("");
                    mEdtTxtAmountOrTerms.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
                    mTxtViewSimulationResult.setVisibility(View.GONE);
                } else if (checkedRatioButton.getText().toString().equals(getApplicationContext().getString(R.string.loan_terms))){
                    mStrRadioChecked = getApplicationContext().getString(R.string.loan_terms);
                    mTxtViewAmountOrTerms.setText(getApplicationContext().getString(R.string.loan_amount));
                    mEdtTxtAmountOrTerms.setText("");
                    mEdtTxtAmountOrTerms.setFilters(new InputFilter[] { new InputFilter.LengthFilter(8) });
                    mTxtViewSimulationResult.setVisibility(View.GONE);
                }
            }
        });
    }

    private void editTextListeners() {
        editTextYearsListener();
        editTextMonthListener();
        editTextRateListener();
    }

    private void editTextRateListener() {
        mEdtTxtRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()){
                    if (mEdtTxtRate.getText().toString().startsWith(".")){
                        mEdtTxtRate.setText("0.");
                        mEdtTxtRate.setSelection(mEdtTxtRate.length()); // put cursor at the end
                    }

                    if (mEdtTxtRate.getText().toString().length() > 2){ // 0.6
                        float rate = Float.parseFloat(mEdtTxtRate.getText().toString());
                        if (rate < 0.4 || rate > 5){
                            mEdtTxtRate.setText("");
                            Snackbar.make(mConstraintLayout,"Rate must be between 0.4 % and 5 %", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    if (mEdtTxtRate.getText().toString().length() == 2 && !mEdtTxtRate.getText().toString().contains(".")){ // case "00", "06"
                        float rate = Float.parseFloat(mEdtTxtRate.getText().toString());
                        if (rate < 0.4 || rate > 5){
                            mEdtTxtRate.setText("");
                            Snackbar.make(mConstraintLayout,"Rate must be between 0.4 % and 5 %", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    if (mEdtTxtRate.getText().toString().length() == 1){
                        float rate = Float.parseFloat(mEdtTxtRate.getText().toString());
                        if (rate > 5) {
                            mEdtTxtRate.setText("");
                            Snackbar.make(mConstraintLayout, "Rate must be between 0.4 % and 5 %", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void editTextMonthListener() {
        mEdtTxtMonths.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()){
                    int month = Integer.parseInt(editable.toString());

                    if (month > 11){
                        int monthModulo = month%12;
                        int nbrYears = month/12;
                        mEdtTxtMonths.setText(String.valueOf(monthModulo));

                        if (!mEdtTxtYears.getText().toString().isEmpty()) {
                            int yearsBase = Integer.parseInt(mEdtTxtYears.getText().toString());
                            int totalYears = yearsBase + nbrYears;
                            mEdtTxtYears.setText(String.valueOf(totalYears));
                        } else {
                            mEdtTxtYears.setText(String.valueOf(nbrYears));
                        }

                    }
                    if (!mEdtTxtYears.getText().toString().isEmpty() && Integer.parseInt(mEdtTxtYears.getText().toString()) >= 30 && month > 0){
                        mEdtTxtMonths.setText("0");
                        Snackbar.make(mConstraintLayout,"Duration cannot exceed 30 years", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void editTextYearsListener() {
        mEdtTxtYears.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()){
                    int years = Integer.parseInt(editable.toString());
                    if (years == 0){
                        mEdtTxtYears.setText("1");
                        Snackbar.make(mConstraintLayout,"Duration must be at least 1 year", Snackbar.LENGTH_SHORT).show();

                    }

                    if (years == 30){
                        mEdtTxtMonths.setText("0");
                    }

                    if (years > 30){
                        mEdtTxtYears.setText("30");
                        mEdtTxtMonths.setText("0");
                        Snackbar.make(mConstraintLayout,"Duration cannot exceed 30 years", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void onClickButtonCalculate() {
        mBtnCalculate.setOnClickListener(view -> {
            if (mEdtTxtAmountOrTerms.getText().toString().isEmpty() || mEdtTxtYears.getText().toString().isEmpty() ||
                    mEdtTxtMonths.getText().toString().isEmpty() || mEdtTxtRate.getText().toString().isEmpty()){
                Toast.makeText(this, "Missing value", Toast.LENGTH_SHORT).show();
            } else {

                if (mStrRadioChecked.equals(getApplicationContext().getString(R.string.loan_terms))){
                    int C = Integer.parseInt(mEdtTxtAmountOrTerms.getText().toString()); // capital
                    float t = Float.parseFloat(mEdtTxtRate.getText().toString())/100; // annual rate
                    float n = Float.parseFloat(mEdtTxtYears.getText().toString()) + Float.parseFloat(mEdtTxtMonths.getText().toString())/12; // duration
                    double m = (C * t/12) / (1 -  Math.pow(1+ t/12, (-12*n))); // monthly term
                    int loanCost = (int) Math.round(m * 12 * n - C);

                    mTxtViewSimulationResult.setVisibility(View.VISIBLE);
                    mTxtViewSimulationResult.setText(getApplicationContext().getString(R.string.simulation_loan_terms_result,
                            Utils.formatPrice(String.valueOf(C)), actualCurrency,
                            Utils.formatPrice(String.valueOf(Math.round(m))), actualCurrency,
                            Utils.formatPrice(String.valueOf(loanCost)), actualCurrency));
                }

                if (mStrRadioChecked.equals(getApplicationContext().getString(R.string.loan_amount))){
                    int m = Integer.parseInt(mEdtTxtAmountOrTerms.getText().toString()); // monthly term
                    float t = Float.parseFloat(mEdtTxtRate.getText().toString())/100; // annual rate
                    float n = 12; // number of repayment per year;
                    float N = (Float.parseFloat(mEdtTxtYears.getText().toString())*12) + Float.parseFloat(mEdtTxtMonths.getText().toString()); // total of monthly repayment
                    double C = (m*(1- Math.pow(1+t/n, -N))) / (t/n);
                    float duration = Float.parseFloat(mEdtTxtYears.getText().toString()) + Float.parseFloat(mEdtTxtMonths.getText().toString())/12; // duration
                    int loanCost = (int) Math.round(m * 12 * duration - C);

                    mTxtViewSimulationResult.setVisibility(View.VISIBLE);
                    mTxtViewSimulationResult.setText(getApplicationContext().getString(R.string.simulation_loan_amount_result,
                            Utils.formatPrice(String.valueOf(Math.round(C))), actualCurrency,
                            Utils.formatPrice(String.valueOf(m)), actualCurrency,
                            Utils.formatPrice(String.valueOf(loanCost)), actualCurrency));
                }
            }
        });
    }

}