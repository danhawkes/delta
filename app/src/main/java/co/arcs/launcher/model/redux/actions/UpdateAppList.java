package co.arcs.launcher.model.redux.actions;

import android.content.pm.ResolveInfo;

import java.util.List;

import co.arcs.launcher.redux.ReduxAction;

public class UpdateAppList implements ReduxAction {

    public final List<ResolveInfo> infos;

    public UpdateAppList(List<ResolveInfo> infos) {
        this.infos = infos;
    }
}
