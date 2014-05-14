package edu.calpoly.android.walkabout;

import java.util.ArrayList;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

/**
 * Activity that contains an interactive Google Map fragment. Users can record
 * a traveled path, mark the map with information and take pictures that become
 * associated with the map. 
 */
public class WalkAbout extends FragmentActivity {

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

	/** The radius of a Circle drawn on the map, in meters. */
	private static final int CIRCLE_RADIUS = 30;

	/** Constants for the LocationManager. */
	private static final int MIN_TIME_CHANGE = 3000;
	private static final int MIN_DISTANCE_CHANGE = 3;

	/** Request codes for starting new Activities. */
	private static final int ENABLE_GPS_REQUEST_CODE = 1;
	private static final int PICTURE_REQUEST_CODE = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLocationData();
		initLayout();
	}

	/**
	 * Initializes all Location-related data.
	 */
	private void initLocationData() {
		// TODO
	}

	/**
	 * Initializes all other data for the application.
	 */
	private void initLayout() {
		setContentView(R.layout.map_layout);
		//MapsInitializer.initialize(this);
		SupportMapFragment smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		m_vwMap = smf.getMap();
		m_vwMap.setMyLocationEnabled(true);
		m_vwMap.getUiSettings().setCompassEnabled(true);
		//Location l = m_vwMap.getMyLocation();
		//LatLng myLocation = new LatLng(l.getLatitude(), l.getLongitude());
		LatLng belgrade = new LatLng(44.769010, 20.479202);

		m_vwMap.setMyLocationEnabled(true);
		m_vwMap.moveCamera(CameraUpdateFactory.newLatLngZoom(belgrade, 12));
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