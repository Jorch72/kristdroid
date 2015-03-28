package net.teamdentro.kristwallet;

import android.app.Application;

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
        System.out.println("KristWallet");
        ACRA.init(this);

        super.onCreate();
    }
}
