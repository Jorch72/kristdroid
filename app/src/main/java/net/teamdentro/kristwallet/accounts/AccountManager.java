package net.teamdentro.kristwallet.accounts;

import android.content.Context;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteStatement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AccountManager {
    public static AccountManager instance;

    private static final String databaseName = "kristwallet";
    private static final String tableName = "kwal_accs";

    private String master;

    private SQLiteDatabase database;
    private ArrayList<Account> accounts;

    private OnAccountCreatedListener accountCreationListener;

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public CurrentAccount currentAccount = null;

    public interface OnAccountCreatedListener {
        public void onEvent();
    }

    public void setOnAccountCreatedListener(OnAccountCreatedListener eventListener) {
        accountCreationListener = eventListener;
    }

    public boolean initialize(Context context, String masterPassword) throws SQLiteException {
        AccountManager.instance = this;

        master = masterPassword;

        SQLiteDatabase.loadLibs(context);
        File databaseFile = context.getDatabasePath("kristwallet.db");
        databaseFile.getParentFile().mkdirs();
        try {
            databaseFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            database = SQLiteDatabase.openOrCreateDatabase(databaseFile, masterPassword, null);
        } catch (SQLiteException e) {
            throw e;
        }
        database.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + "(_id INTEGER PRIMARY KEY," +
                "`label` VARCHAR(42) NOT NULL," +
                "`password` VARCHAR(255) NOT NULL)");

        accounts = new ArrayList<>();

        return loadAccounts();
    }

    public boolean loadAccounts() {
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
        result.close();
        return true;
    }

    public void addAccount(String password, String label) {
        SQLiteStatement stmt = database.compileStatement("INSERT INTO " + tableName + " (label, password) VALUES (?, ?)");
        stmt.bindString(1, label);
        stmt.bindString(2, password);

        Account newAccount = new Account((int) stmt.executeInsert(), label, password);
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
}
