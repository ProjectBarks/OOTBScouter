package me.brandonbarker.ftcscouter.match;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import me.brandonbarker.ftcscouter.OOTBApplication;
import me.brandonbarker.ftcscouter.R;
import me.brandonbarker.ftcscouter.event.BaseAdapter;
import me.brandonbarker.ftcscouter.event.Event;

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
        circleView.setBackgroundResource(alliance.getColor() == AllianceColor.BLUE ? R.drawable.blue_circle : R.drawable.red_circle);
        circleView.setText(alliance.getColor() == AllianceColor.BLUE ? "B" : "R");
        return view;
    }

    protected void onRenameClick(final Alliance alliance, View parent) {
        parent.findViewById(R.id.matchRename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog.SingleButtonCallback callback = new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction action) {
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
                        .customView(R.layout.dialog_input_match, false)
                        .build()
                        .show();
            }
        });
    }
}
