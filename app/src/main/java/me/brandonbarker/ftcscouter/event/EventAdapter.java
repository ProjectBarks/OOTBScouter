package me.brandonbarker.ftcscouter.event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gc.materialdesign.widgets.SnackBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.write.Number;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import me.brandonbarker.ftcscouter.OOTBApplication;
import me.brandonbarker.ftcscouter.R;
import me.brandonbarker.ftcscouter.match.Alliance;
import me.brandonbarker.ftcscouter.match.ScoreGroup;
import me.brandonbarker.ftcscouter.match.ScoreType;

public class EventAdapter extends BaseAdapter<Event> {

    public EventAdapter(List<Event> events, Context context) {
        super(context, 0, events);
    }

    @Override
    public View getViewSecondary(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }
        setText(convertView, R.id.eventTitle, event.getName());
        setText(convertView, R.id.eventSubText, event.getLocation());
        updateIcon(convertView.findViewById(R.id.eventDeleteImage));
        updateIcon(convertView.findViewById(R.id.eventRenameImage));
        updateIcon(convertView.findViewById(R.id.eventExportImage));
        onDeleteClick(event, convertView, R.id.eventDelete, "Event removed from system!");
        onRenameClick(event, convertView);
        onExport(event, convertView);
        return convertView;
    }

    protected void onRenameClick(final Event event, View parentView) {
        parentView.findViewById(R.id.eventRename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog.SingleButtonCallback callback = new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction action) {
                        event.setName(getText(dialog, R.id.eventName));
                        event.setLocation(getText(dialog, R.id.eventLocation));
                        EventAdapter.this.notifyDataSetChanged();
                    }

                    private String getText(MaterialDialog dialog, int id) {
                        //noinspection ConstantConditions
                        return ((TextView) dialog.getCustomView().findViewById(id)).getText().toString();
                    }
                };
                OOTBApplication.getDefaultDialog(OOTBApplication.getInstance().getCurrentActivity(), "Rename this event?", callback)
                        .title("Rename Event")
                        .positiveText("Rename")
                        .customView(R.layout.dialog_input_event, false)
                        .build()
                        .show();
            }
        });
    }

    protected void onExport(final Event event, View convertView) {
        convertView.findViewById(R.id.eventExport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ScoreGroup> scoreGroupList = new ArrayList<>();
                for (Alliance alliance : event.getAlliances()) {
                    scoreGroupList.add(alliance.getEntryOne());
                    scoreGroupList.add(alliance.getEntryTwo());
                }

                try {
                    File file = OOTBApplication.getInstance().getStorage().getFile(OOTBApplication.DIRECTORY,
                            "export_" + event.getName().toLowerCase().replaceAll(" ", "") +".xls");
                    WritableWorkbook workbook = Workbook.createWorkbook(file);
                    WritableSheet sheet = workbook.createSheet("data", 0);
                    int x = 0, y = 0;
                    sheet.addCell(new Label(x++, y, "Match"));
                    sheet.addCell(new Label(x++, y, "Color"));
                    sheet.addCell(new Label(x++, y, "Team"));
                    sheet.addCell(new Label(x++, y, "Starting"));
                    for (ScoreType type : ScoreType.values()) {
                        Label label = new Label(x++, y, type.getName());
                        sheet.addCell(label);
                    }
                    sheet.addCell(new Label(x++, y, ScoreType.Phase.AUTONOMOUS.getName()));
                    sheet.addCell(new Label(x++, y, ScoreType.Phase.TELEOP.getName()));
                    sheet.addCell(new Label(x++, y, ScoreType.Phase.ENDGAME.getName()));
                    sheet.addCell(new Label(x, y, "Totals"));
                    y++;
                    for (ScoreGroup group : scoreGroupList) {
                        x = 0;
                        sheet.addCell(new Number(x++, y, group.getMatch()));
                        sheet.addCell(new Label(x++, y, group.getColor().toString()));
                        sheet.addCell(new Number(x++, y, group.getTeamNumber()));
                        sheet.addCell(new Label(x++, y, group.getStartingPosition().name()));
                        for (ScoreType type : ScoreType.values()) {
                            Object value = group.getScore(type);
                            WritableCell cell = null;
                            if (value instanceof Integer) {
                                cell = new Number(x++, y, (Integer) value);
                            } else if (value instanceof Boolean) {
                                cell = new jxl.write.Boolean(x++, y, ((Boolean) value));
                            }
                            sheet.addCell(cell);
                        }
                        double autonomous = 0, teleop = 0, endgame = 0;
                        for (ScoreType type : ScoreType.values()) {
                            switch(type.getPhase()) {
                                case TELEOP:
                                    autonomous += type.getScore(group.getScore(type));
                                    break;
                                case AUTONOMOUS:
                                    autonomous += type.getScore(group.getScore(type));
                                    break;
                                case ENDGAME:
                                    endgame += type.getScore(group.getScore(type));
                                    break;
                            }
                        }
                        sheet.addCell(new Number(x++, y, autonomous));
                        sheet.addCell(new Number(x++, y, teleop));
                        sheet.addCell(new Number(x++, y, endgame));
                        sheet.addCell(new Number(x, y, autonomous + teleop + endgame));
                        y++;
                    }
                    workbook.write();
                    workbook.close();
                    new SnackBar(OOTBApplication.getInstance().getCurrentActivity(), "Exported " + event.getName() + "!", null, null).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    new SnackBar(OOTBApplication.getInstance().getCurrentActivity(), "Failed export!", null, null).show();
                }
            }
        });
    }
}