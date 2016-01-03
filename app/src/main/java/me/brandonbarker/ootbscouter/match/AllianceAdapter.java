package me.brandonbarker.ootbscouter.match;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

import me.brandonbarker.ootbscouter.OOTBApplication;
import me.brandonbarker.ootbscouter.R;
import me.brandonbarker.ootbscouter.event.BaseAdapter;
import me.brandonbarker.ootbscouter.event.Event;

/**
 * Created by brandon on 1/1/15.
 */
public class AllianceAdapter extends BaseAdapter<Alliance> {

    public AllianceAdapter(Event event, Context context) {
        this(event.getAlliances(), context);
    }

    public AllianceAdapter(List<Alliance> alliances, Context context) {
        super(context, 0, alliances);
    }

    @Override
    public View getViewSecondary(int position, View view, ViewGroup parent) {
        Alliance alliance = getItem(position);
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_match, parent, false);
        }
        updateIcon(view.findViewById(R.id.matchDeleteImage));
        updateIcon(view.findViewById(R.id.matchRenameImage));
        setText(view, R.id.matchTitle, String.format("Match #%s", alliance.getMatchNumber()));
        setText(view, R.id.matchTeams, String.format("%s & %s", alliance.getEntryOne().getTeamNumber(), alliance.getEntryTwo().getTeamNumber()));
        onDeleteClick(alliance, view, R.id.matchDelete, "Match deleted from system!");
        onRenameClick(alliance, view);

        TextView circleView = (TextView) view.findViewById(R.id.matchColor);
        boolean isBlue = alliance.getColor() == AllianceColor.BLUE;
        Drawable drawable = getContext().getResources().getDrawable(isBlue ? R.drawable.blue_circle : R.drawable.red_circle);
        if (Build.VERSION.SDK_INT >= 16) {
            circleView.setBackground(drawable);
        } else {
            circleView.setBackgroundDrawable(drawable);
        }
        circleView.setText(isBlue ? "B" : "R");
        return view;
    }

    protected void onRenameClick(final Alliance alliance, View parent) {
        parent.findViewById(R.id.matchRename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog.SimpleCallback callback = new MaterialDialog.SimpleCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        alliance.setMatchNumber(Integer.parseInt(getMatchNumberRaw(dialog)));
                        alliance.setColor(isRed(dialog) ? AllianceColor.RED : AllianceColor.BLUE);
                        alliance.getEntryOne().setTeamNumber(Integer.parseInt(((TextView) dialog.findViewById(R.id.team1)).getText().toString()));
                        alliance.getEntryTwo().setTeamNumber(Integer.parseInt(((TextView) dialog.findViewById(R.id.team2)).getText().toString()));
                        AllianceAdapter.this.notifyDataSetChanged();
                    }

                    private String getMatchNumberRaw(MaterialDialog dialog) {
                        return ((TextView) dialog.findViewById(R.id.matchNumber)).getText().toString();
                    }

                    private boolean isRed(MaterialDialog dialog) {
                        return ((RadioButton) dialog.findViewById(R.id.matchRed)).isChecked();
                    }
                };
                OOTBApplication.getDefaultDialog(OOTBApplication.getInstance().getCurrentActivity(), "Rename this event?", callback)
                        .title("Edit match")
                        .positiveText("Edit")
                        .customView(R.layout.dialog_input_match)
                        .build()
                        .show();
            }
        });
    }
}
