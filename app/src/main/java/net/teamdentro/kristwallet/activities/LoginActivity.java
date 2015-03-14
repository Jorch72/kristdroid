package net.teamdentro.kristwallet.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.accounts.Account;
import net.teamdentro.kristwallet.accounts.AccountManager;
import net.teamdentro.kristwallet.adapters.AccountsAdapter;
import net.teamdentro.kristwallet.dialogs.AccountCreationDialogFragment;

public class LoginActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new AccountManager().initialize(this);

        initializeView();
        createListeners();
    }

    private void initializeView() {
        accountsList();
    }

    private void accountsList() {
        ListView listView = (ListView) findViewById(R.id.accountSelectListView);

        final AccountsAdapter adapter;
        adapter = new AccountsAdapter(getApplicationContext(), AccountManager.instance.getAccounts());

        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Account acc = adapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage(R.string.confirm_account_delete)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AccountManager.instance.deleteAccount(acc);
                            }
                        }).setNegativeButton(android.R.string.cancel, null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
    }

    private void createListeners() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addAcountFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountCreationDialogFragment acdf = new AccountCreationDialogFragment();
                acdf.show(getSupportFragmentManager(), "kristAccountCreation");
            }
        });


        AccountManager.instance.setOnAccountCreatedListener(new AccountManager.OnAccountCreatedListener() {
            @Override
            public void onEvent() {
                accountsList();
            }
        });
    }
}
