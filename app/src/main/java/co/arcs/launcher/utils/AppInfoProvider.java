package co.arcs.launcher.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

public class AppInfoProvider {

    private final PackageManager pm;

    public AppInfoProvider(Context context) {
        this.pm = context.getPackageManager();
    }

    public List<ResolveInfo> getApplicationInfo() {

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> ri = pm.queryIntentActivities(i, PackageManager.GET_META_DATA);
        return ri;
    }
}
