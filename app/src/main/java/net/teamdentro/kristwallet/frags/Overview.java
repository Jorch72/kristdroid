package net.teamdentro.kristwallet.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.accounts.AccountManager;
import net.teamdentro.kristwallet.accounts.CurrentAccount;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

import io.github.apemanzilla.kwallet.types.Transaction;

public class Overview extends Fragment {
    public static Fragment newInstance(Context context) {
        Overview fragment = new Overview();
        return fragment;
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

        TextView address = (TextView) view.findViewById(R.id.address);
        address.setText(getString(R.string.loading));

        TextView balance = (TextView) view.findViewById(R.id.balance);
        balance.setText(getString(R.string.loading));

        return view;
    }

    public void addCards() {
        CurrentAccount account = AccountManager.instance.currentAccount;

        TextView address = (TextView) getView().findViewById(R.id.address);
        address.setText(getString(R.string.placeholder, account.getAddress()));

        TextView balance = (TextView) getView().findViewById(R.id.balance);
        balance.setText(getString(R.string.balance, account.getBalance()));

        Transaction[] transactions = account.getTransactions();
        transactions = ArrayUtils.subarray(transactions, 0, 15);

        for (Transaction transaction : transactions) {
            addTransaction(account, transaction, getView(), (LayoutInflater) getView().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        }
    }

    public void addTransaction(CurrentAccount account, Transaction transaction, View view, LayoutInflater inflater) {
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.overviewLinearLayout);

        if (transaction.getAddr().equalsIgnoreCase("N/A(Mined)")) {
            View newView = inflater.inflate(R.layout.card_mined, layout, false);

            TextView amount = (TextView) newView.findViewById(R.id.amount);
            amount.setText(getString(R.string.balance, String.valueOf(transaction.getAmount())));

            TextView date = (TextView) newView.findViewById(R.id.date);
            date.setText(getString(R.string.placeholder, new SimpleDateFormat("dd MMM HH:mm").format(transaction.getTime())));

            layout.addView(newView);
        } else if (transaction.getFromAddr().equals(account.getAddress())) {
            View newView = inflater.inflate(R.layout.card_sent, layout, false);

            TextView amount = (TextView) newView.findViewById(R.id.amount);
            amount.setText(getString(R.string.balance, String.valueOf(Math.abs(transaction.getAmount())), transaction.getToAddr()));

            TextView date = (TextView) newView.findViewById(R.id.date);
            date.setText(getString(R.string.placeholder, new SimpleDateFormat("dd MMM HH:mm").format(transaction.getTime())));

            layout.addView(newView);
        } else if (transaction.getToAddr().equals(account.getAddress())) {
            View newView = inflater.inflate(R.layout.card_received, layout, false);

            TextView amount = (TextView) newView.findViewById(R.id.amount);
            amount.setText(getString(R.string.balance, String.valueOf(transaction.getAmount()), transaction.getFromAddr()));

            TextView date = (TextView) newView.findViewById(R.id.date);
            date.setText(getString(R.string.placeholder, new SimpleDateFormat("dd MMM HH:mm").format(transaction.getTime())));

            layout.addView(newView);
        }
    }

}
