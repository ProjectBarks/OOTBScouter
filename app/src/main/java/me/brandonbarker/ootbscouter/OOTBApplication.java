package me.brandonbarker.ootbscouter;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.brandonbarker.ootbscouter.event.Event;
import me.brandonbarker.ootbscouter.match.Alliance;
import me.brandonbarker.ootbscouter.match.ScoreType;

@Accessors(prefix = {"m"})
public class OOTBApplication extends Application {

    public static final String DIRECTORY = "ootbscouter", FILE = "data.json";

    @Getter
    private static OOTBApplication mInstance;
    @Getter
    @Setter
    private BaseActivity mCurrentActivity;
    @Getter
    @Setter
    private Event mCurrentEvent;
    @Getter
    @Setter
    private Alliance mCurrentMatch;
    @Getter
    private List<Event> mEvents;

    public static MaterialDialog.Builder getDefaultDialog(Context context, String title, final MaterialDialog.SimpleCallback callback) {
        return new MaterialDialog.Builder(context)
                .title(title)
                .titleColorRes(R.color.primary_text)
                .autoDismiss(false)
                .positiveColorRes(R.color.accent)
                .negativeText("Cancel")
                .negativeColorRes(R.color.primary_text)
                .callback(new MaterialDialog.Callback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        List<View> allChildrenBFS = getAllChildrenBFS(dialog.getCustomView());
                        List<EditText> textViews = new ArrayList<EditText>();
                        for (View view : allChildrenBFS) {
                            if (!(view instanceof EditText)) {
                                continue;
                            }
                            textViews.add((EditText) view);
                        }
                        if (isEditTextEmpty(textViews.toArray(new EditText[textViews.size()]))) {
                            return;
                        }
                        if (callback != null) {
                            callback.onPositive(dialog);
                        }
                        dialog.dismiss();
                    }


                    private boolean isEditTextEmpty(EditText... editTexts) {
                        boolean empty = false;
                        for (EditText editText : editTexts) {
                            if (editText.length() > 0) {
                                continue;
                            }
                            empty = true;
                            editText.setError("Empty Field!");
                        }
                        return empty;
                    }

                    private List<View> getAllChildrenBFS(View v) {
                        List<View> visited = new ArrayList<View>();
                        List<View> unvisited = new ArrayList<View>();
                        unvisited.add(v);

                        while (!unvisited.isEmpty()) {
                            View child = unvisited.remove(0);
                            visited.add(child);
                            if (!(child instanceof ViewGroup)) continue;
                            ViewGroup group = (ViewGroup) child;
                            final int childCount = group.getChildCount();
                            for (int i=0; i<childCount; i++) unvisited.add(group.getChildAt(i));
                        }

                        return visited;
                    }
                });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        this.mEvents = new ArrayList<>();
        this.mCurrentActivity = null;
        load();
    }

    public void save() {
        super.onTerminate();
        String json = new Gson().toJson(this.mEvents);
        Storage storage = getStorage();
        if (storage.isFileExist(DIRECTORY, FILE)) {
            storage.deleteFile(DIRECTORY, FILE);
        } else if (storage.isDirectoryExists(DIRECTORY)) {
            storage.createDirectory(DIRECTORY);
        }
        storage.createFile(DIRECTORY, FILE, json);
    }

    public void load() {
        Storage storage = getStorage();
        if (storage.isFileExist(DIRECTORY, FILE)) {
            try {
                Type type = new TypeToken<List<Event>>(){}.getType();
                this.mEvents = new Gson().fromJson(storage.readTextFile(DIRECTORY, FILE), type);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.mEvents != null) {
                for (Event events : mEvents) {
                    for (Alliance alliance : events.getAlliances()) {
                        for (ScoreType type : ScoreType.values()) {
                            if (!type.getInput().getType().isAssignableFrom(Integer.class)) {
                                continue;
                            }
                            alliance.getEntryTwo().addScore(type, ((Double) alliance.getEntryTwo().getScore(type)).intValue());
                            alliance.getEntryOne().addScore(type, ((Double) alliance.getEntryOne().getScore(type)).intValue());
                        }
                    }
                }
            } else {
                this.mEvents = new ArrayList<>();
            }
        }
    }

    public Storage getStorage() {
        if (SimpleStorage.isExternalStorageWritable()) {
            return SimpleStorage.getExternalStorage();
        }
        return SimpleStorage.getInternalStorage(getApplicationContext());
    }
}