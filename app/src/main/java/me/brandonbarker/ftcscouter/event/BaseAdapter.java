package me.brandonbarker.ftcscouter.event;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.widgets.SnackBar;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.brandonbarker.ftcscouter.OOTBApplication;
import me.brandonbarker.ftcscouter.R;


@Accessors(prefix = {"m"})
public abstract class BaseAdapter<T> extends ArrayAdapter<T> {
    @Getter
    private int mAccentColor;
    private List<T> removing;

    public BaseAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        init();
    }

    private void init() {
        this.mAccentColor = ContextCompat.getColor(getContext(), R.color.accent);
        removing = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getViewSecondary(position, convertView, parent);
    }

    @Override
    public void remove(T object) {
        if (removing.contains(object)) {
            return;
        }
        super.remove(object);
    }

    @Override
    public void clear() {
        if (removing.size() > 0) {
            for (int i = 0; i <= getCount(); i++) {
                remove(getItem(i));
            }
        } else {
            super.clear();
        }
    }

    public void removeAnimated(T object, ListView view) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.top_to_down);
        final T remove = object;
        animation.setDuration(500);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                removing.remove(remove);
                remove(remove);
                notifyDataSetChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.getChildAt(getPosition(object)).startAnimation(animation);
    }

    public abstract View getViewSecondary(int position, View convertView, ViewGroup parent);

    protected void onDeleteClick(final T item, final View parent, int id, final String msg) {
        parent.findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonView) {
                final int position = BaseAdapter.this.getPosition(item);
                BaseAdapter.this.removeAnimated(item, (ListView) parent.getParent());
                SnackBar snackBar = new SnackBar(OOTBApplication.getInstance().getCurrentActivity(), msg, "UNDO",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                BaseAdapter.this.insert(item, position);
                            }
                        });
                snackBar.setColorButton(mAccentColor);
                snackBar.show();
            }
        });
    }

    protected void setText(View view, int id, String text) {
        ((TextView) view.findViewById(id)).setText(text);
    }

    protected void updateIcon(View view) {
        ImageView imageView = (ImageView) view;
        Drawable drawable = imageView.getDrawable();
        drawable.setColorFilter(this.mAccentColor, PorterDuff.Mode.SRC_ATOP);
        imageView.setImageDrawable(drawable);
    }
}
