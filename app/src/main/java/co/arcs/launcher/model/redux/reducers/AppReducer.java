package co.arcs.launcher.model.redux.reducers;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.arcs.launcher.model.App;
import co.arcs.launcher.model.ImmutableApp;
import co.arcs.launcher.model.ImmutableApps;
import co.arcs.launcher.model.ImmutableState;
import co.arcs.launcher.model.State;
import co.arcs.launcher.model.redux.actions.ReorderSelectedApps;
import co.arcs.launcher.model.redux.actions.ToggleAppSelected;
import co.arcs.launcher.model.redux.actions.UpdateAppList;
import co.arcs.redux.Reducer;

public class AppReducer implements Reducer<State, Object> {

    private PackageManager pm;

    public AppReducer(Context context) {
        this.pm = context.getPackageManager();
    }

    @Override
    public State reduce(State state, Object action) {
        if (action instanceof UpdateAppList) {
            return updateAppList.reduce(state, (UpdateAppList) action);
        } else if (action instanceof ToggleAppSelected) {
            return toggleAppSelected.reduce(state, (ToggleAppSelected) action);
        } else if (action instanceof ReorderSelectedApps) {
            return reorderApps.reduce(state, (ReorderSelectedApps) action);
        } else {
            return state;
        }
    }

    private final Reducer<State, UpdateAppList> updateAppList = (state, action) -> {

        ImmutableState.Builder stateBuilder = ImmutableState.builder();
        if (state != null) {
            stateBuilder = stateBuilder.from(state);
        }

        ImmutableApps.Builder appsBuilder = ImmutableApps.builder();

        // Update and collect new app list
        Map<String, App> newAll = new HashMap<>();
        for (ResolveInfo info : action.infos) {
            String componentName = new ComponentName(info.activityInfo.packageName,
                    info.activityInfo.name).flattenToShortString();
            String label = info.loadLabel(pm).toString();
            int icon = info.getIconResource();

            ImmutableApp app = ImmutableApp.builder()
                    .identifier(componentName)
                    .label(label)
                    .icon(icon)
                    .componentNameString(componentName)
                    .build();

            newAll.put(app.getIdentifier(), app);
        }
        appsBuilder.all(newAll);

        // Remove apps that have been deleted/disabled.
        List<App> newSelected = new ArrayList<>();
        if (state != null) {
            List<App> selected = state.getApps().getSelected();
            for (App oldSelectedApp : selected) {
                App newSel = newAll.get(oldSelectedApp.getIdentifier());
                if (newSel != null) {
                    newSelected.add(newSel);
                }
            }
        }
        appsBuilder.selected(newSelected);

        return stateBuilder.apps(appsBuilder.build()).build();
    };

    private final Reducer<State, ToggleAppSelected> toggleAppSelected = (state, action) -> {

        List<App> newSelected = new ArrayList<>();
        boolean selectionRemoved = false;
        for (App selectedApp : state.getApps().getSelected()) {
            if (selectedApp.getIdentifier().equals(action.appIdentifier)) {
                selectionRemoved = true;
            } else {
                newSelected.add(selectedApp);
            }
        }
        if (!selectionRemoved) {
            newSelected.add(state.getApps().getAll().get(action.appIdentifier));
        }

        return ImmutableState.copyOf(state)
                .withApps(ImmutableApps.copyOf(state.getApps()).withSelected(newSelected));
    };

    private final Reducer<State, ReorderSelectedApps> reorderApps = (state, action) -> {

        List<App> newSelected = new ArrayList<>(state.getApps().getSelected());
        App moved = newSelected.remove(action.a);
        newSelected.add(action.b, moved);

        return ImmutableState.copyOf(state)
                .withApps(ImmutableApps.copyOf(state.getApps()).withSelected(newSelected));
    };
}