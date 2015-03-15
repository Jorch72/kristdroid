package net.teamdentro.kristwallet.accounts;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import net.teamdentro.kristwallet.R;
import net.teamdentro.kristwallet.util.Constants;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import io.github.apemanzilla.kwallet.KristAPI;
import io.github.apemanzilla.kwallet.util.HTTP;

public class CurrentAccount extends Account {
    private Context context;

    private KristAPI api;

    public CurrentAccount(Context context, int id, String label, String password) {
        super(id, label, password);
        this.context = context;
    }

    public void initialize() {
        String apiLink = null;
        try {
            apiLink = HTTP.readURL(new URL(Constants.syncNode));
        } catch (MalformedURLException e) {
            new AlertDialog.Builder(context).setMessage(context.getString(R.string.apiError)).setNeutralButton(android.R.string.ok, null).show();
            e.printStackTrace();
        } catch (IOException e) {
            new AlertDialog.Builder(context).setMessage(context.getString(R.string.apiError)).setNeutralButton(android.R.string.ok, null).show();
            e.printStackTrace();
        }

        try{
            api = new KristAPI(new URL(apiLink), getPassword());
        } catch (MalformedURLException e) {
            new AlertDialog.Builder(context).setMessage(context.getString(R.string.apiError)).setNeutralButton(android.R.string.ok, null).show();
            e.printStackTrace();
        }
    }

    public long getBalance() {
        try {
            return api.getBalance();
        } catch (IOException e) {
            Toast.makeText(context, R.string.balanceError, Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
        return 0;
    }

    public String getAddress() {
        return api.getAddress();
    }

    public KristAPI getAPI() {
        return api;
    }
}
