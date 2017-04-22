package hrv.band.app.view.presenter;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Copyright (c) 2017
 * Created by Thomas on 22.04.2017.
 */

public class AboutPresenter implements IAboutPresenter {
    private Activity activity;

    public AboutPresenter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public String getVersion() {
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(
                    activity.getPackageName(), 0);
            return versionToString(pInfo);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(e.getClass().getName(), "NameNotFoundException", e);
        }
        return "";
    }

    @NonNull
    private String versionToString(PackageInfo pInfo) {
        return "Version: " + pInfo.versionName + " (" + pInfo.versionCode + ")";
    }
}
