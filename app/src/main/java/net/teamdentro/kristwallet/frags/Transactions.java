package net.teamdentro.kristwallet.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.adapters.TransactionsAdapter;
import net.teamdentro.kristwallet.krist.AccountManager;
import net.teamdentro.kristwallet.krist.CurrentAccount;
import net.teamdentro.kristwallet.util.TaskCallback;

import java.util.ArrayList;
import java.util.Arrays;

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
        final View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.transactionsListView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if (AccountManager.instance.currentAccount != null)
            if (AccountManager.instance.currentAccount.loaded)
                addList(view);
            else
                AccountManager.instance.currentAccount.refresh(new TaskCallback() {
                    @Override
                    public void onTaskDone() {
                        addList(view);
                    }
                });

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.transactionsRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.accent, R.color.lb600);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AccountManager.instance.currentAccount.refresh(new TaskCallback() {
                    @Override
                    public void onTaskDone() {
                        addList();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        return view;
    }

    public void addList() {
        addList(getView());
    }

    private void addList(View view) {
        CurrentAccount account = AccountManager.instance.currentAccount;

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.transactionsListView);

        final TransactionsAdapter adapter;
        adapter = new TransactionsAdapter(view.getContext(), new ArrayList<>(Arrays.asList(account.getTransactions())), R.layout.transaction_list_item);

        recyclerView.setAdapter(adapter);
    }

}
