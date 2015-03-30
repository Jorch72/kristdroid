package net.teamdentro.kristwallet.krist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteStatement;
import net.teamdentro.kristwallet.activities.LoginActivity;
import net.teamdentro.kristwallet.exception.InvalidNodeException;
import net.teamdentro.kristwallet.util.Constants;
import net.teamdentro.kristwallet.util.JSONParser;
import net.teamdentro.kristwallet.util.TaskCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

public class AccountManager {
    public static AccountManager instance;

    private static final String databaseName = "kristwallet";
    private static final String tableName = "kwal_accs";

    private String master;

    private SQLiteDatabase database;
    private ArrayList<Account> accounts;
    private TreeMap<String, Node> nodes;

    private OnAccountCreatedListener accountCreationListener;

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public ArrayList<Node> getNodes() {
        Collection<Node> values = nodes.values();
        return new ArrayList<>(values);
    }

    public Node getNode(String unique) {
        return nodes.get(unique);
    }

    public CurrentAccount currentAccount = null;

    public void reset(Context context) {
        File databaseFile = context.getDatabasePath("kristwallet.db");
        databaseFile.delete();

        Intent activity = new Intent(context, LoginActivity.class);
        int pendingId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, pendingId, activity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    public static boolean databaseExists(Context context) {
        File databaseFile = context.getDatabasePath("kristwallet.db");
        return databaseFile.exists();
    }

    public interface OnAccountCreatedListener {
        public void onEvent();
    }

    public void setOnAccountCreatedListener(OnAccountCreatedListener eventListener) {
        accountCreationListener = eventListener;
    }

    public void initialize(Context context, String masterPassword, final TaskCallback callback) throws SQLiteException {
        AccountManager.instance = this;

        master = masterPassword;

        SQLiteDatabase.loadLibs(context);
        File databaseFile = context.getDatabasePath("kristwallet.db");
        databaseFile.getParentFile().mkdirs();
        try {
            databaseFile.createNewFile();
        } catch (IOException e) {
            callback.onTaskFail(e);
            return;
        }
        try {
            database = SQLiteDatabase.openOrCreateDatabase(databaseFile, masterPassword, null);
        } catch (SQLiteException e) {
            throw e;
        }

        database.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + "(_id INTEGER PRIMARY KEY," +
                "`label` VARCHAR(42) NOT NULL," +
                "`password` VARCHAR(255) NOT NULL," +
                "`node` VARCHAR(64) NOT NULL)");

        accounts = new ArrayList<>();

        loadNodes(new TaskCallback() {
            @Override
            public void onTaskDone() {
                try {
                    loadAccounts();
                    callback.onTaskDone();
                } catch (InvalidNodeException e) {
                    callback.onTaskFail(e);
                }
            }
        });
    }

    public boolean loadAccounts() throws InvalidNodeException {
        accounts.clear();

        Cursor result = database.rawQuery("SELECT * FROM " + tableName, null);
        if (result.moveToFirst()) {
            do {
                Node node = getNode(result.getString(result.getColumnIndex("node")));
                if (node == null) {
                    result.close();
                    throw new InvalidNodeException();
                }
                Account newAccount = new Account(
                        result.getInt(result.getColumnIndex("_id")),
                        result.getString(result.getColumnIndex("label")),
                        result.getString(result.getColumnIndex("password")), node);
                accounts.add(newAccount);
            } while (result.moveToNext());
        }
        result.close();
        return true;
    }

    public void addAccount(String password, String label, Node node) {
        SQLiteStatement stmt = database.compileStatement("INSERT INTO " + tableName + " (label, password, node) VALUES (?, ?, ?)");
        stmt.bindString(1, label);
        stmt.bindString(2, password);
        stmt.bindString(3, node.unique);

        Account newAccount = new Account((int) stmt.executeInsert(), label, password, node);
        accounts.add(newAccount);

        if (accountCreationListener != null)
            accountCreationListener.onEvent();
    }

    public void deleteAccount(Account account) {
        database.delete(tableName, "_id = ?", new String[]{String.valueOf(account.getID())});
        accounts.remove(account);

        if (accountCreationListener != null)
            accountCreationListener.onEvent();
    }

    public void loadNodes(TaskCallback callback) {
        new LoadNodesTask(callback).execute();
    }

    private class LoadNodesTask extends AsyncTask<Void, Void, Void> {
        private TaskCallback callback;

        public LoadNodesTask(TaskCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... _) {
            loadNodesNonAsynchronously();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (callback != null)
                callback.onTaskDone();
        }
    }

    public void loadNodesNonAsynchronously() {
        nodes = new TreeMap<String, Node>();

        JSONParser parser = new JSONParser();
        JSONObject object = parser.fromUrl(Constants.syncNode);

        try {
            JSONArray noods = object.getJSONArray("nodes");
            for (int i = 0; i < noods.length(); i++) {
                JSONObject obj = noods.getJSONObject(i);

                Node newNode = new Node(
                        obj.getString("name"),
                        obj.getString("hosted"),
                        obj.getString("owner"),
                        new URL(obj.getString("url")),
                        obj.getString("unique"),
                        obj.getString("currency"),
                        obj.getString("shorthandcurrency"));

                nodes.put(obj.getString("unique"), newNode);
            }
        } catch (JSONException e) {
            Log.e(Constants.krist, "Something weird be going on with that node list");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            Log.e(Constants.krist, "Something weird be going on with that url");
            e.printStackTrace();
        }
    }
}
