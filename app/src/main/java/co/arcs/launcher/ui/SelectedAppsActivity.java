package co.arcs.launcher.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.arcs.launcher.LauncherApp;
import co.arcs.launcher.R;
import co.arcs.launcher.model.redux.actions.ReorderSelectedApps;
import co.arcs.launcher.model.redux.actions.ToggleAppSelected;
import co.arcs.launcher.model.redux.reducers.Store;
import co.arcs.launcher.ui.list_shit.AppListItemAdapter;

import static co.arcs.launcher.utils.RxUtils.defaultSchedulers;

public class SelectedAppsActivity extends RxAppCompatActivity {

    public static final int PICK_APP_REQUEST_CODE = 1000;

    @Inject Store store;
    @Bind(R.id.recycler) RecyclerView recyclerView;
    @Bind(R.id.add) View addButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((LauncherApp) getApplicationContext()).getAppComponent().inject(this);
        setContentView(R.layout.act_selected_apps);
        ButterKnife.bind(this);

        RecyclerViewDragDropManager dragDropManager = new RecyclerViewDragDropManager();
        dragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) getResources().getDrawable(R.drawable.ms9_ambient_shadow_z18));

        RecyclerViewTouchActionGuardManager touchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        touchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        touchActionGuardManager.setEnabled(true);

        RecyclerViewSwipeManager swipeManager = new RecyclerViewSwipeManager();

        AppListItemAdapter adapter = new AppListItemAdapter(this, true, (app, before, after) -> {
            store.dispatch(new ReorderSelectedApps(before, after));
        }, app -> {
            store.dispatch(new ToggleAppSelected(app.getIdentifier()));
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setItemAnimator(new RefactoredDefaultItemAnimator());

        recyclerView.setAdapter(
                swipeManager.createWrappedAdapter(dragDropManager.createWrappedAdapter(adapter)));

        swipeManager.attachRecyclerView(recyclerView);
        dragDropManager.attachRecyclerView(recyclerView);

        store.selectedAppLists()
                .compose(defaultSchedulers())
                .compose(bindToLifecycle())
                .subscribe(adapter::setItems);

        RxView.clicks(addButton).compose(bindToLifecycle()).subscribe(click -> {
            startActivityForResult(new Intent(SelectedAppsActivity.this, AppPickerActivity.class),
                    PICK_APP_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_APP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String appIdentifier = data.getStringExtra(AppPickerActivity.RESULT_APP_IDENTIFIER);
            store.dispatch(new ToggleAppSelected(appIdentifier));
        }
    }
}
