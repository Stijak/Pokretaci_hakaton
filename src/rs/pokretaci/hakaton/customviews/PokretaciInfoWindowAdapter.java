package rs.pokretaci.hakaton.customviews;

import net.ascho.pokretaci.beans.Goal;
import rs.pokretaci.hakaton.MapActivity;
import rs.pokretaci.hakaton.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class PokretaciInfoWindowAdapter implements InfoWindowAdapter {
	private MapActivity mMapActivity;
	
	public PokretaciInfoWindowAdapter(MapActivity mapActivity) {
		mMapActivity = mapActivity;
	}

	@Override
	public View getInfoContents(Marker arg0) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		Goal goal = mMapActivity.getRelevantGoal(marker);
		String title = goal.title;
		String author = goal.creator.full_name;
		int numSupporters = goal.supporters_count;
		LinearLayout parentView = (LinearLayout) ((LayoutInflater)mMapActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.info_window_adapter, null, false);
		TextView text = (TextView) parentView.findViewById(R.id.title);
		text.setText(title);
		text = (TextView) parentView.findViewById(R.id.author);
		text.setText(author);
		text = (TextView) parentView.findViewById(R.id.num_supporters);
		text.setText(Integer.toString(numSupporters));
		return parentView;
	}

}
