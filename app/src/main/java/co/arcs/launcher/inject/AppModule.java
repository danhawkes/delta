package co.arcs.launcher.inject;

import android.app.Application;

import javax.inject.Singleton;

import co.arcs.launcher.model.redux.reducers.Store;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application application() {
        return app;
    }

    @Provides
    @Singleton
    Store provideStore() {
        return Store.newInstance(app);
    }
}
