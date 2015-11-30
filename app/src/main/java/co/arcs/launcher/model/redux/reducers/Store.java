package co.arcs.launcher.model.redux.reducers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import co.arcs.launcher.model.App;
import co.arcs.launcher.model.GsonAdaptersModel;
import co.arcs.launcher.model.ImmutableApps;
import co.arcs.launcher.model.ImmutableConfig;
import co.arcs.launcher.model.ImmutableState;
import co.arcs.launcher.model.ImmutableTriggerAreas;
import co.arcs.launcher.model.State;
import co.arcs.launcher.model.TriggerArea;
import co.arcs.redux.impl.RxStore;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class Store extends RxStore<State> {

    private final StatePersister persister;

    private Store(Context context, StatePersister persister) {
        super(new RootReducer(context), persister.load());
        this.persister = persister;
    }

    public static Store newInstance(Context context) {
        return new Store(context, new StatePersister(context));
    }

    @Override
    public void setState(State state) {
        if (!state.equals(getState())) {
            persister.saveAsync(state);
        }
        super.setState(state);
    }

    public List<App> getApps() {
        return new ArrayList<>(getState().getApps().getAll().values());
    }

    public List<App> getSelectedApps() {
        return getState().getApps().getSelected();
    }

    public List<App> getUnselectedApps() {
        List<App> apps = getApps();
        Iterator<App> it = apps.iterator();
        while (it.hasNext()) {
            if (getState().getApps().getSelected().contains(it.next())) {
                it.remove();
            }
        }
        return apps;
    }

    public List<TriggerArea> getTriggerAreas() {
        return getState().getTriggerAreas().getItems();
    }

    // Observables

    public Observable<List<App>> appLists() {
        return changes().map(change -> getApps())
                .startWith(Observable.defer(() -> Observable.just(getApps())));
    }

    public Observable<List<App>> selectedAppLists() {
        return changes().map(change -> getSelectedApps())
                .startWith(Observable.defer(() -> Observable.just(getSelectedApps())));
    }

    public Observable<List<App>> unselectedAppLists() {
        return changes().map(change -> getUnselectedApps())
                .startWith(Observable.defer(() -> Observable.just(getUnselectedApps())));
    }

    public Observable<List<TriggerArea>> triggerAreas() {
        return changes().map(change -> getTriggerAreas())
                .startWith(Observable.defer(() -> Observable.just(getTriggerAreas())));
    }

    private static class StatePersister {

        private final Gson gson;
        private final File stateFile;
        private final PublishSubject<State> statesToSave = PublishSubject.create();

        public StatePersister(Context context) {
            this.gson = new GsonBuilder().registerTypeAdapterFactory(new GsonAdaptersModel())
                    .create();
            this.stateFile = new File(context.getFilesDir(), "state.json");

            // Serialise and throttle save operations to prevent races and disk thrash.
            statesToSave.throttleLast(1, TimeUnit.SECONDS)
                    .observeOn(Schedulers.io())
                    .subscribe(state -> {
                        try {
                            FileWriter writer = new FileWriter(stateFile);
                            gson.toJson(state, writer);
                            writer.close();
                        } catch (IOException | JsonIOException e) {
                            e.printStackTrace();
                        }
                    });
        }

        public State load() {
            try {
                return gson.fromJson(new FileReader(stateFile), State.class);
            } catch (FileNotFoundException | JsonIOException | JsonSyntaxException e) {
                return ImmutableState.builder()
                        .apps(ImmutableApps.builder().build())
                        .triggerAreas(ImmutableTriggerAreas.builder().build())
                        .config(ImmutableConfig.builder().isInitialised(false).build())
                        .build();
            }
        }

        public void saveAsync(State state) {
            statesToSave.onNext(state);
        }
    }
}
