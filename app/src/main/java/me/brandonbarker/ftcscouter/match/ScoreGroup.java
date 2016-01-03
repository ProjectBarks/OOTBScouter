package me.brandonbarker.ftcscouter.match;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = {"m"})
public class ScoreGroup {

    @SerializedName("alliance")
    @Getter
    @Setter
    private AllianceColor mColor;

    @SerializedName("scores")
    private Map<ScoreType, Object> mEntry;

    @SerializedName("match")
    @Getter
    @Setter
    private Integer mMatch;

    @SerializedName("notes")
    @Getter
    private List<String> mNotes;

    @SerializedName("score")
    @Getter
    private double mScore;

    @SerializedName("starting_position")
    @Getter
    @Setter
    private Starting mStartingPosition;

    @SerializedName("team_number")
    @Getter
    @Setter
    private Integer mTeamNumber;

    public ScoreGroup(Integer teamNumber, Starting position, AllianceColor color, Integer matchNumber) {
        this.mTeamNumber = teamNumber;
        this.mColor = color;
        this.mStartingPosition = position;
        this.mMatch = matchNumber;
        this.mEntry = new HashMap<>();
        this.mNotes = new ArrayList<>();
        this.mScore = 0;
        for (ScoreType type : ScoreType.values()) {
            addScore(type, type.getFiller());
        }

    }

    public void addNote(String note) {
        this.mNotes.add(note);
    }

    public void addScore(ScoreType score, Object value) {
        if (!score.isValid(value)) {
            throw new IllegalArgumentException("Input does not satisfy requirements!");
        }
        this.mEntry.put(score, value);
        updateScore();
    }

    public Object getScore(ScoreType score) {
        if (!this.mEntry.containsKey(score)) {
            return null;
        }
        return this.mEntry.get(score);
    }

    private void updateScore() {
        mScore = 0;
        for (Entry<ScoreType, Object> entry : mEntry.entrySet()) {
            mScore += entry.getKey().getScore(entry.getValue());
        }
    }

    public enum Starting {
        FLOOR,
        RAMP
    }
}