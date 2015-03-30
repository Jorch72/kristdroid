package net.teamdentro.kristwallet.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.krist.AccountManager;
import net.teamdentro.kristwallet.krist.CurrentAccount;
import net.teamdentro.kristwallet.util.TaskCallback;

import org.apache.commons.lang3.StringUtils;

public class SendCurrency extends Fragment {
    public static Fragment newInstance(Context context) {
        return new SendCurrency();
    }

    public SendCurrency() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_send_currency, container, false);

        if (AccountManager.instance.currentAccount != null)
            if (AccountManager.instance.currentAccount.loaded)
                updateBalance(view);
            else
                AccountManager.instance.currentAccount.refreshBalance(new TaskCallback() {
                    @Override
                    public void onTaskDone() {
                        updateBalance(view);
                    }
                });

        final Button sendButton = (Button) view.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(view);
            }
        });

        return view;
    }

    private void send(View view) {
        CurrentAccount account = AccountManager.instance.currentAccount;
        EditText sendAmount = (EditText) view.findViewById(R.id.sendAmount);
        long amount;
        long balance = account.getBalance();

        if (StringUtils.isNumeric(sendAmount.getText())) {
            try {
                amount = Long.parseLong(String.valueOf(sendAmount.getText()));
                if (amount > 0) {
                    if (amount <= balance) {
                        account.send(amount, new TaskCallback() {
                            @Override
                            public void onTaskDone() {

                            }
                        });
                    } else {
                        // TO-DO: insufficiency
                    }
                } else {
                    // TO-DO: zero or negative
                }
            } catch (NumberFormatException e) {
                // TO-DO: not a number
            }
        } else {
            // TO-DO: not a number
        }
    }

    private void updateBalance(View view) {
        CurrentAccount account = AccountManager.instance.currentAccount;
        TextView address = (TextView) view.findViewById(R.id.address);
        address.setText(getString(R.string.placeholder, account.getAddress()));

        TextView balance = (TextView) view.findViewById(R.id.balance);
        balance.setText(getString(R.string.balance, account.getBalance(), account.getNode().shorthandCurrency));
    }
}
