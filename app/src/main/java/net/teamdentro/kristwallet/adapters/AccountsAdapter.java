package net.teamdentro.kristwallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.teamdentro.kristwallet.accounts.Account;

import java.util.ArrayList;

public class AccountsAdapter extends ArrayAdapter<Account> {
    public AccountsAdapter(Context context, ArrayList<Account> accounts) {
        super(context, 0, accounts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Account account = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView label = (TextView) convertView.findViewById(android.R.id.text1);
        label.setText(account.getLabel());
        return convertView;
    }
}