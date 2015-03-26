package net.teamdentro.kristwallet.frags;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.accounts.Account;
import net.teamdentro.kristwallet.accounts.AccountManager;
import net.teamdentro.kristwallet.activities.MainActivity;
import net.teamdentro.kristwallet.adapters.AccountsAdapter;
import net.teamdentro.kristwallet.dialogs.AccountCreationDialogFragment;

public class AccountSelect extends Fragment {
    public static Fragment newInstance(Context context) {
        return new AccountSelect();
    }

    public AccountSelect() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_accounts, container, false);

        accountsList(view);
        createListeners(view);

        return view;
    }

    private void accountsList(View view) {
        ListView listView = (ListView) view.findViewById(R.id.accountSelectListView);

        final AccountsAdapter adapter;
        adapter = new AccountsAdapter(view.getContext(), AccountManager.instance.getAccounts());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Account acc = adapter.getItem(position);

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("account", acc);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_enter, R.anim.slide_leave);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Account acc = adapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
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

    private void createListeners(final View view) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.addAcountFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountCreationDialogFragment acdf = new AccountCreationDialogFragment();
                acdf.show(getActivity().getSupportFragmentManager(), "kristAccountCreation");
            }
        });


        AccountManager.instance.setOnAccountCreatedListener(new AccountManager.OnAccountCreatedListener() {
            @Override
            public void onEvent() {
                accountsList(view);
            }
        });
    }
}
