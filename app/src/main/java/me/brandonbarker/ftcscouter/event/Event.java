package me.brandonbarker.ftcscouter.event;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.brandonbarker.ftcscouter.match.Alliance;

@Accessors(prefix = {"m"})
public class Event  {

    @SerializedName("event_location")
    @Getter
    @Setter
    private String mLocation;

    @SerializedName("event_name")
    @Getter
    @Setter
    private String mName;

    @SerializedName("alliances")
    @Getter
    @Setter
    private List<Alliance> mAlliances;

    public Event(String name, String location) {
        this.mName = name;
        this.mLocation = location;
        this.mAlliances = new ArrayList<>();
    }

}