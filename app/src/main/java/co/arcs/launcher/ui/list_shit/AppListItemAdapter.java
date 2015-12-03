package co.arcs.launcher.ui.list_shit;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.LegacySwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.jakewharton.rxbinding.view.RxView;
import com.raizlabs.coreutils.view.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.arcs.launcher.model.App;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Action3;
import rx.subjects.PublishSubject;

import static co.arcs.launcher.utils.RxUtils.defaultSchedulers;

public class AppListItemAdapter extends Adapter<AppListItemViewHolder> implements
        DraggableItemAdapter<AppListItemViewHolder>, LegacySwipeableItemAdapter<AppListItemViewHolder> {

    private final PackageManager pm;
    private final LayoutInflater inflater;
    private final boolean enableMovement;
    private final List<App> items = new ArrayList<>();
    private PublishSubject<App> clicksSubject = PublishSubject.create();
    private Map<ViewHolder, Subscription> clickSubscriptions = new HashMap<>();
    private Map<ViewHolder, Subscription> imageLoadSubscriptions = new HashMap<>();
    private Map<ViewHolder, Integer> imageLoadIds = new HashMap<>();
    Action3<App, Integer, Integer> moveItem;
    Action1<App> removeItem;

    public AppListItemAdapter(Context context, boolean enableMovement, Action3<App, Integer, Integer> moveItem, Action1<App> removeItem) {
        this.pm = context.getPackageManager();
        this.inflater = LayoutInflater.from(context);
        this.enableMovement = enableMovement;
        this.moveItem = moveItem;
        this.removeItem = removeItem;
        setHasStableIds(true);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).hashCode();
    }

    @Override
    public AppListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppListItemViewHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(AppListItemViewHolder holder, int position) {
        App app = items.get(position);

        holder.t1.setText(app.getLabel());
        holder.dragHandle.setVisibility(enableMovement ? View.VISIBLE : View.INVISIBLE);

        // Icon
        Integer id = imageLoadIds.get(holder);
        if (id == null || id != app.getIcon()) {
            holder.icon.setImageDrawable(null);
            Subscription loadSubscription = imageLoadSubscriptions.get(holder);
            if (loadSubscription != null) {
                loadSubscription.unsubscribe();
            }
            loadSubscription = loadIcon(app).subscribe(holder.icon::setImageDrawable);
            imageLoadSubscriptions.put(holder, loadSubscription);
            imageLoadIds.put(holder, app.getIcon());
        }

        // Clicks
        Subscription clicksSubscription = clickSubscriptions.get(holder);
        if (clicksSubscription != null) {
            clicksSubscription.unsubscribe();
        }
        clicksSubscription = RxView.clicks(holder.itemView)
                .subscribe(click -> clicksSubject.onNext(app));
        clickSubscriptions.put(holder, clicksSubscription);
    }

    public void setItems(List<App> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public PublishSubject<App> clicks() {
        return clicksSubject;
    }

    private Observable<Drawable> loadIcon(App app) {
        return Observable.<Drawable>create(s -> {
            try {
                if (s.isUnsubscribed()) {
                    return;
                }
                Resources resources = pm.getResourcesForApplication(
                        app.getComponentName().getPackageName());
                if (s.isUnsubscribed()) {
                    return;
                }
                Drawable drawable = resources.getDrawable(app.getIcon());
                if (s.isUnsubscribed()) {
                    return;
                }
                s.onNext(drawable);
                s.onCompleted();
            } catch (NameNotFoundException e) {
                s.onError(e);
            }
        }).compose(defaultSchedulers());
    }

    // Drag-drop

    @Override
    public boolean onCheckCanStartDrag(AppListItemViewHolder viewHolder, int position, int x, int y) {
        return enableMovement && (x > (viewHolder.itemView.getWidth() - ViewUtils.dipsToPixels(64,
                viewHolder.itemView)));
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(AppListItemViewHolder viewHolder, int i) {
        return null;
    }

    @Override
    public void onMoveItem(int oldPosition, int newPosition) {
        moveItem.call(items.get(oldPosition), oldPosition, newPosition);
    }

    // Swipe

    @Override
    public int onGetSwipeReactionType(AppListItemViewHolder holder, int position, int x, int y) {
        return RecyclerViewSwipeManager.REACTION_CAN_NOT_SWIPE_LEFT_WITH_RUBBER_BAND_EFFECT | RecyclerViewSwipeManager.REACTION_CAN_SWIPE_RIGHT;
    }

    @Override
    public void onSetSwipeBackground(AppListItemViewHolder appListItemViewHolder, int position, int type) {
    }

    @Override
    public int onSwipeItem(AppListItemViewHolder holder, int position, int result) {
        if (result == RecyclerViewSwipeManager.RESULT_SWIPED_RIGHT) {
            return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM;
        } else {
            return RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_DEFAULT;
        }
    }

    @Override
    public void onPerformAfterSwipeReaction(AppListItemViewHolder holder, int position, int result, int reaction) {
        if (reaction == RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM) {
            removeItem.call(items.get(position));
        }
    }
}


