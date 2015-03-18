package net.teamdentro.kristwallet.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

        if (AccountManager.instance.currentAccount != null)
            addList( view);

        return view;
    }

    private void addList(View view) {
        CurrentAccount account = AccountManager.instance.currentAccount;
        account.refresh();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.transactionsListView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        final TransactionsAdapter adapter;
        adapter = new TransactionsAdapter(view.getContext(), new ArrayList<>(Arrays.asList(account.getTransactions())), R.layout.transaction_list_item);

        recyclerView.setAdapter(adapter);
    }

}
