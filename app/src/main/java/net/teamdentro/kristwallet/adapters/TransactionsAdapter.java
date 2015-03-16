package net.teamdentro.kristwallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.accounts.Account;
import net.teamdentro.kristwallet.accounts.AccountManager;
import net.teamdentro.kristwallet.accounts.CurrentAccount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.github.apemanzilla.kwallet.types.Transaction;

public class TransactionsAdapter extends ArrayAdapter<Transaction> {
    public TransactionsAdapter(Context context, ArrayList<Transaction> transactions) {
        super(context, 0, transactions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CurrentAccount account = AccountManager.instance.currentAccount;
        Transaction transaction = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_list_item, parent, false);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView amount = (TextView) convertView.findViewById(R.id.amount);
        TextView date = (TextView) convertView.findViewById(R.id.date);

        if (transaction.getAddr().equalsIgnoreCase("N/A(Mined)")) {
            image.setImageResource(R.mipmap.krist_mined);
            title.setText(getContext().getString(R.string.mined));
            amount.setText(getContext().getString(R.string.balance, String.valueOf(transaction.getAmount())));
        } else if (transaction.getFromAddr().equals(account.getAddress())) {
            image.setImageResource(R.mipmap.krist_red);
            title.setText(getContext().getString(R.string.sent));
            amount.setText(getContext().getString(R.string.transactionTo, String.valueOf(Math.abs(transaction.getAmount())), transaction.getToAddr()));
        } else if (transaction.getToAddr().equals(account.getAddress())) {
            image.setImageResource(R.mipmap.krist_green);
            title.setText(getContext().getString(R.string.received));
            amount.setText(getContext().getString(R.string.transactionFrom, String.valueOf(transaction.getAmount()), transaction.getFromAddr()));
        }

        date.setText(getContext().getString(R.string.placeholder, new SimpleDateFormat("dd MMM HH:mm").format(transaction.getTime())));

        return convertView;
    }
}