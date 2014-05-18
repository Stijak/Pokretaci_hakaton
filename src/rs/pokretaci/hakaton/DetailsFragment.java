package rs.pokretaci.hakaton;

import com.squareup.picasso.Picasso;

import net.ascho.pokretaci.beans.Goal;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsFragment extends Fragment {
	private TextView mTitle;
	private TextView mDescription;
	private ImageView mImage;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.details_fragment, container, false);
		mTitle = (TextView) view.findViewById(R.id.details_title);
		mDescription = (TextView) view.findViewById(R.id.details_content);
		mImage = (ImageView) view.findViewById(R.id.image);
		return view;
	}
	
	
	protected void setContent(Goal goal) {
		mTitle.setText(goal.title);
		mDescription.setText(goal.description);
		if (goal.image != null && !goal.image.equals("")) Picasso.with(getActivity()).load(goal.image).into(mImage);
	}

}
