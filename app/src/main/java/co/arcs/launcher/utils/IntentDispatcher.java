package co.arcs.launcher.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class IntentDispatcher {

    public static void startApp(Context context, ComponentName componentName) {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        i.setComponent(componentName);
        context.startActivity(i);
    }
}
