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
import android.widget.Spinner;
import android.widget.TabHost;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.adapters.NodeAdapter;
import net.teamdentro.kristwallet.krist.AccountManager;
import net.teamdentro.kristwallet.krist.Node;
import net.teamdentro.kristwallet.util.EmptyTransformationMethod;

public class AccountCreationDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View v = inflater.inflate(R.layout.dialog_account_create, null);

        TabHost tabHost = (TabHost) v.findViewById(R.id.accountTabs);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1");
        tab1.setIndicator(getActivity().getString(R.string.accountTypeLogin), getActivity().getResources().getDrawable(R.drawable.ic_input));
        tab1.setContent(R.id.tab1);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2");
        tab2.setIndicator(getActivity().getString(R.string.accountTypeQR), getActivity().getResources().getDrawable(R.drawable.ic_camera_alt));
        tab2.setContent(R.id.tab2);
        tabHost.addTab(tab2);

        TabHost.TabSpec tab3 = tabHost.newTabSpec("tab3");
        tab3.setIndicator(getActivity().getString(R.string.accountTypeNFC), getActivity().getResources().getDrawable(R.drawable.ic_nfc));
        tab3.setContent(R.id.tab3);
        tabHost.addTab(tab3);

        final CheckBox showPass = (CheckBox) v.findViewById(R.id.showPasswordCheckbox);
        final EditText password = (EditText) v.findViewById(R.id.passwordEditText);
        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setTransformationMethod(showPass.isChecked() ? new EmptyTransformationMethod() : new PasswordTransformationMethod());
            }
        });

        password.setTransformationMethod(new PasswordTransformationMethod());

        final Spinner nodeSpinner = (Spinner) v.findViewById(R.id.nodeSpinner);
        nodeSpinner.setAdapter(new NodeAdapter(getActivity(), AccountManager.instance.getNodes()));

        builder.setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final EditText password = (EditText) v.findViewById(R.id.passwordEditText);
                        final EditText label = (EditText) v.findViewById(R.id.accountLabel);
                        final Spinner nodeSpinner = (Spinner) v.findViewById(R.id.nodeSpinner);

                        AccountManager.instance.addAccount(password.getText().toString(), label.getText().toString(), (Node) nodeSpinner.getSelectedItem());
                    }
                });

        return builder.create();
    }
}
