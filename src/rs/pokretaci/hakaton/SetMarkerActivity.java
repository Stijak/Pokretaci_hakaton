package rs.pokretaci.hakaton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

public class SetMarkerActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {
	private Marker mMarker;
	private LocationClient mLocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_marker_activity);
		Intent intent = getIntent();
		LatLng position = intent.getParcelableExtra(SubmitProblem.LOCATION_EXTRA);
		if (position != null) {
			setMarkerAndMap(position);
		}
	}
	
	private void setMarkerAndMap (LatLng position) {
		
		GoogleMap map = ((MapFragment) getFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		map.setMyLocationEnabled(true);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));

		mMarker = map.addMarker(new MarkerOptions()
		.title(getString(R.string.submit_goal_marker_title))
		.snippet(getString(R.string.submit_goal_marker_details))
		.position(position));
		mMarker.setDraggable(true);
	}

	private void setUpLocationClientIfNeeded() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(
					getApplicationContext(),
					this,  // ConnectionCallbacks
					this); // OnConnectionFailedListener
		}
		mLocationClient.connect();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		//setUpMapIfNeeded();
		setUpLocationClientIfNeeded();
		mLocationClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		if (mMarker != null) return;
		Location location = mLocationClient.getLastLocation();
		LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
		setMarkerAndMap(position);

	}
	
	public void setLocation(View view) {
		Intent intent = this.getIntent();
		LatLng location = mMarker.getPosition();
		intent.putExtra(SubmitProblem.LOCATION_EXTRA, location);
		this.setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}


}
