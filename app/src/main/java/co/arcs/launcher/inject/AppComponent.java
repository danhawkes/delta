package co.arcs.launcher.inject;

import android.app.Application;

import javax.inject.Singleton;

import co.arcs.launcher.LauncherApp;
import co.arcs.launcher.LauncherService;
import co.arcs.launcher.ui.AppPickerActivity;
import co.arcs.launcher.ui.SelectedAppsActivity;
import co.arcs.launcher.ui.launcher.LauncherController;
import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(LauncherApp thing);
    void inject(LauncherService thing);
    void inject(LauncherController thing);
    void inject(AppPickerActivity thing);
    void inject(SelectedAppsActivity thing);

    Application application();
}
