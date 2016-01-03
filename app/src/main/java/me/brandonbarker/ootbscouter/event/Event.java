package me.brandonbarker.ootbscouter.event;

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
import com.google.gson.annotations.SerializedName;

import me.brandonbarker.ootbscouter.R;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.brandonbarker.ootbscouter.OOTBApplication;
import me.brandonbarker.ootbscouter.match.Alliance;
import me.brandonbarker.ootbscouter.match.AllianceColor;

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