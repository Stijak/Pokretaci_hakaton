package rs.pokretaci.hakaton.customviews;

import java.util.List;

import net.ascho.pokretaci.beans.Comment;
import net.ascho.pokretaci.beans.Goal;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
		GoalView commentView;
		if (convertView == null) {
			commentView = new GoalView(mContext, mGoals.get(position));
		} else {
			commentView = (GoalView) convertView;
			commentView.setView(mGoals.get(position));
		}
		return commentView;
	}

}
