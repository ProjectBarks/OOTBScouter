package me.brandonbarker.ftcscouter.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import me.brandonbarker.ftcscouter.MatchActivity;
import me.brandonbarker.ftcscouter.OOTBApplication;
import me.brandonbarker.ftcscouter.R;
import me.brandonbarker.ftcscouter.match.Alliance;
import me.brandonbarker.ftcscouter.match.AllianceAdapter;
import me.brandonbarker.ftcscouter.match.AllianceColor;

public class ListAllianceFragment extends ListFragment {

    private OOTBApplication getApp() {
        return OOTBApplication.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new AllianceAdapter(getApp().getCurrentEvent(), getActivity()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        getApp().setCurrentMatch((Alliance) listView.getAdapter().getItem(position));
        getActivity().startActivity(new Intent(getActivity(), MatchActivity.class));
        getActivity().overridePendingTransition(R.anim.transition_in_right, R.anim.transition_out_left);
    }

    @Override
    public void onResume() {
        super.onResume();
        getApp().getCurrentActivity().getSupportBar().setDisplayHomeAsUpEnabled(true);
        getApp().getCurrentActivity().getSupportBar().setTitle(getApp().getCurrentEvent().getName());
        getApp().getCurrentActivity().getSupportBar().setWindowTitle("asdad");
    }

    public void create() {
        MaterialDialog.SingleButtonCallback callback = new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction action) {
                int team1 = dialogViewToInt(R.id.team1, dialog),
                        team2 = dialogViewToInt(R.id.team2, dialog),
                        match = dialogViewToInt(R.id.matchNumber, dialog);
                AllianceColor color = ((RadioButton) dialog.findViewById(R.id.matchRed)).isChecked() ? AllianceColor.RED :  AllianceColor.BLUE;
                getApp().getCurrentEvent().getAlliances().add(new Alliance(team1, team2, color, match));
                ((ArrayAdapter)getListAdapter()).notifyDataSetChanged();
            }

            private int dialogViewToInt(int id, MaterialDialog dialog) {
                return Integer.parseInt(((TextView) dialog.findViewById(id)).getText().toString());
            }
        };
        OOTBApplication.getDefaultDialog(getActivity(), "Create Match", callback)
                .positiveText("Create")
                .customView(R.layout.dialog_input_match, false)
                .build()
                .show();
    }
}
