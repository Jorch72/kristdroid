package net.teamdentro.kristwallet.accounts;

import android.os.AsyncTask;

import net.teamdentro.kristwallet.util.Constants;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import io.github.apemanzilla.kwallet.KristAPI;
import io.github.apemanzilla.kwallet.types.Transaction;
import io.github.apemanzilla.kwallet.util.HTTP;

public class CurrentAccount extends Account {
    private KristAPI api;
    private long balance;
    private Transaction[] transactions;

    public CurrentAccount(int id, String label, String password) {
        super(id, label, password);
    }

    public void initialize() {
        String apiLink = null;
        try {
            apiLink = HTTP.readURL(new URL(Constants.syncNode));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String updatedPassword = KristAPI.sha256Hex("KRISTWALLET" + getPassword()) + "-000";
            api = new KristAPI(new URL(apiLink), updatedPassword);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        refreshNonAsynchronously();
    }

    public void refresh() {
        new RefreshAccountTask().execute();
    }

    private void refreshNonAsynchronously() {
        try {
            balance = api.getBalance();
            transactions = api.getTransactions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class RefreshAccountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... _) {
            refreshNonAsynchronously();
            return null;
        }
    }

    public long getBalance() {
        return balance;
    }

    public Transaction[] getTransactions() {
        return transactions;
    }

    public String getAddress() {
        return api.getAddress();
    }

    public KristAPI getAPI() {
        return api;
    }
}
