package net.teamdentro.kristwallet.accounts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import net.teamdentro.kristwallet.util.Constants;
import net.teamdentro.kristwallet.util.SimpleCrypto;

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

    public void initialize(Context context, String masterPassword) {
        AccountManager.instance = this;

        master = masterPassword;

        database = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + "(_id INTEGER PRIMARY KEY," +
                "`label` VARCHAR(42) NOT NULL," +
                "`password` VARCHAR(255) NOT NULL)");

        accounts = new ArrayList<>();

        loadAccounts();
    }

    public void loadAccounts() {
        accounts.clear();

        Cursor result = database.rawQuery("SELECT * FROM " + tableName, null);
        if (result.moveToFirst()) {
            do {
                String rawPassword = null;
                try {
                    rawPassword = SimpleCrypto.decrypt(master, result.getString(result.getColumnIndex("password")));
                    if (!rawPassword.startsWith(Constants.pwvr)) {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                Account newAccount = new Account(
                        result.getInt(result.getColumnIndex("_id")),
                        result.getString(result.getColumnIndex("label")),
                        result.getString(result.getColumnIndex("password")),
                        rawPassword);
                accounts.add(newAccount);
            } while (result.moveToNext());
        }
        result.close();
    }

    public void addAccount(String password, String label) {
        String newPassword = null;
        try {
            newPassword = SimpleCrypto.encrypt(master, password);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        SQLiteStatement stmt = database.compileStatement("INSERT INTO " + tableName + " (label, password) VALUES (?, ?)");
        stmt.bindString(1, label);
        stmt.bindString(2, newPassword);

        Account newAccount = new Account((int) stmt.executeInsert(), label, newPassword, password);
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
