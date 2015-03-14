package net.teamdentro.kristwallet.accounts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AccountManager {
    public static AccountManager instance;

    private static final String databaseName = "kristwallet";
    private static final String tableName = "kwal_accs";

    private SQLiteDatabase database;
    private ArrayList<Account> accounts;

    private String key = "test";

    private OnAccountCreatedListener accountCreationListener;

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public interface OnAccountCreatedListener {
        public void onEvent();
    }

    public void setOnAccountCreatedListener(OnAccountCreatedListener eventListener) {
        accountCreationListener=eventListener;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void initialize(Context context) {
        AccountManager.instance = this;

        database = context.openOrCreateDatabase(databaseName, context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + "(_id INTEGER PRIMARY KEY," +
                "`label` VARCHAR(42) NOT NULL," +
                "`password` VARCHAR(255) NOT NULL)");

        accounts = new ArrayList<Account>();

        loadAccounts();
    }

    public void loadAccounts() {
        accounts.clear();

        Cursor result = database.rawQuery("SELECT * FROM " + tableName, null);
        if (result.moveToFirst()) {
            do {
                Account newAccount = new Account(
                        result.getInt(result.getColumnIndex("_id")),
                        result.getString(result.getColumnIndex("label")),
                        result.getString(result.getColumnIndex("password")));
                accounts.add(newAccount);
            } while (result.moveToNext());
        }
    }

    public void addAccount(String password, String label) {
        SQLiteStatement stmt = database.compileStatement("INSERT INTO " + tableName + " (label, password) VALUES (?, ?)");
        stmt.bindString(1, label);
        stmt.bindString(2, password);

        Account newAccount = new Account((int) stmt.executeInsert(), label, password);
        accounts.add(newAccount);

        if(accountCreationListener != null)
            accountCreationListener.onEvent();
    }

    public void deleteAccount(Account account) {
        database.delete(tableName, "_id = ?", new String[] {String.valueOf(account.getID())});
        accounts.remove(account);

        if(accountCreationListener != null)
            accountCreationListener.onEvent();
    }
}
