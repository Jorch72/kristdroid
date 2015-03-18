package net.teamdentro.kristwallet.accounts;

import android.os.AsyncTask;

import net.teamdentro.kristwallet.util.Constants;
import net.teamdentro.kristwallet.util.FragmentCallback;

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

    public boolean initialize() {
        String apiLink = null;
        try {
            apiLink = HTTP.readURL(new URL(Constants.syncNode));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try {
            String updatedPassword = KristAPI.sha256Hex("KRISTWALLET" + getPassword()) + "-000";
            api = new KristAPI(new URL(apiLink), updatedPassword);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }

        return refreshNonAsynchronously();
    }

    public void refresh(FragmentCallback callback) {
        new RefreshAccountTask(callback).execute();
    }

    private boolean refreshNonAsynchronously() {
        try {
            balance = api.getBalance();
            transactions = api.getTransactions();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private class RefreshAccountTask extends AsyncTask<Void, Void, Void> {
        private FragmentCallback callback;

        public RefreshAccountTask(FragmentCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... _) {
            refreshNonAsynchronously();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (callback != null)
                callback.onTaskDone();
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
