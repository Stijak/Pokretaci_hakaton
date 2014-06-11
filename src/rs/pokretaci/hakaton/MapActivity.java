package rs.pokretaci.hakaton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.ascho.pokretaci.MainActivity;
import net.ascho.pokretaci.backend.beans.ServerResponseObject;
import net.ascho.pokretaci.backend.communication.ApacheClient;
import net.ascho.pokretaci.backend.communication.Task;
import net.ascho.pokretaci.backend.communication.TaskFactory;
import net.ascho.pokretaci.backend.communication.TaskListener;
import net.ascho.pokretaci.backend.cookies.CookieManager;
import net.ascho.pokretaci.backend.executors.login.GoogleLogin;
import net.ascho.pokretaci.backend.util.Util;
import net.ascho.pokretaci.beans.Activist;
import net.ascho.pokretaci.beans.Goal;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import rs.pokretaci.hakaton.R;
import rs.pokretaci.hakaton.customviews.ExpendableDrawerAdapter;
import rs.pokretaci.hakaton.customviews.PokretaciInfoWindowAdapter;

/**
 * Activity that contains an interactive Google Map fragment. Users can record
 * a traveled path, mark the map with information and take pictures that become
 * associated with the map. 
 */
public class MapActivity extends Activity implements GoogleMap.OnInfoWindowClickListener, TaskListener, ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {
	//drawer demo
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ExpandableListView mDrawerList2;
	private ActionBarDrawerToggle mDrawerToggle;
	//private TextView mWelcomeMessage;
	
	
	private ArrayList<String> mGroupItem = new ArrayList<String>();
	private ArrayList<Object> mChildItem = new ArrayList<Object>();

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mDrawwerList;
	private Activist mMyProfile;

	/** The interactive Google Map fragment. */
	private GoogleMap m_vwMap;
	private HashMap<Marker, Goal> mMarkerProblemIds = new HashMap<Marker, Goal>();


	private MapFragment mapFragment;




	/** Request codes for starting new Activities. */
	private static final int ENABLE_GPS_REQUEST_CODE = 1;
	private static final int PICTURE_REQUEST_CODE = 2;
	
	public final static String ID_EXTRA = "ID_EXTRA";
	public final static String FULL_NAME_EXTRA = "FULL_NAME_EXTRA";
	public final static String AVATAR_EXTRA = "AVATAR_EXTRA";

	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ApacheClient.getInstance().setCookieStore(
				CookieManager.getInstance().restoreCookiesFromSharedPreferences(
						getApplicationContext()
						));
		setContentView(R.layout.map_layout);
		//mWelcomeMessage = (TextView) findViewById(R.id.welcome_message);
		//mWelcomeMessage.setClickable(true);
		initLayout();
		initDrawer2();
		String email = Util.getAccountNames(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE, getApplicationContext())[0];
		Task googleLogin = new GoogleLogin(email, MapActivity.this);
		googleLogin.executeTask(getApplicationContext(), this);
		//new DownloadLocationsTask().execute();
		Task all =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.ALL_GOALS, Goal.GOAL_FILTER.TRENDING);
		all.executeTask(getApplicationContext(), this);

	}
	
	public boolean dispatchTouchEvent(MotionEvent event) {
		TextView mWelcomeMessage = (TextView) findViewById(R.id.welcome_message);
		mWelcomeMessage.setVisibility(View.GONE);
		mWelcomeMessage.invalidate();
		return super.dispatchTouchEvent(event);
		}
	
	public Goal getRelevantGoal(Marker marker) {
		return mMarkerProblemIds.get(marker);
	}

	public void solveProblem(View v) {
		Intent i = new Intent(this, SubmitProblem.class);
		startActivity(i); 
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (m_vwMap == null) {
			m_vwMap = mapFragment.getMap();
			m_vwMap.setMyLocationEnabled(true);
			m_vwMap.setOnInfoWindowClickListener(this);
			PokretaciInfoWindowAdapter adapter = new PokretaciInfoWindowAdapter(this);
			//m_vwMap.setInfoWindowAdapter(adapter);
		}

	}
	
	public void setGroupData() {
		mGroupItem.addAll(Arrays.asList(this.getResources().getStringArray(R.array.drawer_array)));
	}

	public void setChildGroupData() {
		/**
		 * Add Data For TecthNology
		 */
		ArrayList<String> child = new ArrayList<String>();

		mChildItem.add(child);

		/**
		 * Add Data For Mobile
		 */
		child = new ArrayList<String>();
		child.addAll(Arrays.asList(this.getResources().getStringArray(R.array.drawer_array_child_category)));
		mChildItem.add(child);
		/**
		 * Add Data For Manufacture
		 */
		child = new ArrayList<String>();
		mChildItem.add(child);
		/**
		 * Add Data For Extras
		 */
		child = new ArrayList<String>();
		mChildItem.add(child);
	}


	private void initDrawer() {
		mTitle = mDrawerTitle = getTitle();
		mDrawwerList = getResources().getStringArray(R.array.drawer_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mDrawwerList));
		//mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description for accessibility */
				R.string.drawer_close  /* "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		//mDrawerList.setOnItemClickListener(listener); //TODO
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		// enable ActionBar app icon to behave as action to toggle nav drawer
				getActionBar().setDisplayHomeAsUpEnabled(true);
				getActionBar().setHomeButtonEnabled(true);
	}
	
	private void initDrawer2() {
		setGroupData();
		setChildGroupData();
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList2 = (ExpandableListView) findViewById(R.id.left_drawer);

		mDrawerList2.setAdapter(new ExpendableDrawerAdapter(this, mGroupItem, mChildItem));

		mDrawerList2.setOnChildClickListener(this);
		mDrawerList2.setOnGroupClickListener(this);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
		 R.drawable.ic_drawer, R.string.drawer_open,
		 R.string.drawer_close) {
		 public void onDrawerClosed(View view) {
			 super.onDrawerClosed(view);
		 //getActionBar().setSubtitle("open");
		 }
		 @Override
		    public void onDrawerSlide(View drawerView, float slideOffset)
		    {
		        super.onDrawerSlide(drawerView, slideOffset);
		        mDrawerLayout.bringChildToFront(drawerView);
		        mDrawerLayout.requestLayout();
		    }
		
		 /** Called when a drawer has settled in a completely open state. */
		 public void onDrawerOpened(View drawerView) {
			 super.onDrawerOpened(drawerView);
		 //getActionBar().setSubtitle("close");
		 }
		
		 };
		
		 mDrawerLayout.setDrawerListener(mDrawerToggle);
		 getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
	}


	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu (android.view.Menu menu) {
		// If the nav drawer is open, hide action items related to the content view
		//boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu((android.view.Menu) menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		/*switch(item.getItemId()) {
        case R.id.action_websearch:
            // create intent to perform web search for this planet
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            // catch event that there's no activity to handle intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }*/
		return super.onOptionsItemSelected(item);
	}


	/**
	 * Initializes all other data for the application.
	 */
	private void initLayout() {
		setContentView(R.layout.map_layout);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		GoogleMapOptions mapOptions = new GoogleMapOptions();
		mapOptions.compassEnabled(true);
		CameraPosition.Builder cameraBuilder = CameraPosition.builder();
		LatLng belgrade = new LatLng(44.769010, 20.479202);
		cameraBuilder.target(belgrade);
		cameraBuilder.zoom(12);
		mapOptions.camera(cameraBuilder.build());
		mapFragment = MapFragment.newInstance(mapOptions);
		fragmentTransaction.add(R.id.content_frame, mapFragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		Intent intent = new Intent();
		intent.setClass(this, ProblemDetailsActivity.class);
		intent.putExtra(ID_EXTRA, mMarkerProblemIds.get(marker).id);
		//intent.putExtra("EXTRA_ID", "SOME DATAS");
		startActivity(intent);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		CookieManager.getInstance().saveCookiesToSharedPreferences(getApplicationContext(), ApacheClient.getInstance().getCookieStore());
	}

	@Override
	public void onResponse(ServerResponseObject taskResponse) {

		if(taskResponse != null) { //Nije doslo do neocekivanog sranja
			if(taskResponse.isResponseValid()) { //Nije doslo do exception-a
				List list = taskResponse.getData();
				if (list.size() == 0) return;
				if (list.get(0) instanceof Activist) { //login task
					Log.d("rs.pokretaci.hakaton", "Uspješno ulogovani");
					mMyProfile = (Activist) list.get(0);
				} else {
					List<Goal> goals = (List<Goal>) list;
					for (Goal goal: goals) {
						if (goal == null) break;
						String id = goal.id;
						String title = goal.title;
						if (goal.creator == null) break;
						String authorName = goal.creator.full_name;
						String description = goal.description;
						double longitude = goal.lon;
						double latitude = goal.lat;
						LatLng location = new LatLng(latitude, longitude);
						int icon = R.drawable.new_goal_pin;
						if (goal.mapPinType()==Goal.GOAL_STATES.SUPPORTED_GOAL) icon = R.drawable.supported_goal_pin;
						else if (goal.mapPinType() == Goal.GOAL_STATES.USER_GOAL) icon = R.drawable.user_pin;
						else if (goal.mapPinType() ==  Goal.GOAL_STATES.RESOLVED_GOAL) icon = R.drawable.resolved_goal_pin;
						MarkerOptions mo = new MarkerOptions().title(title).snippet(authorName).position(location).icon(BitmapDescriptorFactory.fromResource(icon));//u zavisnosti od vrste pina
						Marker marker = m_vwMap.addMarker(mo);
						mMarkerProblemIds.put(marker, goal);
					}
				}
			} else {
				//Doslo je do exception, mozes da dohvatis gresku sa
				taskResponse.getExceptionMsg(); // i dalje sta dizajner kaze :P

				//Ali ako je bio google login task poveravaj sledece
				Exception e = taskResponse.getException();
				if(e instanceof GooglePlayServicesAvailabilityException) {

					GooglePlayServicesAvailabilityException gEx = (GooglePlayServicesAvailabilityException) e;

					Dialog alert = GooglePlayServicesUtil.getErrorDialog(gEx.getConnectionStatusCode(), this, GoogleLogin.GOOGLE_AUTH_REQUEST_CODE);
					if(alert != null) { //Od ovog exceptiona mozemo da se oporavimo
						alert.show();
					} else { //Ovde nzn obavestimo ga da ne moze samo da gleda ciljeve a da se ne loguje
						//neki dialog ili toast ("Vas uredjaj ne poseduje GooglePlayServices i nazalost vas ne mozemo ulogovati kroz aplikaciju. I dalje mozete koristiti aplikaciju ali ne mozete postavljati nove probleme...");
					}
				}  else if(e instanceof UserRecoverableAuthException) {
					UserRecoverableAuthException ue = (UserRecoverableAuthException) e;
					//Od ovog exceptiona mozemo da se oporavimo
					startActivityForResult(ue.getIntent(), GoogleLogin.GOOGLE_AUTH_REQUEST_CODE);
				}

				Log.d("rs.pokretaci.hakaton", "Nepoznata excepcija " + e.toString());
			}
		} else {
			Log.d("rs.pokretaci.hakaton", "TaskResponse je null");
		}


	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
		switch (groupPosition) {
		case 0:TextView mWelcomeMessage = (TextView) findViewById(R.id.welcome_message);  
		mWelcomeMessage.setVisibility(View.VISIBLE);
		mWelcomeMessage.invalidate();
		break;
		case 1: return false;
		case 2: getMyProfile();
		break;
		case 3:  ;
		break;
		default: 
		}
		mDrawerLayout.closeDrawers();
		return true;
	}
	
	private void getMyProfile() {
		Intent intent = new Intent();
		intent.setClass(this, ProfileActivity.class);
		intent.putExtra(ID_EXTRA, Activist.getUserProfile().id);
		intent.putExtra(FULL_NAME_EXTRA, Activist.getUserProfile().full_name);
		intent.putExtra(AVATAR_EXTRA, Activist.getUserProfile().avatar);
		//intent.putExtra("EXTRA_ID", "SOME DATAS");
		startActivity(intent);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		m_vwMap.clear();
		if (childPosition == 0) {
			Task all =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.ALL_GOALS, Goal.GOAL_FILTER.TRENDING);
			all.executeTask(getApplicationContext(), this);
		} else if (childPosition == 1) {
			Task trending =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.BY_FILTER, Goal.GOAL_FILTER.TRENDING);
			trending.executeTask(getApplicationContext(), this);
		} else if (childPosition == 2) {
			Task trending =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.BY_FILTER, Goal.GOAL_FILTER.NEWEST);
			trending.executeTask(getApplicationContext(), this);
		} else if (childPosition ==3) {
			Task trending =  TaskFactory.goalFetchTask(Goal.GOAL_FETCH_TYPE.BY_FILTER, Goal.GOAL_FILTER.MOST_DISCUSSED);
			trending.executeTask(getApplicationContext(), this);
		}
		mDrawerLayout.closeDrawers();
		return true;
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}
	 */
}