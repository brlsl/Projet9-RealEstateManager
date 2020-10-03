package com.openclassrooms.realestatemanager.controllers.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.REMViewModel;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Property;
import com.openclassrooms.realestatemanager.utils.ExamUtils;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        public static final String PREFERENCES_CURRENCY = "preferences_currency_key";
        private REMViewModel mViewModel;
        private List<Property> mPropertyList;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            configureViewModel();
            configureCurrencyPreferences();
        }

        private void configureCurrencyPreferences() {
            SwitchPreference currencyChoice = findPreference(PREFERENCES_CURRENCY);
            if (currencyChoice!= null){
                currencyChoice.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

                        boolean isDollar = preferences.getBoolean(PREFERENCES_CURRENCY,false);

                        mViewModel.getPropertyList().observe(requireActivity(), propertyList -> {
                            for (int i = 0; i < propertyList.size(); i++) {
                                Property propertyToUpdate = propertyList.get(i);
                                int convertedPrice;
                                if (isDollar && propertyList.get(i).getCurrency().equals(requireContext().getString(R.string.euro))) {
                                    propertyToUpdate.setCurrency(requireContext().getString(R.string.dollar));
                                    convertedPrice = ExamUtils.convertEuroToDollar(propertyToUpdate.getPrice());
                                    propertyToUpdate.setPrice(convertedPrice);
                                    mViewModel.updateProperty(propertyToUpdate);
                                }

                                if (!isDollar && propertyList.get(i).getCurrency().equals(requireContext().getString(R.string.dollar))) {
                                    propertyToUpdate.setCurrency(requireContext().getString(R.string.euro));
                                    convertedPrice = ExamUtils.convertDollarToEuro(propertyToUpdate.getPrice());
                                    propertyToUpdate.setPrice(convertedPrice);
                                    mViewModel.updateProperty(propertyToUpdate);
                                }
                            }
                        });
/*
                        if (isDollar){
                                for (int i = 0; i < mPropertyList.size() ; i++) {
                                    Property propertyToUpdate = mPropertyList.get(i);
                                    int convertedPrice;

                                    propertyToUpdate.setCurrency(requireContext().getString(R.string.dollar));
                                    convertedPrice = ExamUtils.convertEuroToDollar(propertyToUpdate.getPrice());
                                    propertyToUpdate.setPrice(convertedPrice);
                                    mViewModel.updateProperty(propertyToUpdate);
                                }

                        } else {
                            for (int i = 0; i < mPropertyList.size() ; i++) {
                                    Property propertyToUpdate = mPropertyList.get(i);
                                    int convertedPrice;

                                    propertyToUpdate.setCurrency(requireContext().getString(R.string.euro));
                                    convertedPrice = ExamUtils.convertDollarToEuro(propertyToUpdate.getPrice());
                                    propertyToUpdate.setPrice(convertedPrice);
                                    mViewModel.updateProperty(propertyToUpdate);
                                }
                        }

 */
                      /*  mViewModel.getPropertyList().observe(requireActivity(), propertyList -> {
                            for (int i = 0; i < propertyList.size() ; i++) {
                                Property propertyToUpdate = propertyList.get(i);
                                int convertedPrice;

                                if (isDollar){

                                } else{
                                    propertyToUpdate.setCurrency(requireContext().getString(R.string.euro));
                                    convertedPrice = ExamUtils.convertDollarToEuro(propertyToUpdate.getPrice());
                                }
                                propertyToUpdate.setPrice(convertedPrice);
                                mViewModel.updateProperty(propertyToUpdate);

                            }
                        });
*/
                        Log.d("Setting Activity","Value of boolean preferences: "
                                +preferences.getBoolean(PREFERENCES_CURRENCY, true)); // for debug
                        return false;
                    }
                });

            }
        }

        private void configureViewModel() {
            ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(requireContext());
            mViewModel = new ViewModelProvider(this, viewModelFactory).get(REMViewModel.class);
        }

    }
}