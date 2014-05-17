package rs.pokretaci.hakaton;

import java.util.ArrayList;
import java.util.Locale;





import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import rs.pokretaci.hakaton.R;

public class ProfileActivity extends FragmentActivity implements ActionBar.TabListener {
	private ViewPager mViewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//mViewPager = new ViewPager(this);
		//mViewPager.setId(R.id.pager);
		setContentView(R.layout.profile);
		mViewPager = (ViewPager) findViewById(R.id.pager);

		final ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayShowHomeEnabled(false);
		//bar.setDisplayOptions(0, ActionBar);

		//mSectionsPagerAdapter = new TabsAdapter(this, mViewPager);
		mSectionsPagerAdapter = new SectionsPagerAdapter(this.getSupportFragmentManager());
		mViewPager.setAdapter(mSectionsPagerAdapter);
		final ActionBar actionBar = getActionBar();
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

// For each of the sections in the app, add a tab to the action bar.
for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
	// Create a tab with text corresponding to the page title defined by
	// the adapter. Also specify this Activity object, which implements
	// the TabListener interface, as the callback (listener) for when
	// this tab is selected.
	actionBar.addTab(actionBar.newTab()
			.setText(mSectionsPagerAdapter.getPageTitle(i))
			.setTabListener(this));
}

		/*mSectionsPagerAdapter.addTab(bar.newTab().setText("Simple"),
                CountingFragment.class, null);
        mSectionsPagerAdapter.addTab(bar.newTab().setText("List"),
                FragmentPagerSupport.ArrayListFragment.class, null);
        mSectionsPagerAdapter.addTab(bar.newTab().setText("Cursor"),
                CursorFragment.class, null);*/

		if (savedInstanceState != null) {
			bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost.  It relies on a
	 * trick.  Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show.  This is not sufficient for switching
	 * between pages.  So instead we make the content part of the tab host
	 * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
	 * view to show as the tab content.  It listens to changes in tabs, and takes
	 * care of switch to the correct paged in the ViewPager whenever the selected
	 * tab changes.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return new CommentsFragment();
			} else {
				return new CommentsFragment(); //TODO replace with new fragment
			}
			//TODO return correspondingfragment
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.submited_problems);
			case 1:
				return getString(R.string.supported_problems);
			}
			return null;
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}