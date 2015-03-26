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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.accounts.AccountManager;
import net.teamdentro.kristwallet.accounts.CurrentAccount;
import net.teamdentro.kristwallet.activities.LoginActivity;
import net.teamdentro.kristwallet.adapters.TransactionsAdapter;
import net.teamdentro.kristwallet.util.FragmentCallback;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

import io.github.apemanzilla.kwallet.types.Transaction;

public class MasterPasswordCreate extends Fragment {
    public static Fragment newInstance(Context context) {
        return new MasterPasswordCreate();
    }

    public MasterPasswordCreate() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_create, container, false);
        final LoginActivity loginActivity = (LoginActivity) getActivity();

        final EditText password = (EditText)view.findViewById(R.id.masterPasswordCreate);
        Button createButton = (Button)view.findViewById(R.id.enterPasswordButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActivity.access(password.getText().toString());
            }
        });

        return view;
    }
}
