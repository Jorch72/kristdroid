package net.teamdentro.kristwallet.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.teamdentro.kristwallet.R;

public class Transactions extends Fragment {
    public static Fragment newInstance(Context context) {
        Transactions fragment = new Transactions();
        return fragment;
    }

    public Transactions() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transactions, container, false);
    }

}
