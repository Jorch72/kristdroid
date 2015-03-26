package net.teamdentro.kristwallet;

import android.app.Application;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(
        formKey = "",
        formUri = "https://collector.tracepot.com/17acc0d0"
)

public class KristWallet extends Application {
    @Override
    public void onCreate() {
        System.out.println("KristWallet");
        ACRA.init(this);

        super.onCreate();
    }
}
