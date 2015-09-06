package co.arcs.launcher;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import co.arcs.launcher.inject.AppComponent;
import co.arcs.launcher.inject.AppModule;
import co.arcs.launcher.inject.DaggerAppComponent;
import co.arcs.launcher.model.redux.actions.Init;
import co.arcs.launcher.model.redux.actions.UpdateAppList;
import co.arcs.launcher.model.redux.reducers.Store;
import co.arcs.launcher.utils.AppInfoProvider;

public class LauncherApp extends Application {

    private AppComponent appComponent;
    @Inject Store store;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);

        startService(new Intent(this, LauncherService.class));

        if (!store.getState().getConfig().isInitialised()) {
            store.dispatch(new Init());
        }

        store.dispatch(new UpdateAppList(new AppInfoProvider(this).getApplicationInfo()));
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static LauncherApp from(Context context) {
        return (LauncherApp) context.getApplicationContext();
    }
}
