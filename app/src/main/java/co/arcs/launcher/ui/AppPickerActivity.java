package co.arcs.launcher.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.arcs.launcher.LauncherApp;
import co.arcs.launcher.R;
import co.arcs.launcher.model.redux.reducers.Store;
import co.arcs.launcher.ui.list_shit.AppListItemAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppPickerActivity extends RxAppCompatActivity {

    public static final String RESULT_APP_IDENTIFIER = "appId";

    @Inject Store store;
    @Bind(R.id.recycler) RecyclerView recyclerView;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((LauncherApp) getApplicationContext()).getAppComponent().inject(this);
        setContentView(R.layout.act_app_picker);
        ButterKnife.bind(this);

        toolbar.inflateMenu(R.menu.act_app_picker);
        SearchView searchView = (SearchView) toolbar.getMenu()
                .findItem(R.id.search)
                .getActionView();

        AppListItemAdapter adapter = new AppListItemAdapter(this, false, null, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Observable.combineLatest(store.unselectedAppLists().subscribeOn(Schedulers.io()),
                RxSearchView.queryTextChanges(searchView), (apps, text) -> {
                    return Observable.from(apps)
                            .filter(app -> app.getLabel().toLowerCase().contains(text.toString()))
                            .toList();
                })
                .flatMap(o -> o)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter::setItems);

        adapter.clicks().compose(bindToLifecycle()).subscribe(app -> {
            Intent data = new Intent();
            data.putExtra(RESULT_APP_IDENTIFIER, app.getIdentifier());
            setResult(Activity.RESULT_OK, data);
            finish();
        });
    }
}
