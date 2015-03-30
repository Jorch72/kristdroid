package net.teamdentro.kristwallet.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.frags.Overview;
import net.teamdentro.kristwallet.frags.SendCurrency;
import net.teamdentro.kristwallet.frags.Transactions;
import net.teamdentro.kristwallet.krist.Account;
import net.teamdentro.kristwallet.krist.AccountManager;
import net.teamdentro.kristwallet.krist.CurrentAccount;

import org.apache.commons.lang3.StringUtils;

import java.util.TreeMap;

public class MainActivity extends ActionBarActivity {
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;

    private TreeMap<String, String> fragments;

    private LoadAPITask loadingTask;

    private void prepareFragments() {
        fragments = new TreeMap<>();

        fragments.put(Overview.class.getName(), getString(R.string.overview));
        fragments.put(Transactions.class.getName(), getString(R.string.transactions));
        fragments.put(SendCurrency.class.getName(), getString(R.string.sendCurrency));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadAPI();

        prepareFragments();
        createNavigationDrawer();
    }

    private void loadAPI() {
        Intent intent = getIntent();
        Account targetAccount = (Account) intent.getSerializableExtra("account");

        loadingTask = new LoadAPITask();
        loadingTask.execute(targetAccount);
    }

    private void createNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.kristToolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.kristDrawerLayout);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);

        ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.drawer_list_item, fragments.values().toArray(new String[]{}));

        final ListView drawerList = (ListView) findViewById(R.id.kristDrawerList);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                transaction.replace(R.id.kristDrawerFragmentContainer,
                        Fragment.instantiate(MainActivity.this,
                                fragments.keySet().toArray()[position].toString()), fragments.keySet().toArray()[position].toString());
                transaction.commit();

                drawerList.setItemChecked(position, true);
                setTitle(fragments.values().toArray()[position].toString());

                drawerLayout.closeDrawer(drawerList);
            }
        });
        drawerList.setItemChecked(0, true);
        setTitle(fragments.values().toArray()[0].toString());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.kristDrawerFragmentContainer,
                Fragment.instantiate(MainActivity.this,
                        fragments.keySet().toArray()[0].toString()), fragments.keySet().toArray()[0].toString());
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();

        if (loadingTask != null) {
            loadingTask.cancel(true);
        }

        overridePendingTransition(R.anim.slide_reverse_enter, R.anim.slide_reverse_leave);

        AccountManager.instance.currentAccount = null;
    }

    private class LoadAPITask extends AsyncTask<Account, Void, Boolean> {
        protected Boolean doInBackground(Account... accounts) {
            Account account = accounts[0];

            CurrentAccount currentAccount = new CurrentAccount(account.getID(), account.getLabel(), account.getPassword(), account.getNode());
            if (!currentAccount.initialize()) return Boolean.FALSE;

            AccountManager.instance.currentAccount = currentAccount;
            return Boolean.TRUE;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result == true)
                addCards();
            else
                loadingError();
        }
    }

    private void loadingError() {
        Overview overviewFragment = (Overview) getSupportFragmentManager().findFragmentByTag(Overview.class.getName());
        if (overviewFragment != null)
            overviewFragment.loadingError();
    }

    private void addCards() {
        Overview overviewFragment = (Overview) getSupportFragmentManager().findFragmentByTag(Overview.class.getName());
        Transactions transactionsFragment = (Transactions) getSupportFragmentManager().findFragmentByTag(Transactions.class.getName());
        if (overviewFragment != null)
            overviewFragment.addCards();
        if (transactionsFragment != null)
            transactionsFragment.addList();
    }
}
