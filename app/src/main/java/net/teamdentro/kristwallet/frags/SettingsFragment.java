package net.teamdentro.kristwallet.frags;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.krist.AccountManager;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        YesNoPreference walletReset = (YesNoPreference) findPreference("walletReset");
        walletReset.setListener(new YesNoPreference.YesNoDialogListener() {
            @Override
            public void onDialogClosed(boolean positiveResult) {
                if (positiveResult)
                    AccountManager.instance.reset(getActivity().getApplicationContext());
            }
        });
    }
}
