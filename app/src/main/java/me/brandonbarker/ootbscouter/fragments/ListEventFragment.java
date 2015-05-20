package me.brandonbarker.ootbscouter.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.logging.Level;
import java.util.logging.Logger;

import me.brandonbarker.ootbscouter.OOTBApplication;
import me.brandonbarker.ootbscouter.R;
import me.brandonbarker.ootbscouter.event.Event;
import me.brandonbarker.ootbscouter.event.EventAdapter;

public class ListEventFragment extends ListFragment {
    private OOTBApplication getApp() {
        return OOTBApplication.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new EventAdapter(getApp().getEvents(), getActivity()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        getApp().setCurrentEvent(getApp().getEvents().get(position));
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragment_container, new ListAllianceFragment())
                .addToBackStack("match_manager")
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        getApp().getCurrentActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getApp().getCurrentActivity().getSupportActionBar().setTitle("OOTB Scouter");
    }

    public void create() {
        MaterialDialog.SimpleCallback callback = new MaterialDialog.SimpleCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                MaterialEditText eventName = (MaterialEditText) dialog.findViewById(R.id.eventName);
                MaterialEditText eventLocation = (MaterialEditText) dialog.findViewById(R.id.eventLocation);
                Event event = new Event(eventName.getText().toString(), eventLocation.getText().toString());
                getApp().getEvents().add(event);
                ((ArrayAdapter)getListAdapter()).notifyDataSetChanged();
            }
        };
        OOTBApplication.getDefaultDialog(getActivity(), "Create Event", callback)
                .positiveText("Create")
                .customView(R.layout.dialog_input_event)
                .build()
                .show();
    }
}
