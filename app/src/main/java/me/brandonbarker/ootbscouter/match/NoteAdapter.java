package me.brandonbarker.ootbscouter.match;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.brandonbarker.ootbscouter.R;
import me.brandonbarker.ootbscouter.event.BaseAdapter;

/**
 * Created by brandon on 2/14/15.
 */
public class NoteAdapter extends BaseAdapter<String> {
    public NoteAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getViewSecondary(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
        }
        ((TextView)convertView.findViewById(R.id.noteText)).setText(getItem(position));
        onDeleteClick(getItem(position), convertView, R.id.noteDelete, "Deleted Note!");
        return convertView;
    }
}
