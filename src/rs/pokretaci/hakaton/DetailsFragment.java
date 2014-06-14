package rs.pokretaci.hakaton;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import net.ascho.pokretaci.backend.beans.ServerResponseObject;
import net.ascho.pokretaci.backend.communication.Task;
import net.ascho.pokretaci.backend.communication.TaskListener;
import net.ascho.pokretaci.backend.executors.goals.GoalInteraction;
import net.ascho.pokretaci.beans.Goal;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsFragment extends Fragment implements OnClickListener, TaskListener {
	private Button mSupportButton;
	private TextView mTitle;
	private TextView mDescription;
	private ImageView mImage;
	private ImageView mLocationImage;
	private ImageView mAvatarImage;
	private TextView mFullName;
	private TextView mDate;
	private ProgressDialog mDialog;
	
	private Goal mGoal;
	private boolean mUserLoggedIn = false;
	
	Transformation transformation = new Transformation() {

        @Override public Bitmap transform(Bitmap source) {
            int targetWidth = mImage.getWidth();

            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                // Same bitmap is returned if sizes are the same
                source.recycle();
            }
            return result;
        }

        @Override public String key() {
            return "transformation" + " desiredWidth";
        }
    };
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mUserLoggedIn = prefs.getBoolean(MapActivity.USER_LOGGED_IN, false);
		Log.d("rs.pokretaci.hakaton", "DetailsFragment onCreate");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
		Log.d("rs.pokretaci.hakaton", "DetailsFragment onCreateView");
		View view = inflater.inflate(R.layout.details_fragment, container, false);
		mTitle = (TextView) view.findViewById(R.id.details_title);
		mDescription = (TextView) view.findViewById(R.id.details_content);
		mImage = (ImageView) view.findViewById(R.id.image);
		mLocationImage = (ImageView) view.findViewById(R.id.location_image);
		mAvatarImage = (ImageView) view.findViewById(R.id.avatar_image);
		mFullName = (TextView) view.findViewById(R.id.full_name);
		mDate = (TextView) view.findViewById(R.id.details_date);
		mSupportButton = (Button) view.findViewById(R.id.support);
		mSupportButton.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onClick(View v) {
		if (!mUserLoggedIn) {
			MapActivity.showNotLoggedInError(getActivity());
			return;
		}
		if (mGoal == null) return;
		int action = Goal.GOAL_INTERACTION_TYPE.SUPPORT_GOAL;
		if (mGoal.supported) action = Goal.GOAL_INTERACTION_TYPE.UNSUPPORT_GOAL;
		Task newGoal = new GoalInteraction(action, mGoal);
		newGoal.executeTask(getActivity().getApplicationContext(), this);
		mDialog = ProgressDialog.show(getActivity(), "", getString(R.string.submiting) , true);
	}
	
	
	protected void setContent(Goal goal) {
		mGoal = goal;
		mTitle.setText(goal.title);
		mDescription.setText(goal.description+"                                                                                  ");
		if (goal.image != null && !goal.image.equals("")) Picasso.with(getActivity()).load(goal.image).into(mImage);
		if (goal.location_image != null && !goal.location_image.equals("")) Picasso.with(getActivity()).load(goal.location_image).transform(transformation)
        .into(mLocationImage);
		if (goal.creator.avatar != null && !goal.creator.avatar.equals("")) Picasso.with(getActivity()).load(goal.creator.avatar).placeholder(R.drawable.avatar).into(mAvatarImage);
		mFullName.setText(goal.creator.full_name);
		mDate.setText(goal.created_at);
		prepareSupportButton();
	}
	
	private void prepareSupportButton() {
		if (mGoal.supported) {
			mSupportButton.setActivated(true);
			mSupportButton.setText(R.string.i_support);
		} else {
			mSupportButton.setActivated(false);
			mSupportButton.setText(R.string.support);
		}
	}

	@Override
	public void onResponse(ServerResponseObject taskResponse) {
		if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
		if (taskResponse != null) {
			if (taskResponse.isResponseValid() && taskResponse.isActionSuccessful()) {
				mGoal.supported = !mGoal.supported;
				prepareSupportButton();
			} else {
				Toast.makeText(getActivity(), R.string.submit_failure, Toast.LENGTH_LONG).show();
			}
		}
	}

}
