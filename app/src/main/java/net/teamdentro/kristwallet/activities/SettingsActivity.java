package net.teamdentro.kristwallet.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.frags.SettingsFragment;

public class SettingsActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction().replace(R.id.settingsFrame, new SettingsFragment()).commit();
    }
}
