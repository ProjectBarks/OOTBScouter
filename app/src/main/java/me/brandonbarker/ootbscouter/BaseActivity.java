package me.brandonbarker.ootbscouter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = {"m"})
public abstract class BaseActivity extends ActionBarActivity {
    @Getter
    protected OOTBApplication mApp;
    @Getter
    private Toolbar mToolbar;

    private void clearReferences() {
        BaseActivity activity = this.mApp.getCurrentActivity();
        if (activity != null && activity == this) {
            this.mApp.setCurrentActivity(null);
        }
    }

    protected abstract int getLayoutResource();

    @Override
    public void onBackPressed() {
        if (!getFragmentManager().popBackStackImmediate()) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(getLayoutResource());
        this.mApp = ((OOTBApplication) getApplicationContext());
        this.mToolbar = ((Toolbar) findViewById(R.id.toolbar));
        if (this.mToolbar != null) {
            setSupportActionBar(this.mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        this.mApp.save();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        clearReferences();
        this.mApp.save();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mApp.setCurrentActivity(this);
    }

    protected void setActionBarIcon(int id) {
        this.mToolbar.setNavigationIcon(id);
    }
}
