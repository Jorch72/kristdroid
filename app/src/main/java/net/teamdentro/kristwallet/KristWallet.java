package net.teamdentro.kristwallet;

import android.app.Application;
import android.util.Log;

import net.teamdentro.kristwallet.util.Constants;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(
        formKey = "",
        formUri = "https://collector.tracepot.com/17acc0d0",
        mode = ReportingInteractionMode.DIALOG,
        resDialogText = R.string.acraDialog
)

public class KristWallet extends Application {
    @Override
    public void onCreate() {
        Log.d(Constants.krist, "KristWallet loaded");
        ACRA.init(this);

        super.onCreate();
    }
}
