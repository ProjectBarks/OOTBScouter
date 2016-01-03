package me.brandonbarker.ftcscouter;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;

import me.brandonbarker.ftcscouter.fragments.ListAllianceFragment;
import me.brandonbarker.ftcscouter.fragments.ListEventFragment;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        findViewById(R.id.buttonFloat).setBackgroundColor(ContextCompat.getColor(this, R.color.accent));
        findViewById(R.id.buttonFloat).setOnClickListener(this);
        getSupportBar().setElevation(5.0F);
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .add(R.id.fragment_container, new ListEventFragment())
                .addToBackStack("event_manager")
                .commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof ListEventFragment) {
            ((ListEventFragment)fragment).create();
        } else if (fragment instanceof ListAllianceFragment) {
            ((ListAllianceFragment)fragment).create();
        }
    }
}