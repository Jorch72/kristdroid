package net.teamdentro.kristwallet.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteException;
import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.accounts.AccountManager;
import net.teamdentro.kristwallet.frags.AccountSelect;
import net.teamdentro.kristwallet.frags.MasterPasswordCreate;
import net.teamdentro.kristwallet.frags.MasterPasswordEnter;

public class LoginActivity extends ActionBarActivity {
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.getBoolean("fr", true)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.kristLoginFragment,
                    Fragment.instantiate(LoginActivity.this, MasterPasswordCreate.class.getName()));
            transaction.commit();

            prefs.edit().putBoolean("fr", false).apply();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.kristLoginFragment,
                    Fragment.instantiate(LoginActivity.this, MasterPasswordEnter.class.getName()));
            transaction.commit();
        }
    }

    public void attemptAccess(String text) {
        try {
            boolean result = new AccountManager().initialize(this, text);

            if (result) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.kristLoginFragment,
                        Fragment.instantiate(LoginActivity.this, AccountSelect.class.getName()));
                transaction.commit();
            }
        } catch (SQLiteException e) {
            Toast.makeText(this, getString(R.string.passwordFailiure), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.unknownError), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void access(String text) {
        new AccountManager().initialize(this, text);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.kristLoginFragment,
                Fragment.instantiate(LoginActivity.this, AccountSelect.class.getName()));
        transaction.commit();
    }
}
