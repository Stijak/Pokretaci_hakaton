package rs.pokretaci.hakaton;

import net.ascho.pokretaci.beans.Goal;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment {
	private TextView title;
	private TextView description;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.details_fragment, container, false);
		title = (TextView) view.findViewById(R.id.details_title);
		description = (TextView) view.findViewById(R.id.details_content);
		
		return view;
	}
	
	
	protected void setContent(Goal goal) {
		title.setText(goal.title);
		description.setText(goal.description);
		
	}

}
