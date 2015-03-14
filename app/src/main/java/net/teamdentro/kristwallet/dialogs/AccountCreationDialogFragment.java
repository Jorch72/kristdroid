package net.teamdentro.kristwallet.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.accounts.AccountManager;
import net.teamdentro.kristwallet.util.EmptyTransformationMethod;

public class AccountCreationDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View v = inflater.inflate(R.layout.dialog_account_create, null);

        final CheckBox showPass = (CheckBox) v.findViewById(R.id.showPasswordCheckbox);
        final EditText password = (EditText) v.findViewById(R.id.passwordEditText);
        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setTransformationMethod(showPass.isChecked() ? new EmptyTransformationMethod() : new PasswordTransformationMethod());
            }
        });

        password.setTransformationMethod(new PasswordTransformationMethod());

        builder.setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final EditText password = (EditText) v.findViewById(R.id.passwordEditText);
                        final EditText label = (EditText) v.findViewById(R.id.accountLabel);

                        AccountManager.instance.addAccount(password.getText().toString(), label.getText().toString());
                    }
                });

        return builder.create();
    }
}
