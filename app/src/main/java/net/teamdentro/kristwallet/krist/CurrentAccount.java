package net.teamdentro.kristwallet.krist;

import android.os.AsyncTask;

import net.teamdentro.kristwallet.exception.InsufficientFundsException;
import net.teamdentro.kristwallet.exception.InvalidFundsException;
import net.teamdentro.kristwallet.util.TaskCallback;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

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

    private boolean refreshNonAsynchronously() throws Exception {
        try {
            balance = api.getBalance();
            transactions = api.getTransactions();
            loaded = true;
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    public void refreshBalance(TaskCallback callback) {
        new RefreshBalanceTask(callback).execute();
    }

    public void send(long amount, String recipient, TaskCallback callback) {
        new SendTask(amount, recipient, callback).execute();
    }

    private class SendTask extends AsyncTask<Void, Void, Void> {
        private TaskCallback callback;
        private long amount;
        private String recipient;

        public SendTask(long amount, String recipient, TaskCallback callback) {
            this.amount = amount;
            this.recipient = recipient;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... _) {
            if (amount > 0 && amount <= balance) {
                try {
                    api.sendKrist(amount, recipient);
                } catch (IOException e) {
                    callback.onTaskFail(e);
                }
            } else if (amount <= balance) {
                callback.onTaskFail(new InsufficientFundsException());
            } else {
                callback.onTaskFail(new InvalidFundsException());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (callback != null)
                callback.onTaskDone();
        }
    }

    private class RefreshAccountTask extends AsyncTask<Void, Void, Void> {
        private TaskCallback callback;
        private Exception e;

        public RefreshAccountTask(TaskCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... _) {
            try {
                refreshNonAsynchronously();
            } catch (Exception e) {
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (callback != null) {
                if (e == null)
                    callback.onTaskDone();
                else
                    callback.onTaskFail(e);
            }
        }
    }

    private class RefreshBalanceTask extends AsyncTask<Void, Void, Void> {
        private TaskCallback callback;

        public RefreshBalanceTask(TaskCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... _) {
            try {
                balance = api.getBalance();
            } catch (IOException e) {
                callback.onTaskFail(e);
            }
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
