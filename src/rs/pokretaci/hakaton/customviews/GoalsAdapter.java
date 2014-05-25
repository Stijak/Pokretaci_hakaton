package rs.pokretaci.hakaton.customviews;

import java.util.List;

import com.squareup.picasso.Picasso;

import rs.pokretaci.hakaton.R;
import net.ascho.pokretaci.beans.Comment;
import net.ascho.pokretaci.beans.Goal;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoalsAdapter extends BaseAdapter {
	private Context mContext;
	private List<Goal> mGoals; //promjeniti u goal
	
	public GoalsAdapter(Context context, List<Goal> goals) { //TODO promjeniti u goal
		mContext = context;
		mGoals = goals;
	}

	@Override
	public int getCount() {
		return mGoals.size();
	}

	@Override
	public Object getItem(int position) {
		return mGoals.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.goal_item, parent, false);
		}
		setView (mContext, convertView, mGoals.get(position));
		return convertView;
	}
	
	private static void setView(final Context context, View goalView, final Goal goal) {
		ImageView imageLocation = (ImageView) goalView.findViewById(R.id.image_location);
		TextView date = (TextView) goalView.findViewById(R.id.date);
		TextView title = (TextView) goalView.findViewById(R.id.title);
		TextView numSupporters = (TextView) goalView.findViewById(R.id.num_supporters);
		ImageView circle = (ImageView) goalView.findViewById(R.id.circle_icon);
		

		date.setText(goal.created_at);
		title.setText(goal.title);
		numSupporters.setText(Integer.toString(goal.supporters_count));
		if (goal.location_image != null && !goal.location_image.equals("")) Picasso.with(context).load(goal.location_image).resize(500, 200).centerCrop().into(imageLocation);
	}

}
