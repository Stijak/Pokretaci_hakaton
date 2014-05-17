package rs.pokretaci.hakaton.customviews;

import net.ascho.pokretaci.beans.Activist;
import net.ascho.pokretaci.beans.Comment;
import net.ascho.pokretaci.beans.Goal;
import rs.pokretaci.hakaton.R;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GoalView extends LinearLayout {
	private Context mContext;
	private ImageView mImage;
	private TextView mDate;
	private TextView mTitle;
	private TextView mNumSupporters;
	private ImageView mCircle;

	public GoalView(Context context, Goal goal) {
		super(context);
		mContext = context;
		((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.goal_item, this, true);

		mImage = (ImageView) findViewById(R.id.image);
		mDate = (TextView) findViewById(R.id.date);
		mTitle = (TextView) findViewById(R.id.title);
		mNumSupporters = (TextView) findViewById(R.id.num_supporters);
		mCircle = (ImageView) findViewById(R.id.circle_icon);
		setView(goal);
	}
	
	public void setView(Goal goal) {
		mDate.setText(goal.created_at);
		mTitle.setText(goal.title);
		mNumSupporters.setText(Integer.toString(goal.supporters_count));
		if (goal.image != null) Picasso.with(mContext).load(goal.image).into(mImage);
	}

}

