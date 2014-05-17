package rs.pokretaci.hakaton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;












import net.ascho.pokretaci.backend.beans.ServerResponseObject;
import net.ascho.pokretaci.backend.communication.Task;
import net.ascho.pokretaci.backend.communication.TaskFactory;
import net.ascho.pokretaci.backend.communication.TaskListener;
import net.ascho.pokretaci.backend.executors.login.GoogleLogin;
import net.ascho.pokretaci.beans.Activist;
import net.ascho.pokretaci.beans.Goal;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;
import rs.pokretaci.hakaton.R;

public class ProfileActivity extends FragmentActivity implements ActionBar.TabListener, TaskListener {
	private ViewPager mViewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private TextView mProfileStats;
	private String mUserId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//mViewPager = new ViewPager(this);
		//mViewPager.setId(R.id.pager);
		setContentView(R.layout.profile);
		mViewPager = (ViewPager) findViewById(R.id.pager);

		//final ActionBar bar = getActionBar();
		//bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		//mSectionsPagerAdapter = new TabsAdapter(this, mViewPager);
		mSectionsPagerAdapter = new SectionsPagerAdapter(this.getSupportFragmentManager());
		mViewPager.setAdapter(mSectionsPagerAdapter);

		//Bind the tabs to the ViewPager
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.setViewPager(mViewPager);
		//tabs.add
		/*tabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});*/

		//final ActionBar actionBar = getActionBar();
		/*mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				//actionBar.setSelectedNavigationItem(position);
			}
		});*/

		// For each of the sections in the app, add a tab to the action bar.
		/*for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
	// Create a tab with text corresponding to the page title defined by
	// the adapter. Also specify this Activity object, which implements
	// the TabListener interface, as the callback (listener) for when
	// this tab is selected.
	actionBar.addTab(actionBar.newTab()
			.setText(mSectionsPagerAdapter.getPageTitle(i))
			.setTabListener(this));
}*/

		/*mSectionsPagerAdapter.addTab(bar.newTab().setText("Simple"),
                CountingFragment.class, null);
        mSectionsPagerAdapter.addTab(bar.newTab().setText("List"),
                FragmentPagerSupport.ArrayListFragment.class, null);
        mSectionsPagerAdapter.addTab(bar.newTab().setText("Cursor"),
                CursorFragment.class, null);*/

		/*if (savedInstanceState != null) {
			bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
		}*/

		Task all =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.ALL_GOALS, Goal.GOAL_FILTER.TRENDING);
		all.executeTask(getApplicationContext(), this);
		
		Intent intent = getIntent();
		TextView text = (TextView) findViewById(R.id.profile_name);
		text.setText(intent.getStringExtra(MapActivity.FULL_NAME_EXTRA));
		mUserId = intent.getStringExtra(MapActivity.ID_EXTRA);
		mProfileStats = (TextView) findViewById(R.id.profile_stats);
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
		GoalsListFragment[] fragments = new GoalsListFragment[2];

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public GoalsListFragment getItem(int position) {
			if (fragments[position] == null) {
				fragments[position] = new GoalsListFragment();
			}
			return fragments[position];
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

	@Override
	public void onResponse(ServerResponseObject taskResponse) {

		if(taskResponse != null) { //Nije doslo do neocekivanog sranja
			if(taskResponse.isResponseValid()) { //Nije doslo do exception-a
				List list = taskResponse.getData();
				List<Goal> ownedGoals = new ArrayList<Goal>();
				List<Goal> supportedGoals = new ArrayList<Goal>();
				List<Goal> goals = (List<Goal>) list;
				for (Goal goal: goals) {
					if (goal.creator.id == mUserId) ownedGoals.add(goal);
					if (goal.supported) supportedGoals.add(goal);
				}
				mSectionsPagerAdapter.getItem(0).setGoals(ownedGoals);
				mSectionsPagerAdapter.getItem(1).setGoals(supportedGoals);
				mProfileStats.setText(String.format(getString(R.string.profile_stats), ownedGoals.size(), supportedGoals.size()));
			} else {
				//Doslo je do exception, mozes da dohvatis gresku sa
				taskResponse.getExceptionMsg(); // i dalje sta dizajner kaze :P

				//Ali ako je bio google login task poveravaj sledece
				Exception e = taskResponse.getException();


				Log.d("rs.pokretaci.hakaton", "Nepoznata excepcija " + e.toString());
			}
		} else {
			Log.d("rs.pokretaci.hakaton", "TaskResponse je null");
		}


	}
}