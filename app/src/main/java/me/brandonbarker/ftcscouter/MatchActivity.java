package me.brandonbarker.ftcscouter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.brandonbarker.ftcscouter.fragments.MatchFragment;
import me.brandonbarker.ftcscouter.match.Alliance;
import me.brandonbarker.ftcscouter.match.ScoreGroup;


@Accessors(prefix = {"m"})
public class MatchActivity extends BaseActivity {
    @Getter
    private Alliance mAlliance;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_match;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mAlliance = OOTBApplication.getInstance().getCurrentMatch();
        setTitle(String.format("Match #%s", mAlliance.getMatchNumber()));
        getSupportBar().setDisplayHomeAsUpEnabled(true);
        if (findViewById(R.id.matchActivity).getTag().equals("big_screen")) {
            MatchFragment.create(mAlliance.getEntryOne());
            applyFragment(MatchFragment.create(mAlliance.getEntryOne()), R.id.match1);
            applyFragment(MatchFragment.create(mAlliance.getEntryTwo()), R.id.match2);
            ((TextView)findViewById(R.id.match1Team)).setText(mAlliance.getEntryOne().getTeamNumber() + "");
            ((TextView)findViewById(R.id.match2Team)).setText(mAlliance.getEntryTwo().getTeamNumber() + "");
        } else {
            ViewPager pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(new OOTBFragmentPagerAdapter(getSupportFragmentManager()));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.transition_in_left, R.anim.transition_out_right);
    }

    private void applyFragment(Fragment fragment, int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public class OOTBFragmentPagerAdapter extends FragmentPagerAdapter {
        private static final int PAGE_COUNT = 2;

        public OOTBFragmentPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MatchFragment.create(mAlliance.getEntryOne());
                case 1:
                    return MatchFragment.create(mAlliance.getEntryTwo());
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            ScoreGroup entry;
            switch (position) {
                case 0:
                    entry = MatchActivity.this.mAlliance.getEntryOne();
                    break;
                case 1:
                    entry = MatchActivity.this.mAlliance.getEntryTwo();
                    break;
                default:
                    return null;
            }
            return "Team " + entry.getTeamNumber();
        }
    }
}