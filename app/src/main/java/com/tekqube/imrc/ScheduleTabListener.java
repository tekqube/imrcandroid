package com.tekqube.imrc;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
 
public class ScheduleTabListener<T extends Fragment> implements ActionBar.TabListener {
	
	private final Activity mActivity;
    private final String mTag;
    private final Class<T> mClass;
    private final Bundle mArgs;
    private Fragment mFragment;

    public ScheduleTabListener(Activity activity, String tag, Class<T> clz) {
        this(activity, tag, clz, null);
    }

    public ScheduleTabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
        mActivity = activity;
        mTag = tag;
        mClass = clz;
        mArgs = args;

        // Check to see if we already have a fragment for this tab, probably
        // from a previously saved state.  If so, deactivate it, because our
        // initial state is that a tab isn't shown.
        mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
        if (mFragment != null && !mFragment.isDetached()) {
            FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
            ft.detach(mFragment);
            ft.commit();
        }
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
    	System.out.println(" On selected: "+tab.getPosition());

        if (mFragment == null) {
            mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
            ft.add(R.id.fragment_container, mFragment, mTag);
        } else {
            ft.attach(mFragment);
        }
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    	System.out.println(" On UNselected: "+tab.getPosition());
        if (mFragment != null) {
            ft.detach(mFragment);
        }
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {

    }
}