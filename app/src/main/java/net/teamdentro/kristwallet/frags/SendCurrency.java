package net.teamdentro.kristwallet.frags;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.exception.BadValueException;
import net.teamdentro.kristwallet.exception.InsufficientFundsException;
import net.teamdentro.kristwallet.exception.InvalidRecipientException;
import net.teamdentro.kristwallet.exception.SelfSendException;
import net.teamdentro.kristwallet.exception.UnknownException;
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
        final CurrentAccount account = AccountManager.instance.currentAccount;
        EditText sendAmount = (EditText) view.findViewById(R.id.sendAmount);
        EditText recipient = (EditText) view.findViewById(R.id.sendRecipient);
        long amount;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(sendAmount.getWindowToken(), 0);

        if (StringUtils.isNumeric(sendAmount.getText())) {
            try {
                amount = Long.parseLong(String.valueOf(sendAmount.getText()));
                account.send(amount, String.valueOf(recipient.getText()), new TaskCallback() {
                    @Override
                    public void onTaskDone() {

                    }

                    @Override
                    public void onTaskFail(Exception e) {
                        if (e instanceof InsufficientFundsException) {
                            error(getActivity().getString(R.string.insufficientFunds),
                                    getActivity().getString(R.string.insufficientFundsDetail, StringUtils.capitalize(account.getNode().currency)));
                        } else if (e instanceof InvalidRecipientException) {
                            error(getActivity().getString(R.string.invalidRecipient),
                                    getActivity().getString(R.string.invalidRecipientDetail));
                        } else if (e instanceof BadValueException) {
                            error(getActivity().getString(R.string.badValue),
                                    getActivity().getString(R.string.badValueDetail, StringUtils.capitalize(account.getNode().currency)));
                        } else if (e instanceof SelfSendException) {
                            error(getActivity().getString(R.string.selfSend),
                                    getActivity().getString(R.string.selfSendDetail, StringUtils.capitalize(account.getNode().currency)));
                        } else if (e instanceof UnknownException) {
                            error(getActivity().getString(R.string.unknownError),
                                    e.getMessage());
                        }
                    }
                });
            } catch (NumberFormatException e) {
                error(getActivity().getString(R.string.badValue),
                        getActivity().getString(R.string.badValueDetail, StringUtils.capitalize(account.getNode().currency)));
            }
        } else {
            error(getActivity().getString(R.string.badValue),
                    getActivity().getString(R.string.badValueDetail, StringUtils.capitalize(account.getNode().currency)));
        }
    }

    private void error(String title, String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_error)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
    }

    private void updateBalance(View view) {
        CurrentAccount account = AccountManager.instance.currentAccount;
        TextView address = (TextView) view.findViewById(R.id.address);
        address.setText(getString(R.string.placeholder, account.getAddress()));

        TextView balance = (TextView) view.findViewById(R.id.balance);
        balance.setText(getString(R.string.balance, account.getBalance(), account.getNode().shorthandCurrency));
    }
}
