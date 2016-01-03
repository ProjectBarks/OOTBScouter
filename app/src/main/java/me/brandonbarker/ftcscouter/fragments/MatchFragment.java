package me.brandonbarker.ftcscouter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.brandonbarker.ftcscouter.R;
import me.brandonbarker.ftcscouter.match.AllianceColor;
import me.brandonbarker.ftcscouter.match.ScoreGroup;
import me.brandonbarker.ftcscouter.match.ScoreType;

@Accessors(prefix = "m")
public class MatchFragment extends Fragment {

    public static MatchFragment create(ScoreGroup group) {
        MatchFragment fragment = new MatchFragment();
        fragment.setGroup(group);
        return fragment;
    }

    @Getter
    @Setter
    private ScoreGroup mGroup;

    public MatchFragment() {
        setGroup(new ScoreGroup(0, ScoreGroup.Starting.FLOOR, AllianceColor.BLUE, 0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        View layout = getView();
        assert layout != null;
        initPositionTracker(layout);
        initPointTracker();
        //initSpinnerTracker(layout);
    }

    private void initValueTracker(int viewId, final ScoreType type) {
        View layout =  getView();
        assert layout != null;
        if (layout.findViewById(viewId) instanceof CheckBox) {
            CheckBox checkbox = (CheckBox) layout.findViewById(viewId);
            checkbox.setChecked((Boolean) mGroup.getScore(type));
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGroup.addScore(type, ((CheckBox) v).isChecked());
                }
            });
            checkbox.callOnClick();
        } else if (layout.findViewById(viewId) instanceof DiscreteSeekBar) {
            DiscreteSeekBar seekBar = (DiscreteSeekBar) layout.findViewById(viewId);
            seekBar.setProgress((Integer) mGroup.getScore(type));
            seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
                @Override
                public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                    mGroup.addScore(type, seekBar.getProgress());
                }

                @Override
                public void onStartTrackingTouch(DiscreteSeekBar discreteSeekBar) {}

                @Override
                public void onStopTrackingTouch(DiscreteSeekBar discreteSeekBar) {}
            });
            seekBar.callOnClick();
        } else if (layout.findViewById(viewId) instanceof RadioButton) {
            RadioButton radio = (RadioButton) layout.findViewById(viewId);
            radio.setChecked((Boolean) mGroup.getScore(type));
            radio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mGroup.addScore(type, ((RadioButton) v).isChecked());
                }
            });
        } else {
            throw new ClassCastException("Unable to match view type!");
        }
    }

    /*private void initSpinnerTracker(View layout) {
        ////TODO 2/14/15 Fix
        final Spinner spinner = (Spinner) layout.findViewById(R.id.spinner);
        List<String> messages = new ArrayList<>();
        messages.add("Robot tipped over");
        messages.add("Robot died on Field");
        messages.add("Autonomous failed");
        messages.add("Robot lost control");
        messages.add("Drivers lacked in practice");
        messages.add("Robot lost pieces");
        messages.add("Penalty prone");
        messages.add("No autonomous program");
        messages.add("Custom");
        //Context context, int resource, List<T> objects
        spinner.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, messages));
        final NoteAdapter adapter = new NoteAdapter(getActivity(), 0, getGroup().getNotes());
        ((ListView) layout.findViewById(R.id.notes)).setAdapter(adapter);
        layout.findViewById(R.id.addNoteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.add((String)spinner.getSelectedItem());
                assert getView() != null;
                ((ScrollView)getView().findViewById(R.id.matchScroll)).fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }*/

    private void initPositionTracker(View layout) {
        boolean isOnFloor = mGroup.getStartingPosition() == ScoreGroup.Starting.FLOOR;
        ((RadioButton)layout.findViewById(R.id.mhaOnFloor)).setChecked(isOnFloor);
        ((RadioButton)layout.findViewById(R.id.mhaOnRamp)).setChecked(!isOnFloor);
        layout.findViewById(R.id.mhaOnFloor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGroup.setStartingPosition(((RadioButton)v).isChecked() ? ScoreGroup.Starting.FLOOR : ScoreGroup.Starting.RAMP);
            }
        });
        layout.findViewById(R.id.mhaOnRamp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGroup.setStartingPosition(((RadioButton)v).isChecked() ? ScoreGroup.Starting.RAMP : ScoreGroup.Starting.FLOOR);
            }
        });
    }

    private void initPointTracker() {
        //Autonomous Truth values
        initValueTracker(R.id.mhaGetsOffRamp, ScoreType.GET_OFF_RAMP);
        initValueTracker(R.id.mhaReleasesKickstand, ScoreType.KICKSTAND_RELEASED);
        initValueTracker(R.id.mhaCenterGoal, ScoreType.CENTER_GOAL);
        //Mha Goals in Parking
        initValueTracker(R.id.mhaGoalsInParking, ScoreType.GOALS_PARKING_ZONE_A);
        initValueTracker(R.id.mhaGoalsScored, ScoreType.ROLLING_GOALS);
        //Rolling Goals
        initValueTracker(R.id.mhtLowRollingGoal, ScoreType.LOW_GOAL);
        initValueTracker(R.id.mhtMedRollingGoal, ScoreType.MED_GOAL);
        initValueTracker(R.id.mhtHighRollingGoal, ScoreType.HIGH_GOAL);
        initValueTracker(R.id.mhtCentRollingGoal, ScoreType.CENT_GOAL);
        //Goals end position
        initValueTracker(R.id.mhtGoalsInParking, ScoreType.GOALS_PARKING_ZONE_E);
        initValueTracker(R.id.mhtGoalsOffFloor, ScoreType.GOALS_FLOOR);
        // Teleop Truth values
        initValueTracker(R.id.mhtParkingZone, ScoreType.ROBOT_PARKING);
        initValueTracker(R.id.mhtOffFloor, ScoreType.ROBOT_FLOOR);
    }
}