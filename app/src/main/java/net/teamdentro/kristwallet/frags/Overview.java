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
import android.widget.LinearLayout;
import android.widget.TextView;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.accounts.AccountManager;
import net.teamdentro.kristwallet.accounts.CurrentAccount;
import net.teamdentro.kristwallet.adapters.TransactionsAdapter;
import net.teamdentro.kristwallet.util.FragmentCallback;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

import io.github.apemanzilla.kwallet.types.Transaction;

public class Overview extends Fragment {
    public static Fragment newInstance(Context context) {
        return new Overview();
    }

    public Overview() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.overviewListView);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if (AccountManager.instance.currentAccount != null)
            addCards(view);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.overviewRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AccountManager.instance.currentAccount.refresh(new FragmentCallback() {
                    @Override
                    public void onTaskDone() {
                        addCards();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        return view;
    }

    public void addCards(View view) {
        CurrentAccount account = AccountManager.instance.currentAccount;
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.overviewListView);

        TextView address = (TextView) view.findViewById(R.id.address);
        address.setText(getString(R.string.placeholder, account.getAddress()));

        TextView balance = (TextView) view.findViewById(R.id.balance);
        balance.setText(getString(R.string.balance, account.getBalance()));

        Transaction[] transactions = account.getTransactions();
        transactions = ArrayUtils.subarray(transactions, 0, 15);

        final TransactionsAdapter adapter;
        adapter = new TransactionsAdapter(view.getContext(), new ArrayList<>(Arrays.asList(transactions)), R.layout.transaction_card_item);

        recyclerView.setAdapter(adapter);
    }

    public void addCards() {
        addCards(getView());
    }

    public void loadingError() {
        View view = getView();
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.overviewLinearLayout);
        layout.removeAllViewsInLayout();

        View newView = inflater.inflate(R.layout.card_loading_error, layout, false);
        layout.addView(newView);
    }
}
