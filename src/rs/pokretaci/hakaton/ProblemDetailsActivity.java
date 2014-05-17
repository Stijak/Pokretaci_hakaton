package rs.pokretaci.hakaton;

import java.util.ArrayList;
import java.util.Locale;












import net.ascho.pokretaci.backend.beans.ServerResponseObject;
import net.ascho.pokretaci.backend.communication.Task;
import net.ascho.pokretaci.backend.communication.TaskFactory;
import net.ascho.pokretaci.backend.communication.TaskListener;
import net.ascho.pokretaci.beans.Goal;

import com.astuetz.PagerSlidingTabStrip;

import android.app.ActionBar;
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
import rs.pokretaci.hakaton.R;

public class ProblemDetailsActivity extends FragmentActivity implements ActionBar.TabListener, TaskListener {
	private ViewPager mViewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent callingIntent = this.getIntent();
		
		if (callingIntent != null) {
			String goalId = callingIntent.getStringExtra(MapActivity.GOAL_ID_EXTRA);
			if (goalId != null) {
				Task newestGoals =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.BY_GOAL_ID, goalId);
				newestGoals.executeTask(getApplicationContext(), this);
			}
		}

		//mViewPager = new ViewPager(this);
		//mViewPager.setId(R.id.pager);
		setContentView(R.layout.problem_details);
		mViewPager = (ViewPager) findViewById(R.id.pager);


		//mSectionsPagerAdapter = new TabsAdapter(this, mViewPager);
		mSectionsPagerAdapter = new SectionsPagerAdapter(this.getSupportFragmentManager());
		mViewPager.setAdapter(mSectionsPagerAdapter);

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		tabs.setViewPager(mViewPager);

		//final ActionBar actionBar = getActionBar();
		/*	mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});*/



		/*mSectionsPagerAdapter.addTab(bar.newTab().setText("Simple"),
                CountingFragment.class, null);
        mSectionsPagerAdapter.addTab(bar.newTab().setText("List"),
                FragmentPagerSupport.ArrayListFragment.class, null);
        mSectionsPagerAdapter.addTab(bar.newTab().setText("Cursor"),
                CursorFragment.class, null);*/


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
		private Fragment[] fragments = new Fragment[2];

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) { //nevidjeno zakukuljeno - ali ovo pisem poslije 24 sata nespavanja
			if (fragments[position] != null) {
				return fragments[position];
			}
			if (position == 0) {

				fragments[0] = new DetailsFragment();
				return fragments[0];
			} else {
				fragments[1] = new CommentsFragment();			
				return fragments[1]; 
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
				return getString(R.string.page_details);
			case 1:
				return getString(R.string.page_comments);
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
		if (taskResponse != null && taskResponse.isResponseValid()) {
			Goal goal = (Goal) taskResponse.getData().get(0);
			DetailsFragment df = (DetailsFragment) mSectionsPagerAdapter.getItem(0);
			df.setContent(goal);
			CommentsFragment cf = (CommentsFragment) mSectionsPagerAdapter.getItem(1);
			cf.setComments(goal.getComments());
			
		}


	}
}