package edu.calpoly.android.walkabout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

/**
 * Activity that contains an interactive Google Map fragment. Users can record
 * a traveled path, mark the map with information and take pictures that become
 * associated with the map. 
 */
public class WalkAbout extends Activity {
	//drawer demo
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;

	/** The interactive Google Map fragment. */
	private GoogleMap m_vwMap;

	/** The list of locations, each having a latitude and longitude. */
	private ArrayList<LatLng> m_arrPathPoints;

	/** The list of markers, each having a latitude, longitude and title. */
	private ArrayList<Marker> m_arrPicturePoints;

	/** The continuous set of lines drawn between points on the map. */
	private Polyline m_pathLine;

	/** The Location Manager for the map. Used to obtain location status, etc. */
	private LocationManager m_locManager;

	/** Whether or not recording is currently in progress. */
	private boolean m_bRecording;

	private MapFragment mapFragment;

	/** The radius of a Circle drawn on the map, in meters. */
	private static final int CIRCLE_RADIUS = 30;

	/** Constants for the LocationManager. */
	private static final int MIN_TIME_CHANGE = 3000;
	private static final int MIN_DISTANCE_CHANGE = 3;

	/** Request codes for starting new Activities. */
	private static final int ENABLE_GPS_REQUEST_CODE = 1;
	private static final int PICTURE_REQUEST_CODE = 2;

	@Override
	protected void onStart() {
		super.onStart();
		if (m_vwMap == null) {
			m_vwMap = mapFragment.getMap();
			m_vwMap.setMyLocationEnabled(true);
		}

	}

	private class DownloadLocationsTask extends AsyncTask<Void, Void, JSONObject> {
		@Override
		protected JSONObject doInBackground(Void... voids) {
			StringBuilder builder = new StringBuilder();
		    HttpClient client = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet("http://www.pokretaci.rs/api/goals/filter/trending?limit=50");
		    try {
		      HttpResponse response = client.execute(httpGet);
		      StatusLine statusLine = response.getStatusLine();
		      int statusCode = statusLine.getStatusCode();
		      if (statusCode == 200) {
		        HttpEntity entity = response.getEntity();
		        InputStream content = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		        String line;
		        while ((line = reader.readLine()) != null) {
		          builder.append(line);
		        }
		      } else {
		        Log.e(WalkAbout.class.toString(), "Failed to download file");
		      }
		    } catch (ClientProtocolException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		    try {
				return new JSONObject(builder.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(JSONObject object) {
			JSONObject data;
			try {
				data = object.getJSONObject("data");
	    	//String[] names = JSONObject.getNames(data);
			JSONArray array = data.names();
	    	for (int i = 0; i < array.length(); i++) {
	    		String name = array.getString(i);
	    		JSONObject problem = data.getJSONObject(name);
	    		String title = problem.getString("title");
	    		String description = problem.getString("description");
	    		JSONObject geo = problem.getJSONObject("location").getJSONObject("geo");
	    		double lat = geo.getDouble("lon");
	    		double lon = geo.getDouble("lat");
	    		LatLng location = new LatLng(lat, lon);
	    		m_vwMap.addMarker(new MarkerOptions()
                .title(title)
                .snippet(description)
                .position(location));
	    	}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLocationData();
		initLayout();
		initDrawer();
		
		new DownloadLocationsTask().execute();
	}

	/**
	 * Initializes all Location-related data.
	 */
	private void initLocationData() {
		// TODO
	}
	
	private void initDrawer() {
		 mTitle = mDrawerTitle = getTitle();
	        mPlanetTitles = getResources().getStringArray(R.array.drawer_array);
	        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	        mDrawerList = (ListView) findViewById(R.id.left_drawer);

	        // set a custom shadow that overlays the main content when the drawer opens
	        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	        // set up the drawer's list view with items and click listener
	        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
	                R.layout.drawer_list_item, mPlanetTitles));
	        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

	        // enable ActionBar app icon to behave as action to toggle nav drawer
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	        getActionBar().setHomeButtonEnabled(true);

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
	                getActionBar().setTitle(mTitle);
	                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
	            }

	            public void onDrawerOpened(View drawerView) {
	                getActionBar().setTitle(mDrawerTitle);
	                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
	            }
	        };
	        mDrawerLayout.setDrawerListener(mDrawerToggle);
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
		//MapsInitializer.initialize(this);
		//m_vwMap = mapFragment.getMap();
		//m_vwMap.setMyLocationEnabled(true);
		//m_vwMap.getUiSettings().setCompassEnabled(true);
		//Location l = m_vwMap.getMyLocation();
		//LatLng myLocation = new LatLng(l.getLatitude(), l.getLongitude());
		//LatLng belgrade = new LatLng(44.769010, 20.479202);

		//m_vwMap.setMyLocationEnabled(true);
		//m_vwMap.moveCamera(CameraUpdateFactory.newLatLngZoom(belgrade, 12));
		//m_vwMap.getUiSettings().setZoomControlsEnabled(true);
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = this.getSupportMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	/**
	 * Switch the application so it is or isn't recording the user's path on the map.
	 *  
	 * @param bRecording
	 * 						Whether or not to start recording.
	 */
	private void setRecordingState(boolean bRecording) {
		// TODO
	}

	/**
	 * Writes important map data to a private application file.
	 */
	private void saveRecording() {
		// TODO
	}

	/**
	 * Retrieves specific map data that was previously written to a private application file
	 * and initializes both the lists and the map with the new data.
	 */
	private void loadRecording() {
		// TODO
	}
}