package net.teamdentro.kristwallet.krist;

import android.os.AsyncTask;

import net.teamdentro.kristwallet.util.TaskCallback;

import io.github.apemanzilla.kwallet.KristAPI;
import io.github.apemanzilla.kwallet.types.Transaction;

public class CurrentAccount extends Account {
    private KristAPI api;
    private long balance;
    private Transaction[] transactions;

    public boolean loaded = false;

    public CurrentAccount(int id, String label, String password, Node node) {
        super(id, label, password, node);
    }

    public boolean initialize() {
        String updatedPassword = KristAPI.sha256Hex("KRISTWALLET" + getPassword()) + "-000";
        api = new KristAPI(getNode().url, updatedPassword);

        return refreshNonAsynchronously();
    }

    public void refresh(TaskCallback callback) {
        new RefreshAccountTask(callback).execute();
    }

    private boolean refreshNonAsynchronously() {
        try {
            balance = api.getBalance();
            transactions = api.getTransactions();
            loaded = true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private class RefreshAccountTask extends AsyncTask<Void, Void, Void> {
        private TaskCallback callback;

        public RefreshAccountTask(TaskCallback callback) {
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
