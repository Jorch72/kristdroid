package net.teamdentro.kristwallet.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.accounts.AccountManager;
import net.teamdentro.kristwallet.accounts.CurrentAccount;
import net.teamdentro.kristwallet.adapters.TransactionsAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import io.github.apemanzilla.kwallet.types.Transaction;

public class Transactions extends Fragment {
    public static Fragment newInstance(Context context) {
        return new Transactions();
    }

    public Transactions() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        CurrentAccount account = AccountManager.instance.currentAccount;
        account.refresh();

        ListView listView = (ListView) view.findViewById(R.id.transactionsListView);

        final TransactionsAdapter adapter;
        adapter = new TransactionsAdapter(view.getContext(), new ArrayList<Transaction>(Arrays.asList(account.getTransactions())));

        listView.setAdapter(adapter);

        return view;
    }

}
