package net.teamdentro.kristwallet.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.activities.LoginActivity;

public class MasterPasswordEnter extends Fragment {
    public static Fragment newInstance(Context context) {
        return new MasterPasswordEnter();
    }

    public MasterPasswordEnter() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_password, container, false);
        final LoginActivity loginActivity = (LoginActivity) getActivity();

        final EditText password = (EditText) view.findViewById(R.id.masterPassword);
        Button enterButton = (Button) view.findViewById(R.id.enterPasswordButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                loginActivity.attemptAccess(password.getText().toString());
            }
        });

        return view;
    }
}
