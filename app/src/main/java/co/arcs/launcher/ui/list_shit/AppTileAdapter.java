package co.arcs.launcher.ui.list_shit;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.raizlabs.universaladapter.ListBasedAdapter;

import java.util.HashMap;
import java.util.Map;

import co.arcs.launcher.model.App;
import co.arcs.launcher.ui.launcher.AppTileViewHolder;
import rx.Subscription;
import rx.subjects.PublishSubject;

public class AppTileAdapter extends ListBasedAdapter<App, AppTileViewHolder> {

    private LayoutInflater inflater;
    private PackageManager pm;
    private PublishSubject<App> dropsSubject = PublishSubject.create();
    private Map<ViewHolder, Subscription> dropsSubscriptions = new HashMap<>();

    public AppTileAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.pm = context.getPackageManager();
    }

    @Override
    protected AppTileViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AppTileViewHolder(inflater, viewGroup);
    }

    @Override
    protected void onBindViewHolder(AppTileViewHolder holder, App app, int i) {
        try {
            Drawable activityIcon = pm.getResourcesForApplication(
                    app.getComponentName().getPackageName()).getDrawable(app.getIcon());
            holder.imageView.setImageDrawable(activityIcon);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        Subscription s = dropsSubscriptions.get(holder);
        if (s != null) {
            s.unsubscribe();
        }
        s = holder.drops().map(o -> app).subscribe(dropsSubject::onNext);
        dropsSubscriptions.put(holder, s);
    }

    public PublishSubject<App> drops() {
        return dropsSubject;
    }
}
