package me.brandonbarker.ftcscouter.match;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = {"m"})
public class Alliance {
    @Getter
    @SerializedName("alliance")
    private AllianceColor mColor;
    @Getter
    @SerializedName("team_2")
    private ScoreGroup mEntryOne;
    @Getter
    @SerializedName("team_1")
    private ScoreGroup mEntryTwo;
    @Getter
    @SerializedName("match")
    private Integer mMatchNumber;

    public Alliance(Integer teamOneNumber, Integer teamTwoNumber, AllianceColor color, Integer matchNumber) {
        this.mEntryOne = new ScoreGroup(teamOneNumber, ScoreGroup.Starting.FLOOR, color, matchNumber);
        this.mEntryTwo = new ScoreGroup(teamTwoNumber, ScoreGroup.Starting.RAMP, color, matchNumber);
        this.mColor = color;
        this.mMatchNumber = matchNumber;
    }

    public void setColor(AllianceColor color) {
        this.mColor = color;
        this.mEntryOne.setColor(color);
        this.mEntryTwo.setColor(color);
    }

    public void setMatchNumber(Integer matchNumber) {
        this.mMatchNumber = matchNumber;
        this.mEntryOne.setMatch(matchNumber);
        this.mEntryTwo.setMatch(matchNumber);
    }
}
